#define MyAppName "Coffee Editor"
#define MyAppVersion "1.0"
#define MyAppPublisher "Cabral567"
#define MyAppURL "https://github.com/Cabral567/Coffee"
#define MyAppExeName "Coffee.exe"

[Setup]
AppId={{COFFEE-EDITOR-2025}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DisableProgramGroupPage=yes
LicenseFile=LICENSE
OutputDir=installer
OutputBaseFilename=CoffeeSetup
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
; Arquivos principais do programa
Source: "target\coffee-1.0-jar-with-dependencies.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "LICENSE"; DestDir: "{app}"; Flags: ignoreversion
; JRE embutido (você precisará baixar e colocar na pasta 'jre')
Source: "jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
; Arquivo batch para iniciar o programa
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\start.bat"; IconFilename: "{app}\coffee.ico"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\start.bat"; IconFilename: "{app}\coffee.ico"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent
