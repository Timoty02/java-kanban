public class SubTask extends Task{
    protected int epicId;

    SubTask(String name, String description, String status){
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
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
