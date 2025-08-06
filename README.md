# ToDoWidget - Editor de Texto Avançado

Editor de texto multi-abas, leve e versátil para Windows, feito em Java 17+ com Swing, FlatLaf e RSyntaxTextArea.

## Principais Funcionalidades
- Múltiplas abas (multi-tab)
- Destaque de sintaxe para várias linguagens (Java, Python, C, C++, HTML, JS, XML, texto)
- Autocompletar básico por linguagem
- Numeração de linhas
- Zoom de fonte com Ctrl + scroll do mouse
- Seleção de linguagem por aba (barra inferior)
- Atalhos de edição (cortar, copiar, colar)
- Salvar, abrir e criar arquivos facilmente
- Fechar abas com X vermelho
- Visual limpo, moderno e responsivo

- Alternância de tema (claro/escuro) pelo menu "Tema"
- Menu "Informações" com dados do autor e do programa

## Como executar
1. Certifique-se de ter o Java 17+ instalado.
2. Compile e gere o JAR com dependências:
   ```shell
   mvn clean package
   ```
3. Execute o aplicativo:
   ```shell
   java -jar target/ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## Estrutura do projeto
- `ToDoWidgetApp.java`: interface principal do editor

Menus principais:
- Arquivo, Editar, Tema, Informações, Ajuda

## Dependências
- [FlatLaf](https://www.formdev.com/flatlaf/)
- [RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea)
- [AutoComplete](https://github.com/bobbylight/AutoComplete)

## Futuras extensões
- Temas customizáveis
  (já possui claro/escuro via menu)
- Busca e substituição
- Suporte a plugins
- Mais opções de autocompletar
