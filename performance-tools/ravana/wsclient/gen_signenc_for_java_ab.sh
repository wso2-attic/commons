#!/bin/bash
export WSFC_HOME=/axis2c/deploywsfc
export AXIS2C_HOME=/axis2c/deploywsfc
export LD_LIBRARY_PATH="$WSFC_HOME/lib"

if [ $# -eq 0 ]
then
    echo "gen_signenc.sh <message file.xml>"
    exit 1   
fi
if [ -f $WSFC_HOME/logs/wsclient.log ]
then
    rm $WSFC_HOME/logs/wsclient.log
fi
./wsclient --log-level error --no-wsa --soap --no-mtom --sign-body --key $WSFC_HOME/samples/src/rampartc/data/keys/ahome/alice_key.pem --certificate $WSFC_HOME/samples/src/rampartc/data/keys/ahome/alice_cert.cert --recipient-certificate /home/damitha/projects/perftest-framework/wsclient/wso2carbon.pem --encrypt-payload --policy-file ./policy.xml --soap-dump http://localhost:8280/services/SignEncProxy < ./data/$1 > $1

