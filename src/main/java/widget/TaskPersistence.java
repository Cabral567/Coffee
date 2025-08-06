package widget;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Responsável por salvar e carregar tarefas em JSON.
 */
public class TaskPersistence {
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + ".todo_widget_tasks.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Task> loadTasks() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) return new ArrayList<>();
            return mapper.readValue(new File(FILE_PATH), new TypeReference<List<Task>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveTasks(List<Task> tasks) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
