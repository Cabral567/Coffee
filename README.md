# To-Do Widget

Mini aplicativo de lista de tarefas para Windows, feito em Java 17+ com Swing e FlatLaf.

## Funcionalidades
- Adicionar tarefas
- Marcar tarefas como concluídas
- Persistência automática em JSON
- Sempre visível (always on top)
- Janela pequena, sem bordas
- Design limpo e moderno

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
- `Task.java`: modelo de tarefa
- `TaskPersistence.java`: persistência em JSON
- `ToDoWidgetApp.java`: interface principal

## Dependências
- [Jackson](https://github.com/FasterXML/jackson)
- [FlatLaf](https://www.formdev.com/flatlaf/)

## Futuras extensões
- Teclas de atalho (DEL para apagar)
- Ícone de bandeja
- Sincronização com Google Tasks
