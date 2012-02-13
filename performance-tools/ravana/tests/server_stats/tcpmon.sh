#!/bin/bash
mkdir ./tests/temp
mv ./scenario/server_stats/message* ./tests/temp/
cp -f ./tests/temp/message1k.xml ./scenario/server_stats/
mv ./conf/config.xml ./tests/temp
cp -f ./tests/server_stats/config_tcpmon.xml ./conf/config.xml
./Test.pl 
mv ./tests/temp/config.xml ./conf
mv ./tests/temp/* ./scenario/server_stats/
rm -rf tests/temp
