@echo off
chcp 65001 >nul
echo Компіляція Main.java...
if not exist "bin" mkdir bin
javac -d bin src\Main.java
if %ERRORLEVEL% equ 0 (
    echo Компіляція успішна! Запускаємо програму...
    echo ----------------------------------------
    java -cp bin Main
    echo ----------------------------------------
    echo Програма завершила роботу.
) else (
    echo Помилка компіляції!
)
pause
