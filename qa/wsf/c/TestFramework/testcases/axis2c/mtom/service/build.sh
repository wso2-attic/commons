#!/bin/bash
ANY_HOME=$AXIS2C_HOME
#ANY_HOME=$WSFC_HOME
INST_DIR=$WSFC_HOME
CLIENT_REPO=$WSFC_HOME/client_repo

gcc -shared *.c -g -o mymtom.so -I$ANY_HOME/include/axis2-1.4.0 \
                        -I$ANY_HOME/include/rampart-1.0 \
                        -L$ANY_HOME/lib \
                    -laxutil \
                    -laxis2_axiom \
                    -laxis2_engine \
                    -laxis2_parser \
                    -lpthread \
                    -laxis2_http_sender \
                    -laxis2_http_receiver
#mkdir /home/manoj/wsf/wso2-wsf-c-src-1.1.0/deploy/services/mymtom
#cp mymtom.so  /home/manoj/wsf/wso2-wsf-c-src-1.1.0/deploy/services/mymtom
