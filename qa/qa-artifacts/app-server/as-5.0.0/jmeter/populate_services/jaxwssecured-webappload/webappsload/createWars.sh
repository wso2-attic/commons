#!/bin/bash

cd /home/as5test/jaxwssecured-webappload/webappsload
x=1
#while [$x -eq 100]
cd ./wars
while test $x != 101
do
cp ut_jaxws.war ../webapps/ut_jaxws${x}.war
x=`expr $x + 1`
done
x=`expr $x - 1`
mkdir "/home/as5test/jaxwssecured-webappload/webappsload/webapps/${x}"
mv /home/as5test/jaxwssecured-webappload/webappsload/webapps/*.war /home/as5test/jaxwssecured-webappload/webappsload/webapps/${x}
