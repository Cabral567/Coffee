# Coffee Editor

<p align="center">
  <img src="assets/profile.png" alt="Coffee Editor Banner" width="100%">
</p>

<p align="center">
  <strong>Super Leve • Ultrarrápido • Minimalista</strong>
</p>

Editor multi-abas ultrarrápido com terminal integrado, desenvolvido para ser o mais leve e eficiente possível. Feito em Java 17+ com Swing, FlatLaf e RSyntaxTextArea.

## Instalação

### Windows - Instalador Automático (Recomendado)
1. **[Baixe o instalador setup.exe](https://github.com/Cabral567/ToDoWidget/releases/latest)**
2. Execute como administrador
3. Siga o assistente de instalação
4. Coffee será configurado automaticamente com "Abrir com"

### Linux - Instalador Automático
**Pré-requisitos:** Java 17+ e Maven instalados

1. Clone o repositório:
   ```bash
   git clone https://github.com/Cabral567/Coffee.git
   cd Coffee
   ```
2. Execute o instalador:
   ```bash
   bash ../install.sh
   ```
3. O programa será instalado nos diretórios padrão Linux:
   - `/opt/coffee` (aplicação)
   - `/usr/local/bin/coffee` (atalho de execução)
   - `/usr/share/applications/coffee.desktop` (menu de aplicativos)
   - `/usr/share/icons/hicolor/256x256/apps/coffee.png` (ícone)
   - `~/.local/share/coffee` (dados do usuário)
   - `~/.config/coffee` (configurações do usuário)
4. Para executar:
   ```bash
   coffee
   ```
5. Para abrir um arquivo diretamente:
   ```bash
   coffee caminho/para/arquivo.txt
   ```

**Desinstalação:**
Após a instalação, para remover completamente o Coffee do sistema, execute:
```bash
coffee-remove
```

### Execução Manual (JAR)
**Pré-requisito:** Java 17+ instalado
```shell
java -jar Coffee-1.0.jar
```

**Para "abrir com":**
```shell
java -jar Coffee-1.0.jar "caminho/para/arquivo.txt"
```

## Principais Funcionalidades

### Performance e Leveza
- **Inicialização instantânea** - Abra em segundos
- **Baixo consumo** - Funciona em máquinas antigas
- **Interface responsiva** - Zero travamentos
- **Design minimalista** - Apenas o essencial

### Recursos para Programação
- **Múltiplas abas** - Gerenciamento eficiente de documentos
- **Syntax highlighting** - Java, Python, C/C++, JavaScript, HTML, XML
- **Autocompletar inteligente** por linguagem
- **Detecção automática** de linguagem por extensão
- **Zoom dinâmico** - Ctrl + scroll do mouse
- **Numeração de linhas** e folding de código

### Terminal Integrado
- **PowerShell nativo** - Controle completo do sistema
- **Suporte ANSI** - Cores e formatação
- **Visibilidade alternável** - Mostrar/ocultar conforme necessário

## Build a partir do código fonte

### Requisitos
- JDK 17+, Maven 3.6+, Git

### Compilação
```shell
git clone https://github.com/Cabral567/ToDoWidget.git
cd ToDoWidget
mvn clean package
java -jar target/ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Dependências
- **Java 17+** - Plataforma robusta
- **[FlatLaf](https://www.formdev.com/flatlaf/)** - Tema moderno
- **[RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea)** - Syntax highlighting
- **[AutoComplete](https://github.com/bobbylight/AutoComplete)** - Autocompletar

## Autor
<div align="center">
  <img src="./assets/profile.png" alt="Cabral567" width="150" style="border-radius: 50%;">
  <br>
  <strong>Cabral567</strong>
  <br>
  <em>Desenvolvedor focado em ferramentas leves e eficientes</em>
</div>

---

*"Coffee: O editor que não pesa na sua máquina, mas acelera seu trabalho!"*
