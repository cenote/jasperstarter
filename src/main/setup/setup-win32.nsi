
;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"

  !include "LogicLib.nsh"
  !include "EnvVarUpdate.nsh"

;--------------------------------
;General

  ;Name and file
  Name "${project.name}-${project.version}"
  OutFile "..\${project.build.finalName}-Setup.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\${project.name}"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKLM "Software\${project.name}" ""

  ;Request application privileges for Windows Vista
  ;RequestExecutionLevel user

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "setup-win32\LICENSE"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections
Section
  SetShellVarContext all

  ;Store installation folder
  WriteRegStr HKLM "Software\${project.name}" "" $INSTDIR
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "DisplayName" "${project.name} ${project.version}"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "NoRepair" 1
  SetOutPath $INSTDIR
  WriteUninstaller "$INSTDIR\uninstall.exe"
  
SectionEnd

Section "${project.name}" SecMain
  SectionIn RO
  
  SetShellVarContext all
  
  SetOutPath "$INSTDIR"
  
  File /r "setup-win32\*.*"

  ; append to path
  ${EnvVarUpdate} $0 "PATH" "P" "HKLM" "$INSTDIR\bin"

  ; create menu items
  SetShellVarContext all
  CreateDirectory "$SMPROGRAMS\${project.name}"
  
  SetOutPath "$INSTDIR\docs"
  CreateShortCut "$SMPROGRAMS\${project.name}\${project.name} Help.lnk" "$INSTDIR\docs\index.html"
  
  SetOutPath "$INSTDIR"
  CreateShortCut "$SMPROGRAMS\${project.name}\Uninstall ${project.name}.lnk" "$INSTDIR\uninstall.exe"


SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecMain ${LANG_ENGLISH} "${project.name}s main components"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SecMain} $(DESC_SecMain)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"
  SetShellVarContext all

  Delete "$INSTDIR\Uninstall.exe"

  RMDir /r "$INSTDIR"

  ; remove from path
  ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" "$INSTDIR\bin"
  
  ; Remove shortcuts
  RMDir /r "$SMPROGRAMS\${project.name}"

  ; Remove registry keys
  DeleteRegKey /ifempty HKCU "Software\${project.name}"
  DeleteRegKey /ifempty HKLM "Software\${project.name}"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}"

SectionEnd

Function .onInit
  SetShellVarContext all
  
  ReadRegStr $R0 HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "DisplayName"
  StrCmp $R0 "" 0 isInstalled
  
  Goto done
  
  isInstalled:
  MessageBox MB_OK "$R0 is already installed! Please uninstall first."
  Abort
  
  done:
  
FunctionEnd

