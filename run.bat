@echo off
chcp 65001 >nul
echo Compiling Main.java...
if not exist "bin" mkdir bin
javac -d bin src\kurs_auto\Kurs_Auto.java
if errorlevel 1 goto compile_error

echo Compilation successful! Starting program...
echo ----------------------------------------
java -cp bin kurs_auto.Kurs_Auto
echo ----------------------------------------
echo Program finished.
goto end

:compile_error
echo Compilation failed!

:end
pause