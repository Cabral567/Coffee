@echo off
echo Gerando executavel do Coffee Editor...

echo.
echo 1. Compilando o projeto...
call mvn clean package -q

if not exist "target\ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar" (
    echo ERRO: JAR nao foi gerado!
    pause
    exit /b 1
)

echo.
echo 2. Criando executavel com jpackage...
jpackage ^
    --input target ^
    --name "Coffee Editor" ^
    --main-jar ToDoWidget-1.0-SNAPSHOT-jar-with-dependencies.jar ^
    --main-class widget.ToDoWidgetApp ^
    --type exe ^
    --dest dist ^
    --app-version 1.0 ^
    --description "Coffee Editor - Editor de texto avancado" ^
    --vendor "Cabral567" ^
    --win-console ^
    --win-menu ^
    --win-shortcut

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Executavel gerado com sucesso em: dist\Coffee Editor-1.0.exe
    echo.
    echo O executavel ja inclui o Java Runtime, nao precisa instalar Java separadamente!
) else (
    echo.
    echo ✗ Erro ao gerar executavel
)

pause
