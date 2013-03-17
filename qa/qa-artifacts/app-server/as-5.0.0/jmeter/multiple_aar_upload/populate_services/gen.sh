#!/bin/bash

cd {change_me_to_the_current_directory}
i=1
while test $i != 101
do
sed 's/Axis2SampleService/Axis2SampleService_t1_'$i'/g' service.xml > ./service/META-INF/services.xml
cd ./service
jar -cvf Axis2SampleService_t1_${i}.aar *
#mkdir ../bulk
mv Axis2SampleService_t1_${i}.aar ../bulk1
i=`expr $i + 1`
cd ../
done
 
