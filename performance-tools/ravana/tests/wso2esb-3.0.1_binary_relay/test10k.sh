#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.1_binary_relay/message* ./tests/temp/
cp -f ./tests/temp/message10k.xml ./scenario/wso2esb-3.0.1_binary_relay/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.1_binary_relay/config_10k.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.1_binary_relay/
rm -rf tests/temp
