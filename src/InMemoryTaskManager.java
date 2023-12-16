import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Task create(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask create(SubTask task) {
        task.setId(nextId);
        nextId++;
        subTasks.put(task.getId(), task);
        return task;
    }

    public EpicTask create(EpicTask task) {
        task.setId(nextId);
        nextId++;
        epicTasks.put(task.getId(), task);
        if (task.getSubIds() != null) {
            for (int id : task.getSubIds()) {
                SubTask subTask = subTasks.get(id);
                subTask.setEpicId(task.getId());
                update(subTask);// Не уверен, нужно ли здесь вызывать метод update
            }
        }
        epicStatusUpdater(task);
        return task;

    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(SubTask task) {
        EpicTask epicTask = epicTasks.get( task.getEpicId());
        epicStatusUpdater(epicTask);
        subTasks.put(task.getId(), task);
    }

    public void update(EpicTask task) {

        epicTasks.put(task.getId(), task);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;

    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }


    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        for (EpicTask task:epicTasks.values()) {

            task.setSubIds(null);
            epicStatusUpdater(task);
        }
    }

    public void deleteEpicTasks() {
        epicTasks.clear();
    }

    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
    }

    public Task getTaskByID(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);

    }


    public SubTask getSubByID(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    public EpicTask getEpicByID(int id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    public void deleteById(int id) {
        tasks.remove(id);
        subTasks.remove(id);
        epicTasks.remove(id);
        for (EpicTask task:epicTasks.values()) {
            ArrayList<Integer> subtasks = task.getSubIds();
            if (subtasks.contains(id)){
                subtasks.remove(Integer.valueOf(id));
                task.setSubIds(subtasks);
                epicStatusUpdater(task);
            }

        }

    }

    public ArrayList<SubTask> getSubsOfEpic(int id) {
        EpicTask epicTask = epicTasks.get(id);
        ArrayList<Integer> subIds = epicTask.getSubIds();
        ArrayList<SubTask> subs = new ArrayList<>();
        for (int i : subIds) {
            subs.add(subTasks.get(i));
        }
        return subs;

    }

    public void epicStatusUpdater(EpicTask task){
        TaskStatus status = TaskStatus.IN_PROGRESS;
        int newSubs = 0;
        int doneSubs = 0;
        if (task.getSubIds() != null) {
            for (int id : task.getSubIds()) {
                SubTask subTask = subTasks.get(id);
                if (TaskStatus.NEW.equals(subTask.getStatus())) {
                    newSubs++;
                } else if (TaskStatus.DONE.equals(subTask.getStatus())) {
                    doneSubs++;
                } else {
                    break;
                }
            }
            if (newSubs == task.getSubIds().size()){
                status = TaskStatus.NEW;
            } else if (doneSubs == task.getSubIds().size()){
                status = TaskStatus.DONE;
            }

        } else {
            status = TaskStatus.NEW;
        }


        task.setStatus(status);
        update(task);
    }

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
