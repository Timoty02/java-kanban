package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> taskHistory = new ArrayList<>();

    public void add(Task task){

        if (taskHistory.size() == 10){
            taskHistory.remove(0);
            taskHistory.add(task);
        } else {
            taskHistory.add(task);
        }

    }

    public List<Task> getHistory(){
        return taskHistory;
    }

}
