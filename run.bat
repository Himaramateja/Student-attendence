@echo off
echo ============================================
echo  Student Attendance Management System
echo  Java Swing GUI Application
echo ============================================
echo.
echo Building the application (this may take a moment)...
call .\apache-maven-3.9.12\bin\mvn.cmd package -q -DskipTests
if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo.
echo Starting the application...
java -jar target\student-attendance-system-1.0.jar
pause
