#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.0_warehouse/message* ./tests/temp/
cp -f ./tests/temp/message2k.xml ./scenario/wso2esb-3.0.0_warehouse/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.0_warehouse/config_warmup.xml ./conf/config.xml
./Test.pl -a
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.0_warehouse/
rm -rf tests/temp
