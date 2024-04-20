package test;
import managers.HistoryManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class HttpServerTest {
    HttpTaskServer server;
    Task task1;
    SubTask subTask1;
    EpicTask epicTask1;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer();
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


}
