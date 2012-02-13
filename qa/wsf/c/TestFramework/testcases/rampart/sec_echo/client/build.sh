#!/bin/bash
#ANY_HOME=$AXIS2C_HOME
ANY_HOME=$WSFC_HOME
INST_DIR=$WSFC_HOME
CLIENT_REPO=$WSFC_HOME/client_repo

gcc echo.c -g -o sec_echo -I$ANY_HOME/include/axis2-1.4.0/ \
                        -I$ANY_HOME/include/rampart-1.2.0 \
                        -L$ANY_HOME/lib \
                    -laxutil \
                    -laxis2_axiom \
                    -laxis2_engine \
                    -laxis2_parser \
                    -lpthread \
                    -laxis2_http_sender \
                    -laxis2_http_receiver

cp sec_echo $WSFC_HOME/bin/samples
cp sec_echo_policy.xml $WSFC_HOME/bin/samples


