package tasks;

import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status){
        super(name, description, status);

    }

    public SubTask(String name, String description){
        super(name, description);

    }

    public void setEpicId(int epicId){
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String res = id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," + epicId
                + startTime.format(DateTimeFormatter.ISO_DATE_TIME) + ","
                + getEndTime().format(DateTimeFormatter.ISO_DATE_TIME) + "," + duration;
        return res;
    }
}
