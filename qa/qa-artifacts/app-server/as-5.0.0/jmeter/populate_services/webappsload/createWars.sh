#!/bin/bash

cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/creatload/webappsload
x=1
#while [$x -eq 100]
cd ./wars
while test $x != 51
do
cp example.war ../webapps/example${x}.war
x=`expr $x + 1`
done
x=`expr $x - 1`
mkdir "/home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/webapps/${x}"
mv /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/webapps/*.war /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/webapps/${x}
