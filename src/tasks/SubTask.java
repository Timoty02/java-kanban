package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status) {
        super(name, description, status);

    }

    public SubTask(String name, String description) {
        super(name, description);

    }

    public SubTask(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int duration) {
        super(name, description, status, startTime, endTime, duration);

    }

    public SubTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int epicId) {
        super(id, name, description, status, startTime, endTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int duration, int epicId) {
        super(name, description, status, startTime, endTime, duration);
        this.epicId = epicId;
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
