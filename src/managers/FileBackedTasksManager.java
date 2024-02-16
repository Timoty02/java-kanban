package managers;

import exceptions.ManagerSaveException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileBackedTasksManager extends InMemoryTaskManager{
    @Override
    public Task create(Task task){
        Task task1 = super.create(task);
        save();
        return task1;
    }

    @Override
    public EpicTask create(EpicTask task){
        EpicTask task1 = super.create(task);
        save();
        return task1;
    }

    @Override
    public SubTask create(SubTask task){
        SubTask task1 = super.create(task);
        save();
        return task1;
    }

    @Override
    public void update(Task task){
        super.update(task);
        save();
    }

    @Override
    public void update(EpicTask task){
        super.update(task);
        save();
    }

    @Override
    public void update(SubTask task){
        super.update(task);
        save();
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        HashMap<Integer, Task> temp = super.getTasks();
        save();
        return temp;

    }

    @Override
    public HashMap<Integer, EpicTask> getEpicTasks() {
        HashMap<Integer, EpicTask> temp = super.getEpicTasks();
        save();
        return temp;

    }

    @Override
    public HashMap<Integer, SubTask> getSubTasks() {
        HashMap<Integer, SubTask> temp = super.getSubTasks();
        save();
        return temp;

    }

    @Override
    public void deleteTasks(){
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpicTasks(){
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubTasks(){
        super.deleteSubTasks();
        save();
    }

    @Override
    public Task getTaskByID(int id){
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicByID(int id){
        EpicTask task = super.getEpicByID(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubByID(int id){
        SubTask task = super.getSubByID(id);
        save();
        return task;
    }

    @Override
    public void deleteById(int id){
        super.deleteById(id);
        save();
    }

    @Override
    public ArrayList<SubTask> getSubsOfEpic(int id){
        ArrayList<SubTask> subTasks1 = super.getSubsOfEpic(id);
        save();
        return subTasks1;
    }

    @Override
    public void epicStatusUpdater(EpicTask task){
        super.epicStatusUpdater(task);
        save();
    }

    public static FileBackedTasksManager loadFromFile(){
        try {
            Path sourceFile = Paths.get("manager.csv");
            Path targetFile = Paths.get("manager_temp.csv");
            Files.copy(sourceFile, targetFile, REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.format("Произошла ошибка во время копирования файла.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader("manager_temp.csv"))){
            List<String> lines = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                lines.add(line);
            }
            lines.remove(0);
            FileBackedTasksManager manager = new FileBackedTasksManager();
            if (!lines.isEmpty()){
                int i = 0;
                while (!lines.get(i).isBlank() & i!=lines.size()){
                    manager.create(Task.fromString(lines.get(i)));
                    i++;
                }
                try{
                    i++;
                    String[] temps = lines.get(i).split(",");
                    for(String temp:temps){
                        int id = Integer.parseInt(temp);
                        manager.getTaskByID(id);
                    }
                } catch (IndexOutOfBoundsException ignored){

                }

            }
            return manager;

        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }

    }



    private void save(){
        try{
            try(Writer fileWriter = new FileWriter("manager.csv")){
                fileWriter.write("id,type,name,status,description,epic\n");
                List<String> temp = new ArrayList<>();
                if (!tasks.isEmpty()){
                    for(Task task:tasks.values()){
                        temp.add(task.toString() + "\n");
                    }
                }

                if (!epicTasks.isEmpty()){
                    for(EpicTask task:epicTasks.values()){
                        temp.add(task.toString() + "\n");
                    }
                }

                if (!subTasks.isEmpty()){
                    for(SubTask task:subTasks.values()){
                        temp.add(task.toString() + "\n");
                    }
                }

                if (!historyManager.getHistory().isEmpty()){
                    temp.add("\n");
                    temp.add(historyToString(historyManager));
                }
                for(String line:temp){
                    fileWriter.write(line);
                }

            } catch (IOException e){
                throw new ManagerSaveException("Ошибка при сохранении");
            }
        } catch (ManagerSaveException e){
            System.out.println(e.getMessage());
        }


    }

    static String historyToString(HistoryManager manager){
        List<Task> list = manager.getHistory();
        StringBuilder res = new StringBuilder();
        for (Task task:list){
            if (res.toString().isBlank()){
                res.append(task.getId());
            } else {
                res.append(",").append(task.getId());
            }

        }
        return res.toString();
    }

    static List<Integer> historyFromString(String value){
        List<Integer> ids = new ArrayList<>();
        String[] values = value.split(",");
        for (String val:values){
            ids.add(Integer.parseInt(val));
        }
        return ids;

    }



}
