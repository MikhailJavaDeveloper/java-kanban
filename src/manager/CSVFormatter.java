package manager;

import tasks.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class CSVFormatter {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(Task task) throws IOException {
        switch (task.getType()) {
            case TaskTypes.TASK:
                switch (task.getStatus()) {
                    case TaskStatuses.NEW:
                        return task.getId() + ",TASK," + task.getName() + ",NEW," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() + ",";
                    case TaskStatuses.IN_PROGRESS:
                        return task.getId() + ",TASK," + task.getName() + ",IN_PROGRESS," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() + ",";
                    case TaskStatuses.DONE:
                        return task.getId() + ",TASK," + task.getName() + ",DONE," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() + ",";
                    default:
                        throw new IOException();
                }
            case TaskTypes.EPIC:
                switch (task.getStatus()) {
                    case TaskStatuses.NEW:
                        return task.getId() + ",EPIC," + task.getName() + ",NEW," + task.getDescription() + ",";
                    case TaskStatuses.IN_PROGRESS:
                        return task.getId() + ",EPIC," + task.getName() + ",IN_PROGRESS," + task.getDescription() + ",";
                    case TaskStatuses.DONE:
                        return task.getId() + ",EPIC," + task.getName() + ",DONE," + task.getDescription() + ",";
                    default:
                        throw new IOException();
                }
            case TaskTypes.SUBTASK:
                Subtask subtask = (Subtask) task;
                switch (task.getStatus()) {
                    case TaskStatuses.NEW:
                        return task.getId() + ",SUBTASK," + task.getName() + ",NEW," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() + "," +
                            subtask.getEpic().getId();
                    case TaskStatuses.IN_PROGRESS:
                        return task.getId() + ",SUBTASK," + task.getName() + ",IN_PROGRESS," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() +
                            "," + subtask.getEpic().getId();
                    case TaskStatuses.DONE:
                        return task.getId() + ",SUBTASK," + task.getName() + ",DONE," + task.getDescription() +
                            "," + task.getDurationInMinutes() + "," + task.getStartTime() + "," +
                            subtask.getEpic().getId();
                    default:
                        throw new IOException();
                }
            default:
                throw new IOException();
        }
    }

    public static Task fromString(String line) throws IOException {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        String status = fields[3];
        String description = fields[4];
        long minutes = fields.length > 5 ? Long.parseLong(fields[5]) : 0;
        String startTime = fields.length > 6 ? fields[6] : null;
        String epic = fields.length > 7 ? fields[7] : null;

        Task task = null;
        switch (type) {
            case "TASK":
                switch (status) {
                    case "NEW":
                        task = new Task(name, description, TaskStatuses.NEW,
                            Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    case "IN_PROGRESS":
                        task = new Task(name, description, TaskStatuses.IN_PROGRESS,
                                Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    case "DONE":
                        task = new Task(name, description, TaskStatuses.DONE,
                                Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    default:
                        throw new IOException();
                }
                task.setId(id);
                break;
            case "EPIC":
                switch (status) {
                    case "NEW":
                        task = new Epic(name, description);
                        break;
                    case "IN_PROGRESS":
                        task = new Epic(name, description);
                        break;
                    case "DONE":
                        task = new Epic(name, description);
                        break;
                    default:
                        throw new IOException();
                }
                task.setId(id);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(epic);
                switch (status) {
                    case "NEW":
                        task = new Subtask(name, description, TaskStatuses.NEW, epicId,
                                Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    case "IN_PROGRESS":
                        task = new Subtask(name, description, TaskStatuses.IN_PROGRESS, epicId,
                                Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    case "DONE":
                        task = new Subtask(name, description, TaskStatuses.DONE, epicId,
                                Duration.ofMinutes(minutes), LocalDateTime.parse(startTime));
                        break;
                    default:
                        throw new IOException();
                }
                task.setId(id);
                break;
            default:
                throw new IOException();
        }
        return task;
    }
}
