#!/bin/bash
# Script de instalação do Coffee para Linux seguindo boas práticas
set -e

# Diretórios de instalação (padrão Linux)
APP_NAME="coffee"
INSTALL_DIR="/opt/$APP_NAME"
BIN_DIR="/usr/local/bin"
DESKTOP_DIR="/usr/share/applications"
ICON_DIR="/usr/share/icons/hicolor/256x256/apps"
USER_DATA_DIR="$HOME/.local/share/$APP_NAME"
USER_CONFIG_DIR="$HOME/.config/$APP_NAME"

# Função de desinstalação
uninstall() {
    echo "Removendo Coffee..."
    sudo rm -rf "$INSTALL_DIR"
    sudo rm -f "$BIN_DIR/$APP_NAME"
    sudo rm -f "$BIN_DIR/${APP_NAME}-remove"
    sudo rm -f "$DESKTOP_DIR/$APP_NAME.desktop"
    sudo rm -f "$ICON_DIR/$APP_NAME.png"
    rm -rf "$USER_DATA_DIR" "$USER_CONFIG_DIR"
    echo "Desinstalação concluída."
    exit 0
}

# Se o comando for coffe-remove, executa desinstalação
if [[ $(basename "$0") == "coffee-remove" ]]; then
    uninstall
fi

# Verifica argumento de desinstalação
if [[ "$1" == "--remove" || "$1" == "--uninstall" ]]; then
    uninstall
fi

# Compila o projeto (na subpasta Coffee)
cd Coffee
mvn clean package
cd ..

# Cria diretórios de instalação
sudo mkdir -p "$INSTALL_DIR" "$ICON_DIR"
mkdir -p "$USER_DATA_DIR" "$USER_CONFIG_DIR"

# Move o JAR gerado
sudo cp Coffee/target/Coffee.jar "$INSTALL_DIR/"

# Instala ícone se existir
if [ -f "Coffee/assets/logo.png" ]; then
    sudo cp Coffee/assets/logo.png "$ICON_DIR/$APP_NAME.png"
fi

# Cria atalho para execução
echo -e "#!/bin/bash\njava -jar $INSTALL_DIR/Coffee.jar \"$@\"" | sudo tee "$BIN_DIR/$APP_NAME" > /dev/null
sudo chmod +x "$BIN_DIR/$APP_NAME"

# Cria comando de remoção
echo -e "#!/bin/bash\nbash $PWD/install.sh --remove" | sudo tee "$BIN_DIR/${APP_NAME}-remove" > /dev/null
sudo chmod +x "$BIN_DIR/${APP_NAME}-remove"

# Cria arquivo .desktop
echo "[Desktop Entry]\nName=Coffee\nExec=$BIN_DIR/$APP_NAME %f\nIcon=$ICON_DIR/$APP_NAME.png\nType=Application\nCategories=Utility;TextEditor;" | sudo tee "$DESKTOP_DIR/$APP_NAME.desktop" > /dev/null

echo "Instalação concluída! Use o comando 'coffee' para executar ou 'coffee-remove' para desinstalar."
