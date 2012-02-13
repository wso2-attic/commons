#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-3.0.0_jms3/message* ./tests/temp/
cp -f ./tests/temp/message1k.xml ./scenario/wso2esb-3.0.0_jms3/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-3.0.0_jms3/config_warmup.xml ./conf/config.xml
./Test.pl -a
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-3.0.0_jms3/
rm -rf tests/temp
