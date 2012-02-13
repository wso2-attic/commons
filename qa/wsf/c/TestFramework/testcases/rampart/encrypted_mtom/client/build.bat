cl.exe /nologo /D "WIN32" /D "_WINDOWS" /D "AXIS2_DECLARE_EXPORT" /D "_MBCS" /I%WSFC_HOME%\include *.c /link /nologo /LIBPATH:%WSFC_HOME%\lib *.obj axutil.lib axiom.lib axis2_parser.lib axis2_engine.lib neethi.lib /OUT:sec_mtom.exe

copy sec_mtom.exe %WSFC_HOME%\bin\samples
copy sec_mtom_policy.xml %WSFC_HOME%\bin\samples
