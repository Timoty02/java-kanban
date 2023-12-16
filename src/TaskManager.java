import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    HistoryManager historyManager = Managers.getDefaultHistory();

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();

    Task create(Task task);

    SubTask create(SubTask task);

    EpicTask create(EpicTask task);

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

    void epicStatusUpdater(EpicTask task);

     List<Task> getHistory();




}
