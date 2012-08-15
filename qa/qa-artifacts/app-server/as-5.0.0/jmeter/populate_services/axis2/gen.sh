#!/bin/bash

cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/axis2/
i=1
while test $i != 11
do
sed 's/Axis2SampleService/Axis2SampleService_'$i'/g' service.xml > ./service/META-INF/services.xml
cd ./service
jar -cvf Axis2SampleService_${i}.aar *
#mkdir ../bulk
mv Axis2SampleService_${i}.aar ../bulk
i=`expr $i + 1`
cd ../
done
 
