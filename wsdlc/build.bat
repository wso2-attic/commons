@echo off

@call vcvars32.bat /nologo

@echo.

@echo Building WSDLC...

@echo.

@cd build\win32

nmake 

mt.exe -manifest wsdlc.dll.manifest -outputresource:wsdlc.dll;2

nmake install

@cd ..\..



