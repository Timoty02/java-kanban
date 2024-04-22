package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<Integer> subIds = new ArrayList<>();

    public EpicTask(String name, String description) {
        super(name, description);
        type = TaskType.EPIC;
    }

    public EpicTask(String name, String description, LocalDateTime endTime) {
        super(name, description);
        this.endTime = endTime;
        type = TaskType.EPIC;
    }

    public EpicTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, startTime, endTime);
        type = TaskType.EPIC;

    }

    public EpicTask(String name, String description, int id, int duration, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, ArrayList<Integer> subIds) {
        super(name, description, id, duration, status, startTime, endTime);
        this.subIds = subIds;
        type = TaskType.EPIC;
    }


    public void setSubIds(ArrayList<Integer> subIds) {
        this.subIds = subIds;
    }


    public ArrayList<Integer> getSubIds() {
        return subIds;
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public void addSubId(int id) {
        subIds.add(id);
    }
}
