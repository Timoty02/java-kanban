package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTask extends Task {
    protected LocalDateTime endTime;
    protected ArrayList<Integer> subIds;

    public EpicTask(String name, String description){
        super(name, description);

    }

    public EpicTask(String name, String description, LocalDateTime endTime) {
        super(name, description);
        this.endTime = endTime;
    }


    public void setSubIds(ArrayList<Integer> subIds) {
        this.subIds = subIds;
    }


    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    @Override
    public String toString() {
        String res = id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + ",0";
        return res;
    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
