# Coffee Editor

```
   ___      __  __           ___    _ _ _             
  / __\___ / _|/ _| ___  ___ / __\__| (_) |_ ___  _ __ 
 / /  / _ \| |_| |_ / _ \/ _ / /__/ _` | | __/ _ \| '__|
/ /__| (_) |  _|  _|  __/  __\___| (_| | | || (_) | |   
\____/\___/|_| |_|  \___|\___|   \__,_|_|\__\___/|_|   

      Super Leve • Ultrarrápido • Minimalista
```

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

## Instalação

### Windows - Instalador Automático (Recomendado)
1. Baixe o arquivo `setup.exe` da seção de releases
2. Execute o instalador como administrador
3. Siga as instruções do assistente de instalação
4. O Coffee será instalado e configurado automaticamente
5. Ícone será criado na área de trabalho e menu iniciar
6. **Funcionalidade "Abrir com"** será configurada automaticamente

### Execução Manual (JAR)
**Pré-requisito:** Java 17+ instalado no sistema
```shell
java -jar Coffee-1.0.jar
```

**Para usar "abrir com" manualmente:**
```shell
java -jar Coffee-1.0.jar "caminho/para/arquivo.txt"
```

### Recursos do Instalador Windows
O instalador `setup.exe` configura automaticamente:
- **Associação de arquivos** - Coffee como opção "Abrir com" para arquivos de código
- **Variáveis de ambiente** - PATH configurado para execução global
- **Ícones do sistema** - Área de trabalho e menu iniciar
- **Desinstalador** - Remoção limpa através do Painel de Controle
- **Auto-atualização** - Verificação automática de novas versões

## Build a partir do código fonte

### Requisitos para desenvolvimento
- Java Development Kit (JDK) 17 ou superior
- Maven 3.6+
- Git

### Passos para compilar
1. Clone o repositório:
   ```shell
   git clone https://github.com/Cabral567/Coffee.git
   cd Coffee
   ```
2. Compile o projeto:
   ```shell
   mvn clean compile
   ```
3. Gere o JAR executável:
   ```shell
   mvn clean package
   ```
4. Execute a partir do código:
   ```shell
   mvn exec:java -Dexec.mainClass="widget.ToDoWidgetApp"
   ```

### Estrutura do projeto
- `src/main/java/widget/ToDoWidgetApp.java`: Classe principal do editor
- `pom.xml`: Configuração Maven com dependências
- `README.md`: Documentação do projeto

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
<div align="center">
  <img src="./assets/profile.png" alt="Cabral567" width="150" style="border-radius: 50%;">
  <br>
  <strong>Cabral567</strong>
  <br>
  <em>Desenvolvedor focado em criar ferramentas leves e eficientes</em>
</div>

## Versão
**1.0** - *Editor Super Leve e Rápido*

---

*"Coffee: O editor que não pesa na sua máquina, mas acelera seu trabalho!"*
