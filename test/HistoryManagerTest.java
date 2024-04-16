package test;

import managers.HistoryManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    SubTask subTask1;
    EpicTask epicTask1;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 1, 11, 0), 60);
        task1.setId(1);
        subTask1 = new SubTask("Subtask 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 11, 1),
                LocalDateTime.of(2023, 1, 1, 12, 1), 60, 2);
        subTask1.setId(2);
        epicTask1 = new EpicTask("Epic Task 1", "Description 1");
        epicTask1.setId(3);
        ArrayList<Integer> subIds = new ArrayList<>(List.of(1));
        epicTask1.setSubIds(subIds);
    }

    @Test
    void add() {
        historyManager.add(task1);
        historyManager.add(subTask1);
        historyManager.add(epicTask1);
        assertTrue(historyManager.getHistory().contains(task1));
        assertTrue(historyManager.getHistory().contains(subTask1));
        assertTrue(historyManager.getHistory().contains(epicTask1));
    }

    @Test
    void addDuble() {
        historyManager.add(task1);
        historyManager.add(subTask1);
        historyManager.add(epicTask1);
        historyManager.add(task1);
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    void remove() {
        historyManager.add(task1);
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(subTask1);
        historyManager.remove(2);
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(epicTask1);
        historyManager.remove(3);
        assertTrue(historyManager.getHistory().isEmpty());
        historyManager.add(task1);
        historyManager.add(epicTask1);
        historyManager.remove(1);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.add(task1);
        historyManager.add(subTask1);
        historyManager.add(epicTask1);
        historyManager.remove(2);
        ArrayList<Task> temp = new ArrayList<>(List.of(epicTask1, task1));
        assertEquals(temp, historyManager.getHistory());
        historyManager.add(task1);
        historyManager.add(subTask1);
        historyManager.add(epicTask1);
        historyManager.remove(1);
        ArrayList<Task> temp1 = new ArrayList<>(List.of(epicTask1, subTask1));
        assertEquals(temp1, historyManager.getHistory());
    }

    @Test
    void removeEmpty() {
        assertNull(historyManager.remove(1));
    }

    @Test
    void removeAll() {
        historyManager.add(task1);
        historyManager.add(subTask1);
        historyManager.add(epicTask1);
        HashMap<Integer, Task> hashMap1 = new HashMap<>(Map.of(1, task1));
        historyManager.removeTasks(hashMap1);
        assertEquals(2, historyManager.getHistory().size());
        HashMap<Integer, EpicTask> hashMap2 = new HashMap<>(Map.of(1, epicTask1));
        historyManager.removeEpicTasks(hashMap2);
        assertEquals(1, historyManager.getHistory().size());
        HashMap<Integer, SubTask> hashMap3 = new HashMap<>(Map.of(1, subTask1));
        historyManager.removeSubTasks(hashMap3);
        assertEquals(0, historyManager.getHistory().size());
    }

}
