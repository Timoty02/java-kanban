package tasks;

import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status) {
        super(name, description, status);
        type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description) {
        super(name, description);
        type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime,
                   int duration) {
        super(name, description, status, startTime, endTime, duration);
        type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime,
                   LocalDateTime endTime, int epicId) {
        super(id, name, description, status, startTime, endTime);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime,
                   int duration, int epicId) {
        super(name, description, status, startTime, endTime, duration);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public SubTask(String name, String description, int id, int duration, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int epicId) {
        super(name, description, id, duration, status, startTime, endTime);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + "," + epicId
                + "," + startTime.format(Format) + ","
                + getEndTime().format(Format) + "," + duration;
    }


    public static SubTask copyOf(SubTask task) {
        if (task != null) {
            return new SubTask(task.getName(), task.getDescription(), task.getId(), task.getDuration(), task.getStatus(),
                    task.getStartTime(), task.getEndTime(), task.getEpicId());
        } else {
            return null;
        }
    }
}
