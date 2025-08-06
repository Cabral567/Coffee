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

        // Barra de menus estilo Notepad++
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(170, 220, 120));

        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem saveItem = new JMenuItem("Salvar");
        saveItem.addActionListener(e -> TaskPersistence.saveTasks(getAllTasks()));
        JMenuItem openItem = new JMenuItem("Abrir");
        openItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir não implementado (todas tarefas são carregadas automaticamente)", "Abrir", JOptionPane.INFORMATION_MESSAGE));
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(saveItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Editar");
        JMenuItem deleteItem = new JMenuItem("Apagar tarefa selecionada");
        deleteItem.addActionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                listModel.remove(idx);
                TaskPersistence.saveTasks(getAllTasks());
            }
        });
        editMenu.add(deleteItem);
        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "To-Do Widget estilo Notepad++\nFeito em Java\nPor Cabral567", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Painel principal para layout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Barra verde com botões
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(170, 220, 120));
        topBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputField.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1));
        inputField.addActionListener(e -> addTask());
        topBar.add(inputField, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        btnPanel.setBackground(new Color(170, 220, 120));

        JButton addBtn = new JButton(new ImageIcon("src/main/resources/add.png"));
        addBtn.setToolTipText("Adicionar tarefa");
        addBtn.setBackground(new Color(200, 240, 180));
        addBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        addBtn.addActionListener(e -> addTask());
        btnPanel.add(addBtn);

        JButton delBtn = new JButton("🗑");
        delBtn.setToolTipText("Apagar tarefa selecionada");
        delBtn.setBackground(new Color(200, 240, 180));
        delBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        delBtn.addActionListener(e -> {
            int idx = taskList.getSelectedIndex();
            if (idx >= 0) {
                listModel.remove(idx);
                TaskPersistence.saveTasks(getAllTasks());
            }
        });
        btnPanel.add(delBtn);

        JButton saveBtn = new JButton("💾");
        saveBtn.setToolTipText("Salvar tarefas");
        saveBtn.setBackground(new Color(200, 240, 180));
        saveBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        saveBtn.addActionListener(e -> TaskPersistence.saveTasks(getAllTasks()));
        btnPanel.add(saveBtn);

        topBar.add(btnPanel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

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
        mainPanel.add(scroll, BorderLayout.CENTER);

        // Sombra e cor de fundo
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(180,180,180), 1));
        mainPanel.setBackground(Color.white);
        setContentPane(mainPanel);
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
