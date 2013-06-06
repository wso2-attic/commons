#!/bin/bash

#cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/xsds
x=2
while test $x != 100
do
cp endpoint1.xml ./endpoints.xml
sed 's/endpoint1/endpoint'$x'/g' ./endpoints.xml > ./endpoint${x}.xml
x=`expr $x + 1`
done

