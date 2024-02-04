package managers;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> taskHistory = new HashMap<>();

    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;

    public void add(Task task){
        if (taskHistory.containsKey(task.getId())){
            remove(task.getId());
        }
        taskHistory.put(task.getId(), linkLast(task));
    }

    public void remove(int id){
        Task task = removeNode(taskHistory.remove(id));
        /*try {
            EpicTask epicTask = (EpicTask) task;
        } catch (ClassCastException e){
            return;
        }

            EpicTask epicTask = (EpicTask) task;
            assert epicTask != null;
            if (!epicTask.getSubIds().isEmpty()){
                for (int j:epicTask.getSubIds()) {
                    remove(j);
                }
            }*/



    }

    public void removeTasks(Map<Integer, Task> tasks){
        if (tasks.isEmpty()){
            return;
        } else {
            for (int i = 0; i <= tasks.size(); i++) {
                remove(tasks.get(i).getId());
            }
        }
    }

    public void removeEpicTasks(Map<Integer, EpicTask> tasks){
        if (tasks.isEmpty()){
            return;
        } else {
            for (int i:tasks.keySet()){
                remove(tasks.get(i).getId());
                /*if (!tasks.get(i).getSubIds().isEmpty()){
                    for (int j:tasks.get(i).getSubIds()) {
                        remove(j);
                    }
                }*/
            }
        }
    }

    public void removeSubTasks(Map<Integer, SubTask> tasks){
        if (tasks.isEmpty()){
            return;
        } else {
            for (int i = 0; i <= tasks.size(); i++) {
                remove(tasks.get(i).getId());
            }
        }
    }

    public List<Task> getHistory(){
        return getTasks();
    }

    public Node linkLast(Task task){
        Node<Task> newNode = new Node<Task>(task,null, tail);
        size++;
        if (head == null){
            head = newNode;
            return newNode;
        } else if (tail == null){
            tail = newNode;
            head.setLinkNext(tail);
            tail.setLinkPrev(head);
            return tail;
        } else {
            tail.setLinkNext(newNode);
            tail = newNode;
            return tail;
        }

    }

    private Task removeNode (Node<Task> node) {
        if (size != 0) {
            if (size == 1){
                head = null;
                tail = null;
            } else {
                if (node == head) {
                    head = node.getLinkNext();
                    head.setLinkPrev(null);
                } else if (node == tail) {
                    tail = node.getLinkPrev();
                    tail.setLinkNext(null);
                } else {
                    node.getLinkPrev().setLinkNext(node.getLinkNext());
                    node.getLinkNext().setLinkPrev(node.getLinkPrev());
                }
            }
            size --;
            return node.getData();
        } else {
            return null;
        }
    }

    public List<Task> getTasks(){
        List<Task> historyTaskList = new ArrayList<>();
        Node<Task> nTask = tail;
        if (nTask == null && head == null){
            System.out.println("История просмотров пуста");
        } else if (nTask == null){
            historyTaskList.add(head.getData());
        } else {
            while (nTask != null){
                historyTaskList.add(nTask.getData());
                nTask = nTask.getLinkPrev();
            }
        }
        return historyTaskList;

    }

}
