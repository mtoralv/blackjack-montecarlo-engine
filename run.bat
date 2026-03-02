@echo off
javac -d .\bin\ .\src\*.java .\src\game\*.java .\src\strategy\*.java .\src\simulation\*.java .\src\util\*.java
if %errorlevel% == 0 (
    java -cp .\bin\ Main
) else (
    echo Compilation failed.
    pause
)