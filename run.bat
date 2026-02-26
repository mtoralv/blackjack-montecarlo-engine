@echo off
javac -d .\bin\ .\src\*.java
if %errorlevel% == 0 (
    java -cp .\bin\ Main
) else (
    echo Compilation failed.
    pause
)