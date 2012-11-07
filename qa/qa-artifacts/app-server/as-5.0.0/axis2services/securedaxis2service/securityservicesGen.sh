#!/bin/bash

cd /home/as5test/artifacts/axis2services/securedaxis2service
i=1
while test $i != 101
do
sed 's/##name/RoomManagementService_'$i'/g' service.xml > ./war/META-INF/services.xml
cd ./war
jar -cvf RoomManagementService_${i}.aar *
#mkdir ../bulk
mv RoomManagementService_${i}.aar ../appLoad
i=`expr $i + 1`
cd ../
done
 
