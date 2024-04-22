package managers;

import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node<Task>> taskHistory = new HashMap<>();

    public Node<Task> head = null;
    public Node<Task> tail = null;
    private int size = 0;

    public void add(Task task) {
        if (taskHistory.containsKey(task.getId())) {
            remove(task.getId());
        }
        taskHistory.put(task.getId(), linkLast(task));
    }

    public Task remove(int id) {
        return removeNode(taskHistory.remove(id));
    }

    public void removeTasks(Map<Integer, Task> tasks) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                remove(tasks.get(i).getId());
            }
        }
    }

    public void removeEpicTasks(Map<Integer, EpicTask> tasks) {
        if (!tasks.isEmpty()) {

            for (Integer i : tasks.keySet()) {
                remove(tasks.get(i).getId());

            }
        }
    }

    public void removeSubTasks(Map<Integer, SubTask> tasks) {
        if (!tasks.isEmpty()) {
            for (Integer i : tasks.keySet()) {
                remove(tasks.get(i).getId());
            }
        }
    }

    public List<Task> getHistory() {
        return getTasks();
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>(task, null, tail);
        size++;
        if (head == null) {
            head = newNode;
            return newNode;
        } else if (tail == null) {
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

    private Task removeNode(Node<Task> node) {
        if (size != 0) {
            if (size == 1) {
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
            size--;
            return node.getData();
        } else {
            return null;
        }
    }

    private List<Task> getTasks() {
        List<Task> historyTaskList = new ArrayList<>();
        Node<Task> nTask = tail;
        if (nTask == null && head != null) {
            historyTaskList.add(head.getData());
        } else {
            while (nTask != null) {
                historyTaskList.add(nTask.getData());
                nTask = nTask.getLinkPrev();
            }
        }
        return historyTaskList;

    }

}
