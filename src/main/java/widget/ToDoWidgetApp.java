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
        // setUndecorated(true); // Removido para exibir barra de navegação
        // setAlwaysOnTop(true); // Removido para exibir barra de navegação
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());  // Remove padding para ficar mais parecido com Notepad++

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

        JMenu infoMenu = new JMenu("Informações");
        JMenuItem infoItem = new JMenuItem("Sobre o programa");
        infoItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
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
                "• Terminal PowerShell integrado\n" +
                "• Detecção automática de linguagem\n\n" +
                "Desenvolvido para ser o editor mais leve e ágil,\n" +
                "sem sacrificar funcionalidades essenciais.\n\n" +
                "Autor: Cabral567 | Versão: 1.0 | Java 17+",
                "Coffee - Editor Super Leve", JOptionPane.INFORMATION_MESSAGE));
        infoMenu.add(infoItem);
        menuBar.add(infoMenu);

        JMenu helpMenu = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Editor de texto estilo Notepad++\nFeito em Java\nPor Cabral567", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

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
                "• Terminal PowerShell integrado\n" +
                "• Detecção automática de linguagem\n\n" +
                "Desenvolvido para ser o editor mais leve e ágil,\n" +
                "sem sacrificar funcionalidades essenciais.\n\n" +
                "Autor: Cabral567 | Versão: 1.0 | Java 17+",
                "Coffee - Editor Super Leve", JOptionPane.INFORMATION_MESSAGE));
        topBar.add(infoBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Configuração do terminal seguindo melhores práticas da documentação Java
        terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBackground(new Color(30, 30, 30));
        
        try {
            // Configurar ProcessBuilder seguindo a documentação oficial
            ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-NoExit");
            processBuilder.redirectErrorStream(true); // Merge stderr com stdout
            processBuilder.directory(new File(System.getProperty("user.dir"))); // Working directory
            
            // Configurar environment se necessário
            processBuilder.environment().put("TERM", "dumb"); // Terminal simples
            
            Process process = processBuilder.start();
            
            // Terminal display com JTextArea otimizado
            JTextArea terminalArea = new JTextArea(20, 80);
            terminalArea.setBackground(Color.BLACK);
            terminalArea.setForeground(Color.WHITE);
            terminalArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            terminalArea.setCaretColor(Color.WHITE);
            terminalArea.setEditable(false);
            terminalArea.setLineWrap(true);
            terminalArea.setWrapStyleWord(true);
            
            // Stream readers seguindo melhores práticas de I/O
            java.io.BufferedReader outputReader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream(), 
                java.nio.charset.StandardCharsets.UTF_8)
            );
            java.io.BufferedWriter inputWriter = new java.io.BufferedWriter(
                new java.io.OutputStreamWriter(process.getOutputStream(), 
                java.nio.charset.StandardCharsets.UTF_8)
            );
            
            // Controle de auto-scroll melhorado
            final boolean[] autoScroll = {true};
            
            // Thread para ler output do processo (seguindo pattern de documentação)
            Thread outputThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = outputReader.readLine()) != null) {
                        final String finalLine = line + "\n";
                        SwingUtilities.invokeLater(() -> {
                            terminalArea.append(finalLine);
                            
                            // Limitar tamanho do buffer para performance
                            if (terminalArea.getDocument().getLength() > 50000) {
                                try {
                                    terminalArea.getDocument().remove(0, 10000);
                                } catch (Exception ignored) {}
                            }
                            
                            // Auto-scroll inteligente
                            if (autoScroll[0]) {
                                terminalArea.setCaretPosition(terminalArea.getDocument().getLength());
                            }
                        });
                    }
                } catch (java.io.IOException ex) {
                    SwingUtilities.invokeLater(() -> {
                        terminalArea.append("Terminal desconectado: " + ex.getMessage() + "\n");
                    });
                }
            });
            outputThread.setDaemon(true);
            outputThread.start();
            
            // Campo de entrada otimizado
            JTextField inputField = new JTextField();
            inputField.setBackground(Color.BLACK);
            inputField.setForeground(Color.WHITE);
            inputField.setCaretColor(Color.WHITE);
            inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
            inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            // Action listener para enviar comandos
            inputField.addActionListener(e -> {
                String command = inputField.getText();
                if (!command.trim().isEmpty()) {
                    try {
                        inputWriter.write(command + "\r\n");
                        inputWriter.flush();
                        inputField.setText("");
                    } catch (java.io.IOException ex) {
                        SwingUtilities.invokeLater(() -> {
                            terminalArea.append("Erro ao enviar comando: " + ex.getMessage() + "\n");
                        });
                    }
                }
            });
            
            // ScrollPane com configurações otimizadas
            JScrollPane terminalScroll = new JScrollPane(terminalArea);
            terminalScroll.setBackground(Color.BLACK);
            terminalScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            terminalScroll.getViewport().setBackground(Color.BLACK);
            
            // Configurações de scroll seguindo melhores práticas
            terminalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            terminalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            terminalScroll.getVerticalScrollBar().setUnitIncrement(16);
            terminalScroll.getHorizontalScrollBar().setUnitIncrement(16);
            
            // Caret policy otimizada
            DefaultCaret caret = (DefaultCaret) terminalArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            
            // Detector de scroll manual melhorado
            terminalScroll.getVerticalScrollBar().addAdjustmentListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    JScrollBar scrollBar = terminalScroll.getVerticalScrollBar();
                    int extent = scrollBar.getModel().getExtent();
                    int maximum = scrollBar.getMaximum();
                    int value = scrollBar.getValue();
                    autoScroll[0] = (value + extent) >= (maximum - 50); // Margem de 50px
                }
            });
            
            // Container do terminal
            JPanel terminalContainer = new JPanel(new BorderLayout());
            terminalContainer.add(terminalScroll, BorderLayout.CENTER);
            terminalContainer.add(inputField, BorderLayout.SOUTH);
            
            terminalPanel.add(terminalContainer, BorderLayout.CENTER);
            
            // Cleanup quando a janela fechar
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    inputWriter.close();
                    outputReader.close();
                    process.destroyForcibly();
                } catch (Exception ignored) {}
            }));
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JTextArea errorArea = new JTextArea("Erro ao iniciar terminal: " + ex.getMessage());
            errorArea.setForeground(Color.RED);
            errorArea.setBackground(Color.BLACK);
            terminalPanel.add(errorArea, BorderLayout.CENTER);
        }
        
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
    private String getExtension(String filename) {
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
    private String getSyntaxStyle(String input) {
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
            app.setVisible(true);
        });
    }
}
