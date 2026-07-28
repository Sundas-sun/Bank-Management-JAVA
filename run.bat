@echo off
REM Run this script from the project root.
REM Place MySQL Connector/J in lib\mysql-connector-java.jar before running.

set "JDK_PATH=C:\Program Files\Apache NetBeans\jdk"
if not exist "%JDK_PATH%\bin\javac.exe" (
    if not defined JAVA_HOME (
        echo JDK not found at %JDK_PATH% and JAVA_HOME is not set.
        echo Please install JDK or update JAVA_HOME.
        pause
        exit /b 1
    ) else (
        set "JDK_PATH=%JAVA_HOME%"
    )
)

if not exist "lib\mysql-connector-java.jar" (
    echo Please copy MySQL Connector/J to lib\mysql-connector-java.jar
    pause
    exit /b 1
)

if not exist bin mkdir bin
"%JDK_PATH%\bin\javac.exe" -d bin *.java models\*.java dao\*.java services\*.java utils\*.java ui\*.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)

"%JDK_PATH%\bin\java.exe" -cp "bin;lib\mysql-connector-java.jar" Main
