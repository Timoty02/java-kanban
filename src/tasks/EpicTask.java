package tasks;

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
        String res = id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + ",0";
        return res;
    }

    /*@Override
    public Task fromString(String value){
        String[] values = value.split(",");
        EpicTask epicTask = new EpicTask(values[2], values[4]);
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


        epicTask.setStatus(res1);
        epicTask.setId(Integer.parseInt(values[0]));
        return epicTask;
    }*/
}
