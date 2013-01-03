
;--------------------------------
;Include Modern UI

  !include "MUI2.nsh"

  !include "LogicLib.nsh"
  !include "EnvVarUpdate.nsh"

;--------------------------------
  SetCompressor lzma

;--------------------------------
;General

  ;Name and file
  Name "${project.name}-${project.version}"
  OutFile "..\${project.name}-${project.version}-Setup.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\${project.name}"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKLM "Software\${project.name}" ""

  ;Request application privileges for Windows Vista
  ;RequestExecutionLevel user

;--------------------------------
;Variables

  Var StartMenuFolder
  
;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
  
;--------------------------------
;Language Selection Dialog Settings

  ;Remember the installer language
  !define MUI_LANGDLL_REGISTRY_ROOT "HKLM" 
  !define MUI_LANGDLL_REGISTRY_KEY "Software\${project.name}" 
  !define MUI_LANGDLL_REGISTRY_VALUENAME "Installer Language"  

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "setup-win32\LICENSE"
  !insertmacro MUI_PAGE_COMPONENTS
  
  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKLM" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${project.name}" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  !define MUI_STARTMENUPAGE_DEFAULTFOLDER "${project.name}"
  !insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder

  !insertmacro MUI_PAGE_DIRECTORY  
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages

  ; first language is the default language
  !insertmacro MUI_LANGUAGE "English"
  LangString DESC_SecMain ${LANG_ENGLISH} "${project.name}s main components"
  LangString MBOX_ALREADY_INSTALLED ${LANG_ENGLISH} "$R0 is already installed! Please uninstall first."

  !insertmacro MUI_LANGUAGE "German"
  LangString DESC_SecMain ${LANG_GERMAN} "${project.name}s Haupt Komponenten"
  LangString MBOX_ALREADY_INSTALLED ${LANG_GERMAN} "$R0 ist bereits installiert! Bitte zuerst deinstallieren."

  ;---
  !insertmacro MUI_LANGUAGE "French"
  LangString DESC_SecMain ${LANG_FRENCH} "${project.name}s main components"
  LangString MBOX_ALREADY_INSTALLED ${LANG_FRENCH} "$R0 is already installed! Please uninstall first."  

  !insertmacro MUI_LANGUAGE "Polish"
  LangString DESC_SecMain ${LANG_POLISH} "${project.name}s main components"
  LangString MBOX_ALREADY_INSTALLED ${LANG_POLISH} "$R0 is already installed! Please uninstall first."  

  !insertmacro MUI_LANGUAGE "Romanian"
  LangString DESC_SecMain ${LANG_ROMANIAN} "${project.name}s main components"
  LangString MBOX_ALREADY_INSTALLED ${LANG_ROMANIAN} "$R0 is already installed! Please uninstall first."

  !insertmacro MUI_LANGUAGE "Ukrainian"
  LangString DESC_SecMain ${LANG_UKRAINIAN} "${project.name}s main components"
  LangString MBOX_ALREADY_INSTALLED ${LANG_UKRAINIAN} "$R0 is already installed! Please uninstall first." 

;--------------------------------
  
  ReserveFile "setup-win32\LICENSE"
  !insertmacro MUI_RESERVEFILE_LANGDLL

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
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
  SetOutPath "$INSTDIR\docs"
  CreateShortCut "$SMPROGRAMS\$StartMenuFolder\${project.name} Help.lnk" "$INSTDIR\docs\index.html"	
  SetOutPath "$INSTDIR"
  CreateShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall ${project.name}.lnk" "$INSTDIR\uninstall.exe"  
  !insertmacro MUI_STARTMENU_WRITE_END

SectionEnd

;--------------------------------
;Descriptions

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
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
  Delete "$SMPROGRAMS\$StartMenuFolder\${project.name} Help.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall ${project.name}.lnk"
  RMDir "$SMPROGRAMS\$StartMenuFolder"  

  ; Remove registry keys
  DeleteRegKey /ifempty HKCU "Software\${project.name}"
  DeleteRegKey /ifempty HKLM "Software\${project.name}"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}"

SectionEnd

Function .onInit
  !insertmacro MUI_LANGDLL_DISPLAY
  SetShellVarContext all
  
  ReadRegStr $R0 HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${project.name}" "DisplayName"
  StrCmp $R0 "" 0 isInstalled
  
  Goto done
  
  isInstalled:
  MessageBox MB_OK $(MBOX_ALREADY_INSTALLED)
  Abort
  
  done:
  
FunctionEnd

Function un.onInit

  !insertmacro MUI_UNGETLANGUAGE
  
FunctionEnd

