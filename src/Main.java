import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //тестирование
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        SubTask subTask = new SubTask("Переезд", "Переезд в новый дом", "NEW");
        taskManager.create(subTask);
        subTask = new SubTask("Переезд2", "Переезд в новый дом2", "NEW");
        subTask = taskManager.create(subTask);
        System.out.println(subTask.toString());
        EpicTask epicTask = new EpicTask("Большой переезд",
                "Большой переезд в новый дом");
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        epicTask.setSubIds(ids);
        System.out.println(taskManager.create(epicTask).toString());
        System.out.println(subTask.toString());

        System.out.println(subTask.getEpicId());
        subTask.setStatus("DONE");
        taskManager.update(subTask);
        System.out.println(epicTask.toString());
        taskManager.deleteById(1);
        System.out.println(epicTask.toString());
        taskManager.deleteSubTasks();
        System.out.println(epicTask.toString());

        System.out.println(taskManager.getSubByID(1));
        System.out.println(taskManager.getSubTasks());


    }
}
