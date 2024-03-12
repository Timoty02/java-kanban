package managers;


import exceptions.TaskException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    HistoryManager historyManager;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();


    TreeSet<Task> sortedTasks = new TreeSet<>(
            (Task task1, Task task2) -> {
                if (task1.getStartTime() == null) {
                    return 1;
                }
                if (task2.getStartTime() == null) {
                    return -1;
                }
                return (int) Duration.between(task2.getStartTime(), task1.getStartTime()).toMinutes();
            });

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    public Task create(Task task) {
        try {
            switch (task.getType()) {
                case TASK:
                    return createTask(task);
                case EPIC:
                    return createEpic((EpicTask) task);
                case SUBTASK:
                    return createSubTask((SubTask) task);
                default:
                    throw new TaskException("Неподдерживаемый тип задачи");
            }
        } catch (TaskException e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Task createTask(Task task) {
        try {
            addToSorted(task);
        } catch (TaskException e) {
            System.out.println(e.getMessage());
            return task;
        }
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask task) {
        try {
            addToSorted(task);
        } catch (TaskException e) {
            System.out.println(e.getMessage());
            return task;
        }
        task.setId(nextId);
        nextId++;
        subTasks.put(task.getId(), task);
        return task;
    }

    public EpicTask createEpic(EpicTask task) {
        task.setId(nextId);
        nextId++;
        epicTasks.put(task.getId(), task);
        if (task.getSubIds() != null && !task.getSubIds().isEmpty()) {
            for (int id : task.getSubIds()) {
                SubTask subTask = subTasks.get(id);
                subTask.setEpicId(task.getId());
                update(subTask);
            }
        }
        epicUpdater(task);
        return task;

    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(SubTask task) {
        EpicTask epicTask = epicTasks.get(task.getEpicId());
        epicUpdater(epicTask);
        subTasks.put(task.getId(), task);
    }

    public void update(EpicTask task) {

        epicTasks.put(task.getId(), task);
    }

    public HashMap<Integer, Task> getTasks() {
        for (Task task : tasks.values()) {
            historyManager.add(task);
        }
        return tasks;

    }

    public HashMap<Integer, SubTask> getSubTasks() {
        for (SubTask task : subTasks.values()) {
            historyManager.add(task);
        }
        return subTasks;
    }

    public HashMap<Integer, EpicTask> getEpicTasks() {
        for (EpicTask task : epicTasks.values()) {
            historyManager.add(task);
        }
        return epicTasks;
    }


    public void deleteTasks() {
        historyManager.removeTasks(tasks);
        tasks.clear();
    }

    public void deleteSubTasks() {
        historyManager.removeSubTasks(subTasks);
        subTasks.clear();
        for (EpicTask task : epicTasks.values()) {

            task.setSubIds(null);
            epicUpdater(task);
        }
    }

    public void deleteEpicTasks() {
        historyManager.removeEpicTasks(epicTasks);
        for (EpicTask task : epicTasks.values()) {
            if (!task.getSubIds().isEmpty()) {
                ArrayList<Integer> subtasks = new ArrayList<>(task.getSubIds());
                for (int i : subtasks) {
                    deleteById(i);
                }
            }

        }
        epicTasks.clear();
    }

    public void deleteAllTasks() {
        deleteTasks();
        deleteEpicTasks();
        deleteSubTasks();
    }

    public Task getByID(int id) {
        if (tasks.containsKey(id)) {
            return getTaskByID(id);
        } else if (epicTasks.containsKey(id)) {
            return getEpicByID(id);
        } else if (subTasks.containsKey(id)) {
            return getSubByID(id);
        } else return null;

    }

    public Task getTaskByID(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }


    }


    public SubTask getSubByID(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            return null;
        }
    }

    public EpicTask getEpicByID(int id) {
        if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        } else {
            return null;
        }
    }

    public void deleteById(int id) {
        tasks.remove(id);
        subTasks.remove(id);
        epicTasks.remove(id);
        historyManager.remove(id);
        for (EpicTask task : epicTasks.values()) {
            ArrayList<Integer> subtasks = task.getSubIds();
            if (subtasks.contains(id)) {
                subtasks.remove(Integer.valueOf(id));
                task.setSubIds(subtasks);
                epicUpdater(task);
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

    public void epicUpdater(EpicTask task) {
        TaskStatus status = TaskStatus.IN_PROGRESS;
        int newSubs = 0;
        int doneSubs = 0;
        LocalDateTime tempStartTime = LocalDateTime.MAX;
        LocalDateTime tempEndTime = LocalDateTime.MIN;
        if (task.getSubIds() != null && !task.getSubIds().isEmpty()) {
            for (int id : task.getSubIds()) {
                SubTask subTask = subTasks.get(id);
                if (subTask.getStartTime().isBefore(tempStartTime)) {
                    tempStartTime = subTask.getStartTime();
                }
                if (subTask.getEndTime().isAfter(tempEndTime)) {
                    tempEndTime = subTask.getEndTime();
                }
                if (TaskStatus.NEW.equals(subTask.getStatus())) {
                    newSubs++;
                } else if (TaskStatus.DONE.equals(subTask.getStatus())) {
                    doneSubs++;
                }
            }
            if (newSubs == task.getSubIds().size()) {
                status = TaskStatus.NEW;
            } else if (doneSubs == task.getSubIds().size()) {
                status = TaskStatus.DONE;
            }

        } else {
            tempStartTime = LocalDateTime.of(0, 1, 1, 0, 0, 0);
            tempEndTime = LocalDateTime.of(0, 1, 1, 0, 0, 0);
            status = TaskStatus.NEW;
        }
        task.setStartTime(tempStartTime);
        task.setEndTime(tempEndTime);
        task.setDuration((int) Duration.between(tempStartTime, tempEndTime).toMinutes());
        task.setStatus(status);
        update(task);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getSortedTasks() {
        return sortedTasks;
    }

    protected boolean addToSorted(Task task) {
        for (Task existingTask : sortedTasks) {
            if (task.getStartTime().isBefore(existingTask.getEndTime()) &&
                    task.getEndTime().isAfter(existingTask.getStartTime())) {
                throw new TaskException("Ошибка! На это время уже есть задача");
            }
        }

        return sortedTasks.add(task);
    }


}
