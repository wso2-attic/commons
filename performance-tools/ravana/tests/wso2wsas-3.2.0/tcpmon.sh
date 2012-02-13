#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2wsas-3.2.0/message* ./tests/temp/
cp -f ./tests/temp/message256b.xml ./scenario/wso2wsas-3.2.0/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2wsas-3.2.0/config_tcpmon.xml ./conf/config.xml
./Test.pl -b
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2wsas-3.2.0/
rm -rf tests/temp
