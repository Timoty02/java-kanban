package managers;

import exceptions.ManagerSaveException;
import exceptions.TaskException;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String path;

    public FileBackedTaskManager(String fileName) {
        this.path = fileName;
    }

    public FileBackedTaskManager() {
        this.path = "manager.csv";
    }

    @Override
    public Task create(Task task) throws TaskException {
        Task task1 = super.create(task);
        save();
        return task1;
    }


    @Override
    public Task update(Task task) {
        Task task1 = super.update(task);
        save();
        return task1;
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
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
        save();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public EpicTask getEpicByID(int id) {
        EpicTask task = super.getEpicByID(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubByID(int id) {
        SubTask task = super.getSubByID(id);
        save();
        return task;
    }

    @Override
    public Task deleteById(int id) {
        super.deleteById(id);
        save();
        return null;
    }

    @Override
    public List<SubTask> getSubsOfEpic(int id) {
        List<SubTask> subTasks1 = super.getSubsOfEpic(id);
        save();
        return subTasks1;
    }

    @Override
    public void epicUpdater(EpicTask task) {
        super.epicUpdater(task);
        save();
    }

    public static FileBackedTaskManager loadFromFile(String fileName) {
        try {
            Path sourceFile = Paths.get(fileName);
            Path targetFile = Paths.get("manager_temp.csv");
            Files.copy(sourceFile, targetFile, REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.format("Произошла ошибка во время копирования файла.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader("manager_temp.csv"))) {
            List<String> lines = new ArrayList<>();
            while (br.ready()) {
                String line = br.readLine();
                lines.add(line);
            }
            if (!lines.isEmpty()) {
                lines.remove(0);
            }
            FileBackedTaskManager manager = new FileBackedTaskManager(fileName);
            if (!lines.isEmpty()) {

                int i = 0;
                while (i != lines.size() && !lines.get(i).isBlank()) {
                    manager.create(Task.fromString(lines.get(i)));
                    i++;
                }
                manager.setEpicsID();
                try {
                    i++;
                    List<Integer> temps = historyFromString(lines.get(i));
                    Collections.reverse(temps);
                    for (Integer temp : temps) {
                        manager.getByID(temp);
                    }
                } catch (IndexOutOfBoundsException ignored) {

                }

            }
            return manager;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new FileBackedTaskManager(fileName);
        }

    }


    private void save() {
        try {
            try (Writer fileWriter = new FileWriter(path)) {
                fileWriter.write("id,type,name,status,description,epic,startTime,endTime,duration\n");
                List<String> temp = new ArrayList<>();
                if (!sortedTasksById.isEmpty()) {
                    for (Task task : sortedTasksById) {
                        temp.add(task.toString() + "\n");
                    }
                }

                if (!historyManager.getHistory().isEmpty()) {
                    temp.add("\n");
                    temp.add(historyToString(historyManager));
                }
                for (String line : temp) {
                    fileWriter.write(line);
                }

            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка при сохранении");
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }


    }

    static String historyToString(HistoryManager manager) {
        List<Task> list = manager.getHistory();
        StringBuilder res = new StringBuilder();
        for (Task task : list) {
            if (res.toString().isBlank()) {
                res.append(task.getId());
            } else {
                res.append(",").append(task.getId());
            }

        }
        return res.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        String[] values = value.split(",");
        for (String val : values) {
            ids.add(Integer.parseInt(val));
        }
        return ids;

    }

    protected void setEpicsID() {
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : subTasks.values()) {
                if (epicTasks.containsKey(subTask.getEpicId())) {
                    EpicTask task = epicTasks.get(subTask.getEpicId());
                    task.addSubId(subTask.getId());
                    updateEpic(task);
                }
            }
        }
    }


}
