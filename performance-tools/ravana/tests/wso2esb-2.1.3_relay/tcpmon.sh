#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-2.1.3_relay/message* ./tests/temp/
cp -f ./tests/temp/message512b.xml ./scenario/wso2esb-2.1.3_relay/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-2.1.3_relay/config_tcpmon.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-2.1.3_relay/
rm -rf tests/temp
