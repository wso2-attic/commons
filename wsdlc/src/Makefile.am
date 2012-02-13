lib_LTLIBRARIES=libwsdlc.la

CFLAGS = -g -O0

libwsdlc_la_SOURCES = data.c data_template.c data_util.c parser.c type_map.c util.c xslt.c wsdl_mode.c

libwsdlc_la_LIBADD   = -L$(prefix)/lib -lxml2 -lxslt -lneethi_util -lneethi -laxis2_engine -laxis2_axiom -laxis2_parser -lmod_rampart -laxutil -laxis2_http_sender -laxis2_http_receiver -laxis2_http_common 

INCLUDES =-I../include/ -I/usr/include/libxml2/ -I$(prefix)/include/axis2-1.4.0/ -I$(prefix)/include/rampart-1.2.0/

