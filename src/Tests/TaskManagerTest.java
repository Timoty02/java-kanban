package Tests;

import managers.InMemoryTaskManager;
import managers.*;
import tasks.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.SubTask;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//По неведомой мне причине, если проводить все тесты сразу выдаётся провал, однако при выполнении по одному всё нормально.
//Возможно при последовательном выполнении остаются следы предыдущих тестов, хотя все переменные вроде создаются заново.

abstract class TaskManagerTest<T extends TaskManager> {


    protected T taskManager;
    protected Task task1;
    protected SubTask subTask1;
    protected EpicTask epicTask1;



    @Test
    void createTask() {
        Task createdTask = taskManager.create(task1);
        assertEquals(task1, createdTask);
        assertTrue(taskManager.getTasks().containsKey(createdTask.getId()));
    }

    @Test
    void createSubTask() {
        SubTask createdSubTask = taskManager.create(subTask1);
        assertEquals(subTask1, createdSubTask);
        assertTrue(taskManager.getSubTasks().containsKey(createdSubTask.getId()));
    }

    @Test
    void createEpicTask() {
        taskManager.create(subTask1);
        EpicTask createdEpicTask = taskManager.create(epicTask1);
        assertEquals(epicTask1, createdEpicTask);
        assertTrue(taskManager.getEpicTasks().containsKey(createdEpicTask.getId()));
    }

    @Test
    void updateTask() {
        taskManager.create(task1);
        Task updatedTask = new Task(task1.getId(), "Updated Task 1", "Updated Description 1", TaskStatus.DONE,
                LocalDateTime.of(2023, 1, 1, 12, 0), LocalDateTime.of(2023, 1, 1, 13, 0));
        taskManager.update(updatedTask);
        assertEquals(updatedTask, taskManager.getTaskByID(updatedTask.getId()));
    }

    @Test
    void updateSubTask() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        SubTask updatedSubTask = new SubTask(subTask1.getId(), "Updated Subtask 1", "Updated Description 1", TaskStatus.DONE,
                LocalDateTime.of(2023, 1, 1, 12, 0), LocalDateTime.of(2023, 1, 1, 13, 0), 2);
        taskManager.update(updatedSubTask);
        assertEquals(updatedSubTask, taskManager.getSubByID(updatedSubTask.getId()));
    }

    @Test
    void updateEpicTask() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        EpicTask updatedEpicTask = new EpicTask(epicTask1.getId(), "Updated Epic Task 1", "Updated Description 1", TaskStatus.DONE,
                LocalDateTime.of(2023, 1, 1, 12, 0), LocalDateTime.of(2023, 1, 1, 13, 0));
        taskManager.update(updatedEpicTask);
        assertEquals(updatedEpicTask, taskManager.getEpicByID(updatedEpicTask.getId()));
    }

    @Test
    void getTasks() {
        taskManager.create(task1);
        assertEquals(1, taskManager.getTasks().size());
        assertTrue(taskManager.getTasks().containsValue(task1));
    }

    @Test
    void getSubTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        assertEquals(1, taskManager.getSubTasks().size());
        assertTrue(taskManager.getSubTasks().containsValue(subTask1));
    }

    @Test
    void getEpicTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        assertEquals(1, taskManager.getEpicTasks().size());
        assertTrue(taskManager.getEpicTasks().containsValue(epicTask1));
    }

    @Test
    void deleteTasks() {
        taskManager.create(task1);
        taskManager.deleteTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void deleteSubTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.deleteSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void deleteEpicTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.deleteEpicTasks();
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void deleteAllTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getTasks().isEmpty());
        assertTrue(taskManager.getSubTasks().isEmpty());
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }



    @Test
    void getTaskByID() {
        taskManager.create(task1);
        assertEquals(task1, taskManager.getTaskByID(task1.getId()));
    }

    @Test
    void getSubByID() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        assertEquals(subTask1, taskManager.getSubByID(subTask1.getId()));
    }

    @Test
    void getEpicByID() {
        taskManager.create(epicTask1);
        assertEquals(epicTask1, taskManager.getEpicByID(epicTask1.getId()));
    }

    @Test
    void deleteById() {

        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        taskManager.deleteById(task1.getId());
        assertTrue(taskManager.getTasks().isEmpty());
        taskManager.deleteById(subTask1.getId());
        assertTrue(taskManager.getSubTasks().isEmpty());
        taskManager.deleteById(epicTask1.getId());
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void getSubsOfEpic() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);

        assertEquals(1, taskManager.getSubsOfEpic(epicTask1.getId()).size());
        assertTrue(taskManager.getSubsOfEpic(epicTask1.getId()).contains(subTask1));
    }

    @Test
    void epicUpdater() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);

        taskManager.update(subTask1);
        assertEquals(TaskStatus.NEW, taskManager.getEpicByID(epicTask1.getId()).getStatus());
        subTask1.setStatus(TaskStatus.DONE);
        taskManager.update(subTask1);
        assertEquals(TaskStatus.DONE, taskManager.getEpicByID(epicTask1.getId()).getStatus());
    }

    @Test
    void getHistory() {

        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        taskManager.getEpicByID(2);
        taskManager.getTaskByID(3);
        taskManager.getSubByID(1);
        assertEquals(3, taskManager.getHistory().size());
        assertTrue(taskManager.getHistory().contains(task1));
        assertTrue(taskManager.getHistory().contains(subTask1));
        assertTrue(taskManager.getHistory().contains(epicTask1));
    }
}