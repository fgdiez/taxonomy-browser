; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
AppName=Taxy
AppVerName=Taxy v1.1.0-beta2
AppPublisher=Universidad de Malaga
AppPublisherURL=http://www.bitlab.es
AppSupportURL=http://www.bitlab.es
AppUpdatesURL=http://www.bitlab.es
DefaultDirName={pf}\Taxy1.1.0-beta2
DefaultGroupName=Taxy1.1.0-beta2
AllowNoIcons=yes
OutputBaseFilename=Taxy1.1.0-beta2_setup
Compression=lzma
SolidCompression=yes
SourceDir=release\
LicenseFile=LICENSE

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "taxy.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "lib/taxy.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "lib/RepoPersistence-client.jar"; DestDir: "{app}\lib"; Flags: ignoreversion
Source: "lib/axiom-api-1.2.5.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/axiom-impl-1.2.5.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/axis2-kernel-1.3.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/commons-codec-1.3.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/commons-httpclient-3.0.1.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/commons-logging-1.1.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/jibx-run-1.1.5.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/wsdl4j-1.6.2.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/XmlSchema-1.3.2.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/stax-api-1.0.1.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/wstx-asl-3.2.1.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "lib/BrowserLauncher2-12.jar"; DestDir: "{app}/lib"; Flags: ignoreversion
Source: "plugins/acgtRepoPlugin.jar"; DestDir: "{app}/plugins"; Flags: ignoreversion
Source: "plugins/ncbitaxaPlugin.jar"; DestDir: "{app}/plugins"; Flags: ignoreversion
Source: "LICENSE"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\Taxy"; Filename: "{app}\taxy.exe"; WorkingDir: "{app}"
Name: "{commondesktop}\Taxy"; Filename: "{app}\taxy.exe"; WorkingDir: "{app}"; Tasks: desktopicon
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\Taxy"; Filename: "{app}\taxy.exe"; WorkingDir: "{app}"; Tasks: quicklaunchicon

[Run]
Filename: "{app}\taxy.exe"; Description: "{cm:LaunchProgram,Taxy}"; Flags: nowait postinstall skipifsilent

