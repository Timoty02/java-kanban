package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

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
    }

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int duration, TaskStatus status, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = status;
        this.startTime = startTime;
        calculateEndTime();
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task( int id, String name, String description, TaskStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.id = id;

        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
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
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + ",0,"
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

    protected void calculateEndTime(){
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
        return duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }//логичнее было бы сравнивать поле id, но id выдаёт менеджер, что было бы проблемой в тестах

    @Override
    public int hashCode() {
        return Objects.hash(name, description, duration, status, startTime, endTime);
    }
}
