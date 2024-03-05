package managers;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    void removeTasks(Map<Integer, Task> Tasks);

    void removeEpicTasks(Map<Integer, EpicTask> epicTasks);

    void removeSubTasks(Map<Integer, SubTask> SubTasks);

}
