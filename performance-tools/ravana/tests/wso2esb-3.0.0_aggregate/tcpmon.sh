#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.0_aggregate/message* ./tests/temp/
cp -f ./tests/temp/message20k.xml ./scenario/wso2esb-3.0.0_aggregate/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.0_aggregate/config_tcpmon.xml ./conf/config.xml
./Test.pl -b
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.0_aggregate/
rm -rf tests/temp
