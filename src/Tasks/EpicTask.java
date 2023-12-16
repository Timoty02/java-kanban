package Tasks;

import Tasks.Task;

import java.util.ArrayList;

public class EpicTask extends Task {
    protected ArrayList<Integer> subIds;

    public EpicTask(String name, String description){
        super(name, description);

    }

    public void setSubIds(ArrayList<Integer> subIds) {
        this.subIds = subIds;
    }


    public ArrayList<Integer> getSubIds() {
        return subIds;
    }

    @Override
    public String toString() {
        return "Tasks.EpicTask{" +
                "subIds=" + subIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
