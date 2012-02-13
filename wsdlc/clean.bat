@echo off

@call vcvars32.bat /nologo

@echo.

@echo Building WSDLC...

@echo.

@cd build\win32

@if exist int.msvc rmdir /q int.msvc

nmake clean

@cd ..\..



