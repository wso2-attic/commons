cl.exe /nologo /D "WIN32" /D "_WINDOWS" /D "AXIS2_DECLARE_EXPORT" /D "_MBCS" /I%WSFC_HOME%\include echo_util.c echo_blocking_dual.c /link /nologo /LIBPATH:%WSFC_HOME%\lib *.obj axutil.lib axiom.lib axis2_parser.lib axis2_engine.lib neethi.lib /OUT:sec_echo_blocking_dual.exe

copy sec_echo_blocking_dual.exe %WSFC_HOME%\bin\samples
copy username_token_dual_policy.xml %WSFC_HOME%\bin\samples
