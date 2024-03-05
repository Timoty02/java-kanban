package tasks;

import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status) {
        super(name, description, status);

    }

    public SubTask(String name, String description) {
        super(name, description);

    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," + epicId
                + "," + startTime.format(Format) + ","
                + getEndTime().format(Format) + "," + duration;
    }
}
