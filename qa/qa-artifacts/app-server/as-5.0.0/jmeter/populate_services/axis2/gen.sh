#!/bin/bash

cd /opt/svn_repos/qa_artifacts/qa-artifacts/app-server/as-5.0.0/jmeter/populate_services/axis2
i=1
while test $i != 11
do
sed 's/Axis2SampleService/Axis2SampleService_'$i'/g' service.xml > ./service/META-INF/services.xml
cd ./service
jar -cvf Axis2SampleService_${i}.aar *
#mkdir ../bulk
mv Axis2SampleService_${i}.aar /home/evanthika/WSO2/CARBON/CARBON4/AS/axis2services
i=`expr $i + 1`
cd ../
done
 
