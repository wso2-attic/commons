#!/bin/bash

#cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/xsds
x=2
while test $x != 100
do
cp newAPI1.xml ./newAPIs.xml
sed 's/apicontext1/apicontext'$x'/g' ./newAPIs.xml > newAPIs2.xml 
sed 's/newAPI1/newAPI'$x'/g' ./newAPIs2.xml > ./newAPI${x}.xml
x=`expr $x + 1`
done

