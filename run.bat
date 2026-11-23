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
javac --release 21 -d bin -sourcepath src src\Main.java
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)
if /i "%~1"=="test" (
    javac --release 21 -d bin -sourcepath src test\EngineTests.java
    if errorlevel 1 exit /b 1
    java -cp bin EngineTests
    if errorlevel 1 exit /b 1
    exit /b 0
)
java -Dstdout.encoding=UTF-8 -cp bin Main %*
