#!/bin/bash

cd /home/as5test/artifacts/jaxrs-webappload/webappsload
x=1
#while [$x -eq 100]
cd ./wars
while test $x != 101
do
cp jaxrs_basic.war ../webapps/jaxrs_basic${x}.war
x=`expr $x + 1`
done
x=`expr $x - 1`

mkdir /home/as5test/artifacts/jaxrs-webappload/webappsload/webapps/${x}
mv /home/as5test/artifacts/jaxrs-webappload/webappsload/webapps/*.war /home/as5test/artifacts/jaxrs-webappload/webappsload/webapps/${x}
