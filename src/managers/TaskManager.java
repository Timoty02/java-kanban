package managers;


import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {


    Task create(Task task);

    Task update(Task task);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, SubTask> getSubTasks();

    HashMap<Integer, EpicTask> getEpicTasks();


    void deleteTasks();

    void deleteSubTasks();

    void deleteEpicTasks();

    void deleteAllTasks();

    Task getByID(int id);

    Task deleteById(int id);

    List<SubTask> getSubsOfEpic(int id);

    void epicUpdater(EpicTask task);

    List<Task> getHistory();


    TreeSet<Task> getSortedTasksByStartTime();

}
