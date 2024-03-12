import managers.*;
import tasks.EpicTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        EpicTask epicTask1 = new EpicTask("Epic Task 1", "Description 1");
        //ArrayList<Integer> subIds = new ArrayList<>(List.of(1));
        //epicTask1.setSubIds(subIds);
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 1, 11, 0), 60);
        FileBackedTaskManager tasksManager = new FileBackedTaskManager();
        tasksManager.create(epicTask1);
    }
}
