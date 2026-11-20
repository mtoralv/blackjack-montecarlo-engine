@echo off
rem Compile and run the Blackjack Monte Carlo engine.
rem   run.bat                 run the benchmark suite
rem   run.bat play            play in the terminal
rem   run.bat simulate ...    see "run.bat help"
rem   run.bat test            run the test suite
setlocal
cd /d "%~dp0"
if exist bin rmdir /s /q bin
mkdir bin
dir /s /b src\*.java > bin\sources.txt
javac --release 21 -d bin @bin\sources.txt
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)
if /i "%~1"=="test" (
    javac --release 21 -cp bin -d bin test\*.java
    if errorlevel 1 exit /b 1
    java -cp bin EngineTests
    exit /b %errorlevel%
)
java -Dstdout.encoding=UTF-8 -cp bin Main %*
