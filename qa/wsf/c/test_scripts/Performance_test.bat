@echo off
set _number=0
set _max=10
echo Running some program

:Start
if %_number%==%_max% goto end

cd %AXIS2C_HOME%\bin\samples

REM Use the correct port here

start echo.exe http://localhost/axis2/services/echo
set /a _number=_number + 1
goto start

:end