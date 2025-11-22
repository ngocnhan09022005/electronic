@echo off
cd /d %~dp0
echo ========================================
echo Starting Spring Boot Application...
echo ========================================
echo.
echo Waiting for application to start...
echo Check logs below for any errors...
echo.
echo Press Ctrl+C to stop the application
echo.
echo ========================================
mvn spring-boot:run
pause

