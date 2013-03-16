#!/bin/bash

cd /opt/svn_repos/qa_artifacts/qa-artifacts/app-server/as-5.0.0/jmeter/populate_services/axis2
i=1
while test $i != 101
do
sed 's/Axis2SampleService/Axis2SampleService_t1_'$i'/g' service.xml > ./service/META-INF/services.xml
cd ./service
jar -cvf Axis2SampleService_t1_${i}.aar *
#mkdir ../bulk
mv Axis2SampleService_t1_${i}.aar ../bulk
i=`expr $i + 1`
cd ../
done
 
