package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String name;
    protected String description;
    protected int id;

    protected int duration;
    protected TaskStatus status;
    protected LocalDateTime startTime;

    public Task(String name, String description){
        this.name = name;
        this.description = description;
    }
    public Task (String name, String description, TaskStatus status){
        this.name = name;
        this.description = description;
        this.status = status;
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
        String res = id + "," + TaskType.TASK + "," + name + "," + status + "," + description + ",0"
            + startTime.format(DateTimeFormatter.ISO_DATE_TIME) + ","
            + getEndTime().format(DateTimeFormatter.ISO_DATE_TIME) + "," + duration;
        return res;
    }

    public static Task fromString(String value){
        String[] values = value.split(",");
        Task task;
        switch(values[1]){
            case "TASK":
                task = new Task(values[2], values[4]);
                break;
            case "EPIC":
                task = new EpicTask(values[2], values[4], LocalDateTime.parse(values[7], DateTimeFormatter.ISO_DATE_TIME));
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
        switch(values[3]){
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
        task.setStartTime(LocalDateTime.parse(values[6], DateTimeFormatter.ISO_DATE_TIME));
        task.setDuration(Integer.parseInt(values[8]));
        return task;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
