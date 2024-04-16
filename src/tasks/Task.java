package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected TaskType type;

    protected static final DateTimeFormatter Format = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss");
    protected String name;
    protected String description;
    protected int id;

    protected int duration;
    protected TaskStatus status;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        type = TaskType.TASK;
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        type = TaskType.TASK;
    }

    public Task(String name, String description, int duration, TaskStatus status, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = status;
        this.startTime = startTime;
        calculateEndTime();
        type = TaskType.TASK;
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        type = TaskType.TASK;
    }

    public Task(int id, String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.id = id;

        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        type = TaskType.TASK;
    }

    public Task(String name, String description, int id, int duration, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.duration = duration;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        type = TaskType.TASK;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + ",0,"
                + startTime.format(Format) + ","
                + getEndTime().format(Format) + "," + duration;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task;
        switch (values[1]) {
            case "TASK":
                task = new Task(values[2], values[4]);
                break;
            case "EPIC":
                task = new EpicTask(values[2], values[4], LocalDateTime.parse(values[7], Format));
                break;
            case "SUBTASK":
                task = new SubTask(values[2], values[4]);
                ((SubTask) task).setEpicId(Integer.parseInt(values[5]));
                break;
            default:
                task = null;
                break;

        }
        TaskStatus res1;
        switch (values[3]) {
            case "NEW":
                res1 = TaskStatus.NEW;
                break;
            case "DONE":
                res1 = TaskStatus.DONE;
                break;
            case "IN_PROGRESS":
                res1 = TaskStatus.IN_PROGRESS;
                break;
            default:
                res1 = null;
                break;

        }
        assert task != null;
        task.setStatus(res1);
        task.setId(Integer.parseInt(values[0]));
        task.setStartTime(LocalDateTime.parse(values[6], Format));
        task.setDuration(Integer.parseInt(values[8]));
        return task;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        calculateEndTime();
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    protected void calculateEndTime() {
        this.endTime = startTime.plusMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration && Objects.equals(name, task.name)
            && Objects.equals(description, task.description) && status == task.status
            && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, duration, status, startTime, endTime);
    }

    public static Task copyOf(Task task) {
        if (task != null) {
            return new Task(task.getName(), task.getDescription(), task.getId(), task.getDuration(), task.getStatus(),
                    task.getStartTime(), task.getEndTime());
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public TaskType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
