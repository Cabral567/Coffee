# Coffee Editor

**Editor de Texto Super Leve e Rápido** - Interface minimalista, performance otimizada e recursos essenciais para programadores.

Editor multi-abas ultrarrápido com terminal integrado, desenvolvido para ser o mais leve e eficiente possível, sem sacrificar funcionalidades importantes. Feito em Java 17+ com Swing, FlatLaf e RSyntaxTextArea.

## Como usar

### Execução direta
1. Certifique-se de ter o Java 17+ instalado
2. Execute o comando:
   ```shell
   java -jar target/ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

### Build a partir do código fonte
1. Clone o repositório:
   ```shell
   git clone https://github.com/Cabral567/Coffee.git
   cd Coffee
   ```
2. Compile e gere o JAR com dependências:
   ```shell
   mvn clean package
   ```
3. Execute o programa:
   ```shell
   java -jar target/ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## Características Principais

**FOCADO EM PERFORMANCE:**
- **Ultrarrápido e responsivo** - Inicialização instantânea
- **Super leve** - Baixo consumo de memória e CPU
- **Interface minimalista** - Design otimizado para eficiência
- **Zero bloat** - Apenas recursos essenciais, sem desperdício

## Principais Funcionalidades
- **Múltiplas abas** (multi-tab) - Gerenciamento eficiente de documentos
- **Terminal PowerShell integrado** - Controle completo do sistema
  - Suporte a cores ANSI
  - Visibilidade alternável
  - Integração otimizada com o editor
- **Syntax highlighting** para 8+ linguagens:
  - Java, Python, C/C++, JavaScript, HTML, XML, Texto
- **Autocompletar inteligente** por linguagem
- **Numeração de linhas** e folding de código
- **Zoom dinâmico** com Ctrl + scroll do mouse
- **Detecção automática de linguagem** baseada na extensão do arquivo
- **Atalhos otimizados** (Ctrl+S para salvar, cortar, copiar, colar)
- **Operações de arquivo rápidas** (salvar, abrir, criar)
- **Fechar abas** com botão X intuitivo
- **Visual moderno e responsivo** - Interface limpa e profissional
## Por que escolher o Coffee?

**Coffee foi desenvolvido com foco em PERFORMANCE e LEVEZA**, oferecendo:

- **Inicialização instantânea** - Abra o editor em segundos
- **Consumo mínimo de recursos** - Funciona bem até em máquinas mais antigas
- **Interface responsiva** - Sem travamentos ou delays
- **Funcionalidades essenciais** - Tudo que você precisa, nada que você não precisa
- **Otimizado para programadores** - Syntax highlighting e autocompletar eficientes

*Ideal para quem busca um editor rápido, leve e funcional para programação diária.*

## Como usar

## Desenvolvimento

### Requisitos
- Java Development Kit (JDK) 17 ou superior
- Maven

### Estrutura do projeto
- `src/main/java/widget/ToDoWidgetApp.java`: Classe principal do editor

### Compilação
```shell
mvn clean compile
```

### Execução em modo de desenvolvimento
```shell
mvn exec:java -Dexec.mainClass="widget.ToDoWidgetApp"
```

## Interface do Usuário
- Barra de menus:
  - Arquivo (Nova aba, Abrir, Salvar, Salvar como)
  - Editar (Cortar, Copiar, Colar)
  - Informações
  - Ajuda
- Barra de ferramentas com ações rápidas
- Terminal PowerShell integrado
- Barra inferior com seleção de linguagem

## Estrutura do projeto
- `ToDoWidgetApp.java`: interface principal do editor com terminal integrado

## Dependências

**Escolhidas por serem leves e eficientes:**
- **Java 17+** - Plataforma robusta e otimizada
- **[FlatLaf](https://www.formdev.com/flatlaf/)** - Tema moderno e leve
- **[RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea)** - Syntax highlighting otimizado
- **[AutoComplete](https://github.com/bobbylight/AutoComplete)** - Autocompletar eficiente

## Autor
**Cabral567** - *Desenvolvedor focado em criar ferramentas leves e eficientes*

## Versão
**1.0** - *Editor Super Leve e Rápido*

---

*"Coffee: O editor que não pesa na sua máquina, mas acelera seu trabalho!"*
