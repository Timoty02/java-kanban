package Tasks;

import Tasks.Task;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status){
        super(name, description, status);

    }

    public void setEpicId(int epicId){
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
