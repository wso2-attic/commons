#!/bin/bash

#cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/xsds
CARBON_HOME=/home/pavithra/Products/BPS/wso2bps-3.2.0
unzip HelloWorld.zip
x=1
while test $x != 101
do
mkdir HelloWorld${x}
cp ./HelloWorld/* ./HelloWorld${x}
sed -e 's/pns:HelloWorldtest/pns:HelloWorldtest'$x'/g' -e 's/service name="wns:HelloServicetest"/service name="wns:HelloServicetest'$x'"/g' ./HelloWorld/deploy.xml > ./HelloWorld${x}/deploy.xml
sed 's/process name="HelloWorldtest"/process name="HelloWorldtest'$x'"/g' ./HelloWorld/HelloWorld.bpel > ./HelloWorld${x}/HelloWorld.bpel
sed 's/HelloServicetest/HelloServicetest'$x'/g' ./HelloWorld/HelloWorld.wsdl > ./HelloWorld${x}/HelloWorld.wsdl
cd HelloWorld${x}
zip -r ../HelloWorld${x}.zip *
cd ..
cp HelloWorld${x}.zip $CARBON_HOME/repository/deployment/server/bpel
x=`expr $x + 1`
done

