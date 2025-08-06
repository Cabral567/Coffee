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
                "Editor de texto avançado, multi-abas, com destaque de sintaxe, autocompletar, zoom, seleção de linguagem, temas claro/escuro.\n\nAutor: Cabral567\nVersão: 1.0\nJava 17+\nProjeto: Coffee",
                "Informações", JOptionPane.INFORMATION_MESSAGE));
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
                "Editor de texto avançado, multi-abas, com destaque de sintaxe, autocompletar, zoom, seleção de linguagem, temas claro/escuro.\n\nAutor: Cabral567\nVersão: 1.0\nJava 17+\nProjeto: ToDoWidget",
                "Informações", JOptionPane.INFORMATION_MESSAGE));
        topBar.add(infoBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Configuração do terminal
        terminalPanel = new JPanel(new BorderLayout());
        terminalPanel.setBackground(new Color(30, 30, 30));
        
        // Criar processo do terminal com PowerShell
        ProcessBuilder processBuilder = new ProcessBuilder(
            "powershell.exe",
            "-NoExit",
            "-Command",
            "$Host.UI.RawUI.WindowTitle = 'Windows PowerShell';" +
            "Write-Host 'Windows PowerShell';" +
            "Write-Host 'Copyright (C) Microsoft Corporation. Todos os direitos reservados.';" +
            "Write-Host '';" +
            "function prompt {" +
            "    $currentPath = $(Get-Location);" +
            "    'PS ' + $currentPath + '> '" +
            "}"
        );
        processBuilder.redirectErrorStream(true);
        
        try {
            Process process = processBuilder.start();
            // Terminal com suporte a cores ANSI
            JTextPane terminalArea = new JTextPane();
            terminalArea.setBackground(Color.BLACK);
            terminalArea.setForeground(Color.WHITE);
            terminalArea.setFont(new Font("Consolas", Font.PLAIN, 14));
            terminalArea.setCaretColor(Color.WHITE);
            terminalArea.setEditable(true);
            
            // Suporte a cores ANSI
            terminalArea.setEditorKit(new StyledEditorKit() {
                @Override
                public Document createDefaultDocument() {
                    return new DefaultStyledDocument() {
                        @Override
                        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                            if (str.contains("\u001B[")) {
                                // Processa sequências ANSI
                                StringBuilder processedText = new StringBuilder();
                                SimpleAttributeSet attrs = new SimpleAttributeSet();
                                
                                for (int i = 0; i < str.length(); i++) {
                                    if (str.charAt(i) == '\u001B' && i + 1 < str.length() && str.charAt(i + 1) == '[') {
                                        i += 2;
                                        StringBuilder code = new StringBuilder();
                                        while (i < str.length() && str.charAt(i) != 'm') {
                                            code.append(str.charAt(i++));
                                        }
                                        
                                        // Processa o código ANSI
                                        String[] codes = code.toString().split(";");
                                        for (String c : codes) {
                                            switch (c) {
                                                case "0": attrs = new SimpleAttributeSet(); break;
                                                case "30": StyleConstants.setForeground(attrs, Color.BLACK); break;
                                                case "31": StyleConstants.setForeground(attrs, Color.RED); break;
                                                case "32": StyleConstants.setForeground(attrs, Color.GREEN); break;
                                                case "33": StyleConstants.setForeground(attrs, Color.YELLOW); break;
                                                case "34": StyleConstants.setForeground(attrs, Color.BLUE); break;
                                                case "35": StyleConstants.setForeground(attrs, Color.MAGENTA); break;
                                                case "36": StyleConstants.setForeground(attrs, Color.CYAN); break;
                                                case "37": StyleConstants.setForeground(attrs, Color.WHITE); break;
                                            }
                                        }
                                    } else {
                                        processedText.append(str.charAt(i));
                                    }
                                }
                                
                                super.insertString(offs, processedText.toString(), attrs);
                            } else {
                                super.insertString(offs, str, a);
                            }
                        }
                    };
                }
            });
            
            // Redirecionar saída do processo para o JTextArea
            new Thread(() -> {
                try {
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                    );
                    int c;
                    while ((c = reader.read()) != -1) {
                        final char ch = (char)c;
                        SwingUtilities.invokeLater(() -> {
                            try {
                                Document doc = terminalArea.getDocument();
                                doc.insertString(doc.getLength(), String.valueOf(ch), null);
                                terminalArea.setCaretPosition(doc.getLength());
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
            
            // Entrada do usuário
            terminalArea.addKeyListener(new KeyAdapter() {
                private StringBuilder inputBuffer = new StringBuilder();
                
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String command = inputBuffer.toString() + "\n";
                        try {
                            process.getOutputStream().write(command.getBytes());
                            process.getOutputStream().flush();
                            inputBuffer.setLength(0);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && inputBuffer.length() > 0) {
                        inputBuffer.setLength(inputBuffer.length() - 1);
                    } else if (!e.isControlDown() && !e.isAltDown() && !e.isMetaDown()) {
                        char c = e.getKeyChar();
                        if (c != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(c)) {
                            inputBuffer.append(c);
                        }
                    }
                }
            });
            
            JScrollPane terminalScroll = new JScrollPane(terminalArea);
            terminalScroll.setBackground(new Color(1, 36, 86));
            terminalScroll.setBorder(null);
            terminalScroll.getViewport().setBackground(new Color(1, 36, 86));
            terminalPanel.add(terminalScroll, BorderLayout.CENTER);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JTextArea errorArea = new JTextArea("Erro ao iniciar terminal: " + ex.getMessage());
            errorArea.setForeground(Color.RED);
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
        
        // Adiciona atalho Ctrl+S para salvar
        area.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "save");
        area.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentTab();
            }
        });
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
        // Zoom de fonte com Ctrl + scroll
        area.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                int notches = e.getWheelRotation();
                Font f = area.getFont();
                int newSize = Math.max(8, f.getSize() - notches);
                float newSize_f = (float)newSize;
                
                // Atualiza fonte da área de texto
                area.setFont(f.deriveFont(newSize_f));
                
                // Atualiza fonte da numeração de linhas
                RTextScrollPane scrollPane = (RTextScrollPane)area.getParent().getParent();
                Font gutterFont = new Font(f.getFamily(), Font.PLAIN, newSize);
                scrollPane.getGutter().setLineNumberFont(gutterFont);
                
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

    // Alternar visibilidade do terminal
    private void toggleTerminal() {
        isTerminalVisible = !isTerminalVisible;
        terminalPanel.setVisible(isTerminalVisible);
        splitPane.setDividerLocation(0.7); // Mantém a proporção ao mostrar/ocultar
        revalidate();
        repaint();
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
