package managers;


import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {


    Task create(Task task);

    void update(Task task);

    void update(SubTask task);

    void update(EpicTask task);

    HashMap<Integer, Task> getTasks();

    HashMap<Integer, SubTask> getSubTasks();

    HashMap<Integer, EpicTask> getEpicTasks();


    void deleteTasks();

    void deleteSubTasks();

    void deleteEpicTasks();

    void deleteAllTasks();

    Task getTaskByID(int id);


    SubTask getSubByID(int id);

    EpicTask getEpicByID(int id);

    void deleteById(int id);

    ArrayList<SubTask> getSubsOfEpic(int id);

    void epicUpdater(EpicTask task);

    List<Task> getHistory();


    TreeSet<Task> getSortedTasks();

}
