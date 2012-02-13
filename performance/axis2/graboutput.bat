REM @echo off
setlocal enableextensions
for /f "usebackq tokens=4" %%a in (`grep "Requests per second" %1`) do set reqsper=%%a
for /f "usebackq tokens=4" %%a in (`grep "Time per request:.*(mean)" %1`) do set timeper=%%a
for /f "usebackq tokens=1" %%a in (`grep "kb/s total" %1`) do set kbs=%%a
echo %4, %2, %3, %reqsper%, %timeper%, %kbs%
