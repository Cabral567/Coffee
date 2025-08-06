package widget;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Interface principal do widget To-Do List.
 */
public class ToDoWidgetApp extends JFrame {
    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;
    private JTextField inputField;
    private List<Task> tasks;

    public ToDoWidgetApp() {
        super("To-Do Widget");
        // setUndecorated(true); // Removido para exibir barra de navegação
        // setAlwaysOnTop(true); // Removido para exibir barra de navegação
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Carrega tarefas
        tasks = TaskPersistence.loadTasks();
        listModel = new DefaultListModel<>();
        tasks.forEach(listModel::addElement);

        // Campo de entrada
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputField.addActionListener(e -> addTask());
        add(inputField, BorderLayout.NORTH);

        // Lista de tarefas
        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        taskList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int idx = taskList.locationToIndex(e.getPoint());
                if (idx >= 0) {
                    Task t = listModel.get(idx);
                    t.setCompleted(!t.isCompleted());
                    taskList.repaint();
                    TaskPersistence.saveTasks(getAllTasks());
                }
            }
        });
        JScrollPane scroll = new JScrollPane(taskList);
        add(scroll, BorderLayout.CENTER);

        // Sombra e cor de fundo
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(180,180,180), 1));
        getContentPane().setBackground(Color.white);
    }

    private void addTask() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            Task t = new Task(text);
            listModel.addElement(t);
            inputField.setText("");
            TaskPersistence.saveTasks(getAllTasks());
        }
    }

    private List<Task> getAllTasks() {
        List<Task> all = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            all.add(listModel.get(i));
        }
        return all;
    }

    public static void main(String[] args) {
        try { FlatLightLaf.install(); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            ToDoWidgetApp app = new ToDoWidgetApp();
            app.setVisible(true);
        });
    }

    // Renderizador customizado para mostrar checkbox
    private static class TaskCellRenderer extends JCheckBox implements ListCellRenderer<Task> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getDescription());
            setSelected(value.isCompleted());
            setFont(list.getFont());
            setBackground(isSelected ? new Color(230,240,255) : Color.white);
            setForeground(Color.black);
            return this;
        }
    }
}
