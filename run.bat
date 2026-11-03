@echo off
chcp 65001 >nul
cd /d "%~dp0"
java -Dconsole.encoding=UTF-8 -jar dist\PatientFlowSystem.jar
pause
