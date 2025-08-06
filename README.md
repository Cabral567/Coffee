# Coffee Editor

Editor de texto multi-abas com terminal integrado, leve e versátil para Windows, feito em Java 17+ com Swing, FlatLaf e RSyntaxTextArea.

[![GitHub Release](https://img.shields.io/github/v/release/Cabral567/Coffee?include_prereleases)](https://github.com/Cabral567/Coffee/releases)
[![License](https://img.shields.io/github/license/Cabral567/Coffee)](LICENSE)

## Download e Instalação

### Instalador Windows
1. Baixe o instalador mais recente `CoffeeSetup.exe` da [página de releases](https://github.com/Cabral567/Coffee/releases)
2. Execute o instalador e siga as instruções
3. Após a instalação, o Coffee estará disponível no Menu Iniciar e opcionalmente na Área de Trabalho
4. O programa já vem com Java embutido, não é necessário instalar o Java separadamente

### Build Manual
1. Certifique-se de ter o Java 17+ instalado
2. Clone o repositório:
   ```shell
   git clone https://github.com/Cabral567/Coffee.git
   cd Coffee
   ```
3. Compile e gere o JAR com dependências:
   ```shell
   mvn clean package
   ```
4. O JAR será gerado em `target/coffee-1.0-jar-with-dependencies.jar`

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
- Launch4j (para gerar .exe)
- Inno Setup (para criar o instalador)

### Gerando o Executável
1. Primeiro, gere o JAR com dependências:
   ```shell
   mvn clean package
   ```

2. Use o Launch4j para criar o executável:
   - Configure o caminho do JAR de entrada: `target/coffee-1.0-jar-with-dependencies.jar`
   - Configure o arquivo de saída: `Coffee.exe`
   - Defina a versão mínima do Java como 17
   - Configure o ícone e informações do programa

3. Use o Inno Setup para criar o instalador:
   - Crie um novo script usando o assistente
   - Inclua o `Coffee.exe` e as dependências
   - Configure informações do programa, ícones e atalhos
   - Compile para gerar `CoffeeSetup.exe`

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
