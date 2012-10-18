#!/bin/bash

cd /home/as5test/artifacts/jaxwsservice-load/webappsload
x=1
#while [$x -eq 100]
cd ./wars
while test $x != 11
do
cp JaxWsService_1.0.0.war ../webapps/JaxWsService_1.0.${x}.war
x=`expr $x + 1`
done
x=`expr $x - 1`
mkdir "/home/as5test/artifacts/jaxwsservice-load/webappsload/webapps/${x}"
mv /home/as5test/artifacts/jaxwsservice-load/webappsload/webapps/*.war /home/as5test/artifacts/jaxwsservice-load/webappsload/webapps/${x}
