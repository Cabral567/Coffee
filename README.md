# Coffee Editor

Editor de texto multi-abas com terminal integrado, leve e versátil para Windows, feito em Java 17+ com Swing, FlatLaf e RSyntaxTextArea.

[![GitHub Release](https://img.shields.io/github/v/release/Cabral567/Coffee?include_prereleases)](https://github.com/Cabral567/Coffee/releases)
[![License](https://img.shields.io/github/license/Cabral567/Coffee)](LICENSE)

## Como usar

### Execução direta
1. Baixe o arquivo JAR da [página de releases](https://github.com/Cabral567/Coffee/releases)
2. Certifique-se de ter o Java 17+ instalado
3. Execute o comando:
   ```shell
   java -jar coffee-1.0-jar-with-dependencies.jar
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
   java -jar target/coffee-1.0-jar-with-dependencies.jar
   ```

## Principais Funcionalidades
- Múltiplas abas (multi-tab)
- Terminal integrado com PowerShell
  - Suporte a cores ANSI
  - Visibilidade alternável
  - Integração completa com o editor
- Destaque de sintaxe para várias linguagens:
  - Java
  - Python
  - C/C++
  - JavaScript
  - HTML
  - XML
  - Texto puro
- Autocompletar inteligente por linguagem
- Numeração de linhas
- Zoom de fonte com Ctrl + scroll do mouse
- Seleção de linguagem por aba (barra inferior)
- Atalhos de edição (cortar, copiar, colar)
- Salvar, abrir e criar arquivos facilmente
- Fechar abas com X vermelho
- Visual limpo, moderno e responsivo
- Menu "Informações" com dados do projeto

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
- Java 17+
- [FlatLaf](https://www.formdev.com/flatlaf/) - Tema moderno
- [RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea) - Destaque de sintaxe
- [AutoComplete](https://github.com/bobbylight/AutoComplete) - Autocompletar

## Autor
Cabral567

## Versão
1.0
