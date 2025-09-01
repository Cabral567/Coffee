package widget;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.autocomplete.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.util.Stack;

/**
 * Interface principal do widget To-Do List.
 */
public class ToDoWidgetApp extends JFrame {
    private JTabbedPane tabbedPane;
    private JFileChooser fileChooser;
    private JPanel bottomBar;  // Referência para a barra inferior
    private JSplitPane splitPane; // Divisor entre editor e terminal
    private JPanel terminalPanel; // Painel do terminal
    private boolean isTerminalVisible = false; // Estado de visibilidade do terminal
    
    // Componentes do terminal
    private JTabbedPane terminalTabbedPane;
    private List<TerminalTab> terminalTabs;
    private TerminalTab currentTerminalTab;
    
    // Estiliza um botão da barra de ferramentas
    private void styleToolbarButton(JButton btn) {
        btn.setBackground(new Color(240, 240, 240));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(220, 220, 220));
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(140, 140, 140)),
                        BorderFactory.createEmptyBorder(3,7,3,7)
                    ));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(240, 240, 240));
                btn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
            }
        });
    }

    public ToDoWidgetApp() {
    super("Coffee");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

        // Editor multi-abas com syntax highlighting
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        fileChooser = new JFileChooser();
        addNewTab("Novo arquivo", "java");



        // Painel principal para layout vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Barra de atalhos estilo Notepad++
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        topBar.setBackground(new Color(240, 240, 240));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(160, 160, 160)));

        JButton newBtn = new JButton("Nova aba");
        newBtn.setToolTipText("Nova aba");
        newBtn.setFocusPainted(false);
        newBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(newBtn);
        newBtn.addActionListener(e -> addNewTab("Novo arquivo", "java"));
        topBar.add(newBtn);

        JButton openBtn = new JButton("Abrir arquivo");
        openBtn.setToolTipText("Abrir arquivo");
        openBtn.setFocusPainted(false);
        openBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(openBtn);
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
        saveBtn.setToolTipText("Salvar");
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(saveBtn);
        saveBtn.addActionListener(e -> saveCurrentTab());
        topBar.add(saveBtn);

        JButton saveAsBtn = new JButton("Salvar como");
        saveAsBtn.setToolTipText("Salvar como");
        saveAsBtn.setFocusPainted(false);
        saveAsBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(saveAsBtn);
        saveAsBtn.addActionListener(e -> saveCurrentTabAs());
        topBar.add(saveAsBtn);

        JButton cutBtn = new JButton("Cortar");
        cutBtn.setToolTipText("Cortar");
        cutBtn.setFocusPainted(false);
        cutBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(cutBtn);
        cutBtn.addActionListener(e -> getCurrentTextArea().cut());
        topBar.add(cutBtn);

        JButton copyBtn = new JButton("Copiar");
        copyBtn.setToolTipText("Copiar");
        copyBtn.setFocusPainted(false);
        copyBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(copyBtn);
        copyBtn.addActionListener(e -> getCurrentTextArea().copy());
        topBar.add(copyBtn);

        JButton pasteBtn = new JButton("Colar");
        pasteBtn.setToolTipText("Colar");
        pasteBtn.setFocusPainted(false);
        pasteBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(pasteBtn);
        pasteBtn.addActionListener(e -> getCurrentTextArea().paste());
        topBar.add(pasteBtn);

        // Botão Terminal
        JButton terminalBtn = new JButton("Terminal");
        terminalBtn.setToolTipText("Mostrar/Ocultar Terminal");
        terminalBtn.setFocusPainted(false);
        terminalBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(terminalBtn);
        terminalBtn.addActionListener(e -> toggleTerminal());
        topBar.add(terminalBtn);

        // Botão Informações
        JButton infoBtn = new JButton("Informações");
        infoBtn.setToolTipText("Sobre o programa");
        infoBtn.setFocusPainted(false);
        infoBtn.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        styleToolbarButton(infoBtn);
        infoBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Coffee - Editor de Texto Super Leve e Rápido\n\n" +
                "CARACTERÍSTICAS PRINCIPAIS:\n" +
                "• Editor ultrarrápido e responsivo\n" +
                "• Interface minimalista e eficiente\n" +
                "• Baixo consumo de memória e CPU\n\n" +
                "RECURSOS:\n" +
                "• Syntax highlighting para 8+ linguagens\n" +
                "• Sistema de abas multi-documento\n" +
                "• Autocompletar inteligente\n" +
                "• Zoom dinâmico (Ctrl + Scroll)\n" +
                "• Terminal integrado (PowerShell no Windows, Bash no Linux)\n" +
                "• Detecção automática de linguagem\n\n" +
                "Desenvolvido para ser o editor mais leve e ágil,\n" +
                "sem sacrificar funcionalidades essenciais.\n\n" +
                "Autor: Cabral567 | Versão: 1.0 | Java 17+",
                "Coffee - Editor Super Leve", JOptionPane.INFORMATION_MESSAGE));
        topBar.add(infoBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Inicializar terminal com implementação melhorada estilo VSCode
        initializeTerminal();

        // Configurar split pane
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, terminalPanel);
        splitPane.setResizeWeight(0.7); // 70% para o editor, 30% para o terminal
        splitPane.setDividerSize(5);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        terminalPanel.setVisible(false);

        // Barra inferior para seleção de linguagem
        bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(Color.white);
        bottomBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JLabel langLabel = new JLabel("Linguagem:");
        bottomBar.add(langLabel, BorderLayout.WEST);

        String[] langs = {"Java", "Python", "C", "C++", "JavaScript", "HTML", "XML", "Texto"};
        JComboBox<String> langCombo = new JComboBox<>(langs);
        langCombo.setSelectedItem("Java");
        langCombo.setToolTipText("Escolha a linguagem para destaque de sintaxe");
        bottomBar.add(langCombo, BorderLayout.EAST);

        // Armazenar referência do combo para uso posterior
        final JComboBox<String> finalLangCombo = langCombo;

        langCombo.addActionListener(e -> {
            RSyntaxTextArea area = getCurrentTextArea();
            String lang = (String) langCombo.getSelectedItem();
            String syntax = getSyntaxStyle(lang.toLowerCase());
            area.setSyntaxEditingStyle(syntax);
            
            // Atualiza autocompletar para nova linguagem
            CompletionProvider provider = createCompletionProvider(syntax);
            AutoCompletion ac = new AutoCompletion(provider);
            ac.install(area);
        });

        // Listener para detectar mudança de aba e atualizar linguagem automaticamente
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() >= 0) {
                String tabTitle = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
                String detectedLang = detectLanguageFromTitle(tabTitle);
                finalLangCombo.setSelectedItem(detectedLang);
            }
        });

        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        // Sombra e cor de fundo
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(180,180,180), 1));
        mainPanel.setBackground(Color.white);
        setContentPane(mainPanel);
        
        // Cleanup ao fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdownTerminal();
            }
        });
    }

    /**
     * Classe interna para representar uma aba do terminal
     */
    private static class TerminalTab {
        private JTextPane textPane;
        private JTextField inputField;
        private Process process;
        private BufferedWriter processInput;
        private BufferedReader processOutput;
        private ExecutorService executor;
        private AtomicBoolean running;
        private StyledDocument document;
        private List<String> commandHistory;
        private int historyIndex;
        private String currentPrompt;
        
        public TerminalTab() {
            this.commandHistory = new ArrayList<>();
            this.historyIndex = -1;
            this.currentPrompt = "$ ";
            this.executor = Executors.newCachedThreadPool();
            this.running = new AtomicBoolean(false);
            initializeComponents();
        }
        
        private void initializeComponents() {
            // TextPane para output
            textPane = new JTextPane();
            document = textPane.getStyledDocument();
            
            // Configurar aparência
            textPane.setBackground(new Color(30, 30, 30));
            textPane.setForeground(Color.WHITE);
            textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
            textPane.setCaretColor(Color.WHITE);
            textPane.setEditable(false);
            
            // Campo de entrada
            inputField = new JTextField();
            inputField.setBackground(new Color(30, 30, 30));
            inputField.setForeground(Color.WHITE);
            inputField.setCaretColor(Color.WHITE);
            inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
            inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            // Configurar atalhos de teclado
            setupKeyboardShortcuts();
            
            // Configurar menu de contexto
            setupContextMenu();
        }
        
        private void setupKeyboardShortcuts() {
            // Histórico de comandos com setas
            inputField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        e.consume();
                        navigateHistory(-1);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        e.consume();
                        navigateHistory(1);
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        e.consume();
                        executeCommand();
                    } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        e.consume();
                        // Auto-complete de comandos (implementar depois)
                    }
                }
            });
        }
        
        private void setupContextMenu() {
            JPopupMenu contextMenu = new JPopupMenu();
            
            JMenuItem copyItem = new JMenuItem("Copiar");
            copyItem.addActionListener(e -> copySelectedText());
            contextMenu.add(copyItem);
            
            JMenuItem pasteItem = new JMenuItem("Colar");
            pasteItem.addActionListener(e -> pasteText());
            contextMenu.add(pasteItem);
            
            JMenuItem clearItem = new JMenuItem("Limpar");
            clearItem.addActionListener(e -> clearTerminal());
            contextMenu.add(clearItem);
            
            JMenuItem newTabItem = new JMenuItem("Nova aba");
            newTabItem.addActionListener(e -> createNewTerminalTab());
            contextMenu.add(newTabItem);
            
            // Adicionar menu de contexto ao textPane
            textPane.setComponentPopupMenu(contextMenu);
            inputField.setComponentPopupMenu(contextMenu);
        }
        
        private void navigateHistory(int direction) {
            if (commandHistory.isEmpty()) return;
            
            if (direction < 0 && historyIndex < commandHistory.size() - 1) {
                historyIndex++;
            } else if (direction > 0 && historyIndex > 0) {
                historyIndex--;
            } else if (direction > 0 && historyIndex == 0) {
                historyIndex = -1;
                inputField.setText("");
                return;
            }
            
            if (historyIndex >= 0 && historyIndex < commandHistory.size()) {
                inputField.setText(commandHistory.get(historyIndex));
                inputField.setCaretPosition(inputField.getText().length());
            }
        }
        
        private void executeCommand() {
            String command = inputField.getText().trim();
            if (!command.isEmpty() && running.get()) {
                try {
                    // Adicionar ao histórico
                    commandHistory.add(command);
                    historyIndex = -1;
                    
                    // Mostrar comando no terminal
                    appendText(command + "\n", Color.YELLOW);
                    
                    // Enviar para o processo
                    processInput.write(command);
                    processInput.newLine();
                    processInput.flush();
                    
                    // Limpar campo de entrada
                    inputField.setText("");
                    
                } catch (IOException e) {
                    appendText("Erro ao enviar comando: " + e.getMessage() + "\n", Color.RED);
                }
            }
        }
        
        private void copySelectedText() {
            textPane.copy();
        }
        
        private void pasteText() {
            inputField.paste();
        }
        
        private void clearTerminal() {
            try {
                document.remove(0, document.getLength());
                appendText("Terminal limpo.\n", Color.CYAN);
                appendText(currentPrompt, Color.WHITE);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        
        private void createNewTerminalTab() {
            // Implementar criação de nova aba
            appendText("Funcionalidade de nova aba será implementada.\n", Color.CYAN);
        }
        
        public void appendText(String text, Color color) {
            try {
                javax.swing.text.Style style = document.addStyle("terminal", null);
                StyleConstants.setForeground(style, color);
                
                int length = document.getLength();
                document.insertString(length, text, style);
                
                // Limitar tamanho do documento
                if (document.getLength() > 50000) {
                    document.remove(0, 10000);
                }
                
                // Auto-scroll
                textPane.setCaretPosition(document.getLength());
                
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        
        public void startProcess() {
            try {
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder processBuilder;
                
                if (os.contains("win")) {
                    processBuilder = new ProcessBuilder("powershell.exe", "-NoExit", "-Command", "-");
                } else {
                    processBuilder = new ProcessBuilder("bash");
                }
                
                processBuilder.redirectErrorStream(true);
                processBuilder.directory(new File(System.getProperty("user.dir")));
                
                if (!os.contains("win")) {
                    processBuilder.environment().put("TERM", "dumb");
                    processBuilder.environment().put("PS1", "$ ");
                }
                
                process = processBuilder.start();
                processInput = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8));
                processOutput = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                
                running.set(true);
                
                // Mensagem inicial
                appendText("Terminal iniciado - " + (os.contains("win") ? "PowerShell" : "Bash") + "\n", Color.GREEN);
                appendText("Diretório: " + System.getProperty("user.dir") + "\n", Color.CYAN);
                appendText(currentPrompt, Color.WHITE);
                
                // Thread de leitura
                executor.submit(this::readProcessOutput);
                
            } catch (Exception e) {
                appendText("Erro ao iniciar terminal: " + e.getMessage() + "\n", Color.RED);
                e.printStackTrace();
            }
        }
        
        private void readProcessOutput() {
            try {
                char[] buffer = new char[1024];
                int bytesRead;
                
                while (running.get() && (bytesRead = processOutput.read(buffer)) != -1) {
                    final String output = new String(buffer, 0, bytesRead);
                    SwingUtilities.invokeLater(() -> {
                        String cleanOutput = filterAnsiCodes(output);
                        appendText(cleanOutput, Color.WHITE);
                    });
                }
            } catch (IOException e) {
                if (running.get()) {
                    SwingUtilities.invokeLater(() -> {
                        appendText("Terminal desconectado: " + e.getMessage() + "\n", Color.RED);
                    });
                }
            }
        }
        
        private String filterAnsiCodes(String text) {
            return text.replaceAll("\\x1B\\[[0-9;]*[a-zA-Z]", "")
                       .replaceAll("\\x1B\\][0-9;]*;.*?\\x07", "")
                       .replaceAll("\\x1B\\][0-9;]*;.*?\\x1B\\\\", "");
        }
        
        public void shutdown() {
            running.set(false);
            
            if (executor != null) {
                executor.shutdown();
            }
            
            if (processInput != null) {
                try {
                    processInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (processOutput != null) {
                try {
                    processOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            if (process != null) {
                process.destroyForcibly();
            }
        }
        
        public JTextPane getTextPane() { return textPane; }
        public JTextField getInputField() { return inputField; }
        public boolean isRunning() { return running.get(); }
    }

    /**
     * Inicializa o terminal com implementação robusta estilo VSCode
     */
    private void initializeTerminal() {
        terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBackground(new Color(30, 30, 30));
        
        // Inicializar sistema de abas do terminal
        terminalTabs = new ArrayList<>();
        terminalTabbedPane = new JTabbedPane();
        terminalTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        terminalTabbedPane.setBackground(new Color(30, 30, 30));
        terminalTabbedPane.setForeground(Color.WHITE);
        
        // Criar primeira aba do terminal
        createNewTerminalTab();
        
        // Adicionar botões de controle
        JPanel terminalControlPanel = createTerminalControlPanel();
        
        terminalPanel.add(terminalControlPanel, BorderLayout.NORTH);
        terminalPanel.add(terminalTabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createTerminalControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        controlPanel.setBackground(new Color(45, 45, 45));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        
        // Botão nova aba
        JButton newTabBtn = new JButton("+");
        newTabBtn.setToolTipText("Nova aba do terminal");
        newTabBtn.setPreferredSize(new Dimension(25, 20));
        newTabBtn.setBackground(new Color(60, 60, 60));
        newTabBtn.setForeground(Color.WHITE);
        newTabBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        newTabBtn.addActionListener(e -> createNewTerminalTab());
        controlPanel.add(newTabBtn);
        
        // Botão fechar aba
        JButton closeTabBtn = new JButton("×");
        closeTabBtn.setToolTipText("Fechar aba atual");
        closeTabBtn.setPreferredSize(new Dimension(25, 20));
        closeTabBtn.setBackground(new Color(60, 60, 60));
        closeTabBtn.setForeground(Color.WHITE);
        closeTabBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        closeTabBtn.addActionListener(e -> closeCurrentTerminalTab());
        controlPanel.add(closeTabBtn);
        
        // Botão limpar
        JButton clearBtn = new JButton("Limpar");
        clearBtn.setToolTipText("Limpar terminal atual");
        clearBtn.setPreferredSize(new Dimension(60, 20));
        clearBtn.setBackground(new Color(60, 60, 60));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        clearBtn.addActionListener(e -> clearCurrentTerminal());
        controlPanel.add(clearBtn);
        
        return controlPanel;
    }
    
    private void createNewTerminalTab() {
        TerminalTab newTab = new TerminalTab();
        terminalTabs.add(newTab);
        
        // Criar painel da aba
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBackground(new Color(30, 30, 30));
        
        // Adicionar componentes
        JScrollPane scrollPane = new JScrollPane(newTab.getTextPane());
        scrollPane.setBackground(new Color(30, 30, 30));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        tabPanel.add(scrollPane, BorderLayout.CENTER);
        tabPanel.add(newTab.getInputField(), BorderLayout.SOUTH);
        
        // Adicionar aba
        String tabTitle = "Terminal " + terminalTabs.size();
        terminalTabbedPane.addTab(tabTitle, tabPanel);
        terminalTabbedPane.setSelectedComponent(tabPanel);
        
        // Definir como aba atual
        currentTerminalTab = newTab;
        
        // Iniciar processo
        newTab.startProcess();
        
        // Configurar listener para mudança de aba
        terminalTabbedPane.addChangeListener(e -> {
            int selectedIndex = terminalTabbedPane.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < terminalTabs.size()) {
                currentTerminalTab = terminalTabs.get(selectedIndex);
            }
        });
    }
    
    private void closeCurrentTerminalTab() {
        if (currentTerminalTab != null && terminalTabs.size() > 1) {
            int currentIndex = terminalTabbedPane.getSelectedIndex();
            if (currentIndex >= 0) {
                // Desligar terminal
                currentTerminalTab.shutdown();
                
                // Remover da lista
                terminalTabs.remove(currentIndex);
                
                // Remover aba
                terminalTabbedPane.removeTabAt(currentIndex);
                
                // Atualizar aba atual
                if (!terminalTabs.isEmpty()) {
                    currentTerminalTab = terminalTabs.get(Math.min(currentIndex, terminalTabs.size() - 1));
                    terminalTabbedPane.setSelectedIndex(Math.min(currentIndex, terminalTabs.size() - 1));
                }
            }
        }
    }
    
    private void clearCurrentTerminal() {
        if (currentTerminalTab != null) {
            currentTerminalTab.clearTerminal();
        }
    }
    
    /**
     * Desliga o terminal e limpa recursos
     */
    private void shutdownTerminal() {
        if (terminalTabs != null) {
            for (TerminalTab tab : terminalTabs) {
                tab.shutdown();
            }
            terminalTabs.clear();
        }
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
        
        // Adiciona atalho Ctrl+S para salvar
        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "save");
        area.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTab();
            }
        });
        
        // Cria RTextScrollPane seguindo exemplo oficial da documentação
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

        // Configuração de zoom com Ctrl + scroll seguindo as melhores práticas
        // O RTextScrollPane já tem scroll normal, só precisamos adicionar zoom
        sp.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                // Implementa zoom com Ctrl + scroll
                int rotation = e.getWheelRotation();
                Font currentFont = area.getFont();
                int currentSize = currentFont.getSize();
                int newSize = Math.max(8, Math.min(72, currentSize - rotation));
                
                if (newSize != currentSize) {
                    Font newFont = currentFont.deriveFont((float)newSize);
                    area.setFont(newFont);
                    
                    // Atualiza fonte dos números de linha
                    Font gutterFont = new Font(newFont.getFamily(), Font.PLAIN, newSize);
                    sp.getGutter().setLineNumberFont(gutterFont);
                }
                e.consume(); // Consome evento apenas no zoom
            }
            // Scroll normal é tratado automaticamente pelo RTextScrollPane
        });
        
        tabbedPane.addTab(title, sp);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabPanel);
        tabbedPane.setSelectedComponent(sp);
        
        // Atualizar combo de linguagem automaticamente após adicionar a aba
        SwingUtilities.invokeLater(() -> {
            // Trigger do listener de mudança de aba para atualizar a linguagem
            if (tabbedPane.getChangeListeners().length > 0) {
                for (javax.swing.event.ChangeListener listener : tabbedPane.getChangeListeners()) {
                    listener.stateChanged(new javax.swing.event.ChangeEvent(tabbedPane));
                }
            }
        });
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
    public String getExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx > 0 && idx < filename.length() - 1) {
            return filename.substring(idx + 1).toLowerCase();
        }
        return "txt";
    }

    // Alternar visibilidade do terminal
    private void toggleTerminal() {
        isTerminalVisible = !isTerminalVisible;
        terminalPanel.setVisible(isTerminalVisible);
        splitPane.setDividerLocation(0.7); // Mantém a proporção ao mostrar/ocultar
        revalidate();
        repaint();
    }

    // Mapeia extensão OU nome da linguagem para syntax highlighting
    public String getSyntaxStyle(String input) {
        String normalized = input.toLowerCase();
        switch (normalized) {
            // Por extensão de arquivo
            case "java": return SyntaxConstants.SYNTAX_STYLE_JAVA;
            case "python": case "py": return SyntaxConstants.SYNTAX_STYLE_PYTHON;
            case "js": case "javascript": return SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
            case "html": case "htm": return SyntaxConstants.SYNTAX_STYLE_HTML;
            case "xml": case "xsl": case "xsd": return SyntaxConstants.SYNTAX_STYLE_XML;
            case "c": return SyntaxConstants.SYNTAX_STYLE_C;
            case "cpp": case "c++": case "cc": case "cxx": return SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
            // Por nome da linguagem (do combo)
            case "texto": case "text": default: return SyntaxConstants.SYNTAX_STYLE_NONE;
        }
    }

    // Detecta linguagem baseada no título/nome do arquivo para atualizar combo automaticamente
    private String detectLanguageFromTitle(String title) {
        if (title.equals("Novo arquivo")) {
            return "Java"; // Padrão para arquivos novos
        }
        
        String ext = getExtension(title).toLowerCase();
        switch (ext) {
            case "java": return "Java";
            case "py": case "python": return "Python";
            case "c": return "C";
            case "cpp": case "cc": case "cxx": return "C++";
            case "js": case "mjs": return "JavaScript";
            case "html": case "htm": return "HTML";
            case "xml": case "xsl": case "xsd": return "XML";
            case "txt": case "text": default: return "Texto";
        }
    }

    public static void main(String[] args) {
        try { FlatLightLaf.install(); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            ToDoWidgetApp app = new ToDoWidgetApp();
            
            // Se foi passado um arquivo como argumento (abrir com)
            if (args.length > 0) {
                String filePath = args[0];
                File file = new File(filePath);
                if (file.exists() && file.isFile()) {
                    try {
                        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()), 
                                                  java.nio.charset.StandardCharsets.UTF_8);
                        String ext = app.getExtension(file.getName());
                        String syntax = app.getSyntaxStyle(ext);
                        
                        // Remove a aba padrão "Novo arquivo" se estiver vazia
                        if (app.tabbedPane.getTabCount() == 1) {
                            RSyntaxTextArea defaultArea = app.getCurrentTextArea();
                            if (defaultArea.getText().trim().isEmpty()) {
                                app.tabbedPane.removeTabAt(0);
                            }
                        }
                        
                        // Adiciona nova aba com o arquivo
                        app.addNewTab(file.getName(), syntax, content);
                    } catch (Exception ex) {
                        // Se houver erro ao ler arquivo, mostra mensagem mas ainda abre o editor
                        JOptionPane.showMessageDialog(app, 
                            "Erro ao abrir arquivo: " + ex.getMessage(), 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
            app.setVisible(true);
        });
    }
}
