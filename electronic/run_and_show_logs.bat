@echo off
cd /d %~dp0
echo ========================================
echo Starting Spring Boot Application...
echo ========================================
echo.
echo Please wait for application to start...
echo Watch for "Started ElectronicApplication" message
echo Press Ctrl+C to stop
echo.
echo ========================================
mvn spring-boot:run
pause

