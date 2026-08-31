@echo off
setlocal

set "JAVA_HOME=C:\Temp\Programs\JavaStack\jdk-17.0.19+10"
set "MAVEN_HOME=C:\Temp\Programs\JavaStack\apache-maven-3.9.16"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

if "%~1"=="" (
    echo [ERROR] Falta indicar que evaluador ejecutar.
    echo.
    echo   Uso:  corregir.bat EvaluadorR1
    echo         corregir.bat EvaluadorR2
    echo.
    exit /b 1
)

echo --- Iniciando Correccion Automatica: %~1 ---

set PWDEBUG=0
mvn compile exec:java -Dexec.mainClass=%~1

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Hubo un problema con la compilacion o ejecucion de Maven.
    exit /b 1
)

echo --- Correccion terminada: %~1 ---
endlocal