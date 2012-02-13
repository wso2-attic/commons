AUTOCONF = configure.in
!include $(AUTOCONF)

TARGET_DLL_DIR   = ..\..\lib
TARGET_LIB_DIR   = ..\..\lib
WSDLC_BINDIR 	 = ..\deploy
WSDLC_SOURCE_DIR = ..\..

#################### directories for intermediate files
WSDLC_INTDIR = .\int.msvc
WSDLC = wsdlc

#################### include path
WSDLC_INCLUDE_PATH = /I$(WSDLC_SOURCE_DIR)\include \
		     /I$(WSFC_HOME_DIR)\include \
		     /I$(WSDLC_SOURCE_DIR) \
		     /I$(LIBXML2_BIN_DIR)\include \
		     /I$(LIBXSLT_BIN_DIR)\include \
		     /I$(ICONV_BIN_DIR)\include 
		     
#################### compiler options
CC = @cl.exe
CFLAGS = /D "WIN32" /D "_WINDOWS" /D "_MBCS"  /D "AXIS2_DECLARE_EXPORT"\
	 /D "_CRT_SECURE_NO_DEPRECATE" /D "_CRT_SECURE_NO_WARNINGS" \
         /D "AXIS2_SVR_MULTI_THREADED" /WX /W3 /wd4100 /MP10 /nologo $(WSDLC_INCLUDE_PATH)

#################### linker options
LD = @link.exe
LDFLAGS = /NOLOGO /WX \
	  /LIBPATH:$(WSFC_HOME_DIR)\lib /LIBPATH:$(LIBXML2_BIN_DIR)\lib /LIBPATH:$(LIBXSLT_BIN_DIR)\lib
		 
LIBS =  Rpcrt4.lib Ws2_32.lib libxml2.lib libxslt.lib
WSFC_LIBS = sandesha2.lib neethi_util.lib neethi.lib mod_rampart.lib axutil.lib axis2_parser.lib axis2_http_sender.lib axis2_http_receiver.lib axis2_engine.lib axiom.lib 

LIBS = $(LIBS) $(WSFC_LIBS)

#################### manifest
MT = mt.exe
MT="$(MT)"
!if "$(EMBED_MANIFEST)" == "0"
_VC_MANIFEST_EMBED_EXE=
_VC_MANIFEST_EMBED_DLL=
!else
_VC_MANIFEST_EMBED_EXE= if exist $@.manifest $(MT) -nologo -manifest $@.manifest -outputresource:$@;1
_VC_MANIFEST_EMBED_DLL= if exist $@.manifest $(MT) -nologo -manifest $@.manifest -outputresource:$@;2
!endif

#################### debug symbols
!if "$(DEBUG)" == "1"
CFLAGS = $(CFLAGS) /D "_DEBUG" /Od /Z7 $(CRUNTIME)d
LDFLAGS = $(LDFLAGS) /DEBUG
!else
CFLAGS = $(CFLAGS) /D "NDEBUG" /O2 $(CRUNTIME)
LDFLAGS = $(LDFLAGS)
!endif

#################### 
WSDLC_OBJS = $(WSDLC_INTDIR)\*.obj

all: $(WSDLC).dll

clean:
	if exist $(WSDLC_BINDIR) rmdir /S /Q $(WSDLC_BINDIR)
	if exist $(WSDLC_INTDIR) rmdir /S /Q $(WSDLC_INTDIR)

install: all
	 copy $(WSDLC).dll $(TARGET_DLL_DIR)
	 copy $(WSDLC).lib $(TARGET_LIB_DIR)

$(WSDLC).dll: $(WSDLC_OBJS)
	$(LD) $(LDFLAGS) $(WSDLC_OBJS) $(LIBS) /DLL /OUT:$(WSDLC).dll \
	/IMPLIB:$(WSDLC).lib
	-@$(_VC_MANIFEST_EMBED_DLL)

$(WSDLC_INTDIR):
	@if not exist $(WSDLC_INTDIR) mkdir $(WSDLC_INTDIR)

{$(WSDLC_SOURCE_DIR)\src}.c{$(WSDLC_INTDIR)}.obj :: 
	$(CC) $(CFLAGS) /Fo$(WSDLC_INTDIR)\ /c $<

$(WSDLC_OBJS): $(WSDLC_INTDIR) 
