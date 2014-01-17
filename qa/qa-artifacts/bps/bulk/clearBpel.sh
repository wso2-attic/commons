#!/bin/bash

#clear the bpels created
CARBON_HOME=/home/pavithra/Products/BPS/wso2bps-3.2.0
x=1
while test $x != 101
do
rm HelloWorld${x}.zip
rm -rf HelloWorld
rm -rf HelloWorld${x}
rm $CARBON_HOME/repository/deployment/server/bpel/HelloWorld${x}.zip 
x=`expr $x + 1`
done
