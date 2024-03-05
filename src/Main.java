import managers.*;
import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //тестирование было решено оставить в этом методе
        System.out.println("Поехали!");
        TaskManager taskManager = new FileBackedTasksManager();

        /*System.out.println("История");
        if(taskManager.getHistory()!=null){
            for (Task task:taskManager.getHistory()){
                System.out.println(task);
            }
        }*/

        SubTask subTask = new SubTask("Переезд", "Переезд в новый дом", TaskStatus.NEW);
        subTask.setStartTime(LocalDateTime.now().minusDays(3));
        subTask.setDuration(100);
        taskManager.create(subTask);
        subTask = new SubTask("Переезд2", "Переезд в новый дом2", TaskStatus.NEW);
        subTask.setStartTime(LocalDateTime.now().minusDays(4));
        subTask.setDuration(200);
        subTask = taskManager.create(subTask);
        //System.out.println(subTask.toString());
        EpicTask epicTask = new EpicTask("Большой переезд",
                "Большой переезд в новый дом");
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        epicTask.setSubIds(ids);
        System.out.println(taskManager.create(epicTask).toString());
        System.out.println(taskManager.getSortedTasks());
        /*System.out.println(subTask.toString());

        System.out.println(subTask.getEpicId());
        subTask.setStatus(TaskStatus.DONE);
        taskManager.update(subTask);
        System.out.println(epicTask.toString());
        //taskManager.deleteById(1);
        System.out.println(epicTask.toString());
        //taskManager.deleteSubTasks();
        System.out.println(epicTask.toString());

        System.out.println(taskManager.getEpicByID(3));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("История");
        for (Task task:taskManager.getHistory()){
            System.out.println(task);
        }
        System.out.println(taskManager.getSubByID(2));
        System.out.println(taskManager.getSubByID(1));

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("История");
        for (Task task:taskManager.getHistory()){
            System.out.println(task);
        }
        //taskManager.deleteAllTasks();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("История");
        for (Task task:taskManager.getHistory()){
            System.out.println(task);
        }*/


    }
}
