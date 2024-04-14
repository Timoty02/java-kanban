package test;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTaskManager();
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 1, 11, 0), 60);
        subTask1 = new SubTask("Subtask 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 11, 1),
                LocalDateTime.of(2023, 1, 1, 12, 1), 60, 2);
        epicTask1 = new EpicTask("Epic Task 1", "Description 1");
        ArrayList<Integer> subIds = new ArrayList<>(List.of(1));
        epicTask1.setSubIds(subIds);
    }

    @Test
    void saveTask() {
        taskManager.create(task1);
        Path filePath1 = Paths.get("C:\\Users\\Tim\\IdeaProjects\\java-kanban\\manager.csv");
        Path filePath2 = Paths.get("C:\\Users\\Tim\\IdeaProjects\\java-kanban\\src\\Tests\\manager_test_task.csv");
        try {
            long mismatch = filesCompareByByte(filePath1, filePath2);
            assertEquals(-1, mismatch);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void saveEpicTask() {
        epicTask1.setSubIds(null);
        taskManager.create(epicTask1);
        Path filePath1 = Paths.get("C:\\Users\\Tim\\IdeaProjects\\java-kanban\\manager.csv");
        Path filePath2 = Paths.get("C:\\Users\\Tim\\IdeaProjects\\java-kanban\\src\\Tests\\manager_test_epic_1.csv");
        try {
            long mismatch = filesCompareByByte(filePath1, filePath2);
            assertEquals(-1, mismatch);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void loadSoloEpicNoHistory() {
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile("C:\\Users\\Tim\\IdeaProjects\\java-kanban\\src\\Tests\\manager_test_epic.csv");
        epicTask1.setSubIds(null);
        taskManager.create(epicTask1);
        assertEquals(taskManager.getEpicTasks(), taskManager1.getEpicTasks());
        assertEquals(taskManager.getHistory(), taskManager1.getHistory());
    }

    @Test
    void loadNoTasks() {

    }

    @Test
    void saveSeveralTasks() {
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(3);
        Task task2 = taskManager.getByID(1);
        System.out.println(task2);
    }

    //метод сравнения файлов взятый из интернета
    private static long filesCompareByByte(Path path1, Path path2) throws IOException {
        try (BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(path1.toFile()));
             BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(path2.toFile()))) {

            int ch;
            long pos = 1;
            while ((ch = fis1.read()) != -1) {
                if (ch != fis2.read()) {
                    return pos;
                }
                pos++;
            }
            if (fis2.read() == -1) {
                return -1;
            } else {
                return pos;
            }
        }
    }

}
