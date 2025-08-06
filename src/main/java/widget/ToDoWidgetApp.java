package widget;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.autocomplete.*;

/**
 * Interface principal do widget To-Do List.
 */
public class ToDoWidgetApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JFileChooser fileChooser;

    public ToDoWidgetApp() {
        super("To-Do Widget");
        // setUndecorated(true); // Removido para exibir barra de navegação
        // setAlwaysOnTop(true); // Removido para exibir barra de navegação
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Editor multi-abas com syntax highlighting
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        fileChooser = new JFileChooser();
        addNewTab("Novo arquivo", "java");

        // Barra de menus estilo Notepad++
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        menuBar.setPreferredSize(new Dimension(0, 28));

        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem newItem = new JMenuItem("Nova aba");
        newItem.addActionListener(e -> addNewTab("Novo arquivo", "java"));
        JMenuItem openItem = new JMenuItem("Abrir...");
        openItem.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String ext = getExtension(file.getName());
                String syntax = getSyntaxStyle(ext);
                try {
                    String content = new String(java.nio.file.Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                    addNewTab(file.getName(), syntax, content);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JMenuItem saveItem = new JMenuItem("Salvar aba");
        saveItem.addActionListener(e -> saveCurrentTab());
        JMenuItem saveAsItem = new JMenuItem("Salvar aba como...");
        saveAsItem.addActionListener(e -> saveCurrentTabAs());
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Editar");
        JMenuItem cutItem = new JMenuItem("Cortar");
        cutItem.addActionListener(e -> getCurrentTextArea().cut());
        JMenuItem copyItem = new JMenuItem("Copiar");
        copyItem.addActionListener(e -> getCurrentTextArea().copy());
        JMenuItem pasteItem = new JMenuItem("Colar");
        pasteItem.addActionListener(e -> getCurrentTextArea().paste());
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Editor de texto estilo Notepad++\nFeito em Java\nPor Cabral567", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Painel principal para layout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Barra de atalhos (sem cor verde)
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        topBar.setBackground(Color.white);
        topBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JButton newBtn = new JButton("Nova aba");
        newBtn.setToolTipText("Nova aba");
        newBtn.setBackground(Color.white);
        newBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        newBtn.addActionListener(e -> addNewTab("Novo arquivo", "java"));
        topBar.add(newBtn);

        JButton openBtn = new JButton("Abrir");
        openBtn.setToolTipText("Abrir arquivo");
        openBtn.setBackground(Color.white);
        openBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        openBtn.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String ext = getExtension(file.getName());
                String syntax = getSyntaxStyle(ext);
                try {
                    String content = new String(java.nio.file.Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
                    addNewTab(file.getName(), syntax, content);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        topBar.add(openBtn);

        JButton saveBtn = new JButton("Salvar");
        saveBtn.setToolTipText("Salvar aba atual");
        saveBtn.setBackground(Color.white);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        saveBtn.addActionListener(e -> saveCurrentTab());
        topBar.add(saveBtn);

        JButton saveAsBtn = new JButton("Salvar como");
        saveAsBtn.setToolTipText("Salvar aba como...");
        saveAsBtn.setBackground(Color.white);
        saveAsBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        saveAsBtn.addActionListener(e -> saveCurrentTabAs());
        topBar.add(saveAsBtn);

        JButton cutBtn = new JButton("Cortar");
        cutBtn.setToolTipText("Cortar");
        cutBtn.setBackground(Color.white);
        cutBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        cutBtn.addActionListener(e -> getCurrentTextArea().cut());
        topBar.add(cutBtn);

        JButton copyBtn = new JButton("Copiar");
        copyBtn.setToolTipText("Copiar");
        copyBtn.setBackground(Color.white);
        copyBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        copyBtn.addActionListener(e -> getCurrentTextArea().copy());
        topBar.add(copyBtn);

        JButton pasteBtn = new JButton("Colar");
        pasteBtn.setToolTipText("Colar");
        pasteBtn.setBackground(Color.white);
        pasteBtn.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        pasteBtn.addActionListener(e -> getCurrentTextArea().paste());
        topBar.add(pasteBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Área de edição de texto multi-abas
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Barra inferior para seleção de linguagem
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.white);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JLabel langLabel = new JLabel("Linguagem:");
        bottomBar.add(langLabel, BorderLayout.WEST);

        String[] langs = {"Java", "Python", "C", "C++", "JavaScript", "HTML", "XML", "Texto"};
        JComboBox<String> langCombo = new JComboBox<>(langs);
        langCombo.setSelectedItem("Java");
        langCombo.setToolTipText("Escolha a linguagem para destaque de sintaxe");
        bottomBar.add(langCombo, BorderLayout.EAST);

        langCombo.addActionListener(e -> {
            RSyntaxTextArea area = getCurrentTextArea();
            String lang = (String) langCombo.getSelectedItem();
            String syntax = getSyntaxStyle(lang.toLowerCase());
            area.setSyntaxEditingStyle(syntax);
            // Atualiza autocompletar
            for (MouseWheelListener mwl : area.getMouseWheelListeners()) {
                area.removeMouseWheelListener(mwl);
            }
            CompletionProvider provider = createCompletionProvider(syntax);
            AutoCompletion ac = new AutoCompletion(provider);
            ac.install(area);
            // Reinstala zoom
            area.addMouseWheelListener(ev -> {
                if (ev.isControlDown()) {
                    int notches = ev.getWheelRotation();
                    Font f = area.getFont();
                    int newSize = Math.max(8, f.getSize() - notches);
                    area.setFont(f.deriveFont((float)newSize));
                    ev.consume();
                }
            });
        });

        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        // Sombra e cor de fundo
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(180,180,180), 1));
        mainPanel.setBackground(Color.white);
        setContentPane(mainPanel);
    }

    // Adiciona uma nova aba com linguagem
    private void addNewTab(String title, String syntax) {
        addNewTab(title, syntax, "");
    }

    private void addNewTab(String title, String syntax, String content) {
        RSyntaxTextArea area = new RSyntaxTextArea(30, 80);
        area.setSyntaxEditingStyle(syntax);
        area.setCodeFoldingEnabled(true);
        area.setAntiAliasingEnabled(true);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));
        area.setText(content);
        RTextScrollPane sp = new RTextScrollPane(area);
        sp.setLineNumbersEnabled(true);
        // Autocomplete básico
        CompletionProvider provider = createCompletionProvider(syntax);
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(area);

        // Painel customizado para aba com X vermelho
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabPanel.setOpaque(false);
        JLabel titleLabel = new JLabel(title + "  ");
        tabPanel.add(titleLabel);
        JButton closeBtn = new JButton("✖");
        closeBtn.setMargin(new Insets(0, 2, 0, 2));
        closeBtn.setBorder(BorderFactory.createEmptyBorder());
        closeBtn.setForeground(Color.RED);
        closeBtn.setOpaque(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setToolTipText("Fechar aba");
        closeBtn.setFont(closeBtn.getFont().deriveFont(Font.BOLD));
        closeBtn.addActionListener(e -> {
            int idx = tabbedPane.indexOfTabComponent(tabPanel);
            if (idx != -1) tabbedPane.remove(idx);
        });
        tabPanel.add(closeBtn);

        // Zoom de fonte com Ctrl + scroll
        area.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                int notches = e.getWheelRotation();
                Font f = area.getFont();
                int newSize = Math.max(8, f.getSize() - notches);
                area.setFont(f.deriveFont((float)newSize));
                e.consume();
            }
        });

        tabbedPane.addTab(title, sp);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabPanel);
        tabbedPane.setSelectedComponent(sp);
    }

    // Retorna a área de texto da aba atual
    private RSyntaxTextArea getCurrentTextArea() {
        RTextScrollPane sp = (RTextScrollPane) tabbedPane.getSelectedComponent();
        return (RSyntaxTextArea) sp.getTextArea();
    }

    // Salva aba atual
    private void saveCurrentTab() {
        RTextScrollPane sp = (RTextScrollPane) tabbedPane.getSelectedComponent();
        RSyntaxTextArea area = (RSyntaxTextArea) sp.getTextArea();
        String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        File file = null;
        if (title.equals("Novo arquivo")) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
            } else {
                return;
            }
        } else {
            file = new File(title);
        }
        try {
            java.nio.file.Files.write(file.toPath(), area.getText().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Salva aba atual como...
    private void saveCurrentTabAs() {
        RTextScrollPane sp = (RTextScrollPane) tabbedPane.getSelectedComponent();
        RSyntaxTextArea area = (RSyntaxTextArea) sp.getTextArea();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), file.getName());
            try {
                java.nio.file.Files.write(file.toPath(), area.getText().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Sugestões de autocompletar por linguagem
    private CompletionProvider createCompletionProvider(String syntax) {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        if (syntax.equals(SyntaxConstants.SYNTAX_STYLE_JAVA)) {
            provider.addCompletion(new BasicCompletion(provider, "public"));
            provider.addCompletion(new BasicCompletion(provider, "class"));
            provider.addCompletion(new BasicCompletion(provider, "static"));
            provider.addCompletion(new BasicCompletion(provider, "void"));
            provider.addCompletion(new BasicCompletion(provider, "main"));
        } else if (syntax.equals(SyntaxConstants.SYNTAX_STYLE_PYTHON)) {
            provider.addCompletion(new BasicCompletion(provider, "def"));
            provider.addCompletion(new BasicCompletion(provider, "import"));
            provider.addCompletion(new BasicCompletion(provider, "print"));
        } else {
            provider.addCompletion(new BasicCompletion(provider, "if"));
            provider.addCompletion(new BasicCompletion(provider, "else"));
        }
        return provider;
    }

    // Detecta extensão para linguagem
    private String getExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx > 0 && idx < filename.length() - 1) {
            return filename.substring(idx + 1).toLowerCase();
        }
        return "txt";
    }

    // Mapeia extensão para syntax highlighting
    private String getSyntaxStyle(String ext) {
        switch (ext) {
            case "java": return SyntaxConstants.SYNTAX_STYLE_JAVA;
            case "python": case "py": return SyntaxConstants.SYNTAX_STYLE_PYTHON;
            case "js": case "javascript": return SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
            case "html": return SyntaxConstants.SYNTAX_STYLE_HTML;
            case "xml": return SyntaxConstants.SYNTAX_STYLE_XML;
            case "c": return SyntaxConstants.SYNTAX_STYLE_C;
            case "cpp": case "c++": return SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
            case "txt": case "texto": default: return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }

    public static void main(String[] args) {
        try { FlatLightLaf.install(); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            ToDoWidgetApp app = new ToDoWidgetApp();
            app.setVisible(true);
        });
    }
}
