@echo off
javac -d .\bin\ .\src\*.java .\src\game\*.java .\src\strategy\bettingStrategy\*.java .\src\strategy\playingStrategy\*.java .\src\simulation\*.java .\src\util\*.java
if %errorlevel% == 0 (
    java -cp .\bin\ Main
    pause
) else (
    echo Compilation failed.
    pause
)