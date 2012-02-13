#!/bin/bash
mkdir ./tests/temp
mv ./scenario/wso2esb-2.1.3_cbr/message* ./tests/temp/
cp -f ./tests/temp/message10k.xml ./scenario/wso2esb-2.1.3_cbr/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/wso2esb-2.1.3_cbr/config_10k.xml ./conf/config.xml
./Test.pl
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/wso2esb-2.1.3_cbr/
rm -rf tests/temp
