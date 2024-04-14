package test;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 1, 11, 0), 60);
        subTask1 = new SubTask("Subtask 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 11, 1),
                LocalDateTime.of(2023, 1, 1, 12, 1), 60, 2);
        epicTask1 = new EpicTask("Epic Task 1", "Description 1");
        ArrayList<Integer> subIds = new ArrayList<>(List.of(1));
        epicTask1.setSubIds(subIds);
    }

    @Test
    void getByID() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        assertEquals(task1, taskManager.getByID(task1.getId()));
        assertEquals(subTask1, taskManager.getByID(subTask1.getId()));
        assertEquals(epicTask1, taskManager.getByID(epicTask1.getId()));
    }

}
