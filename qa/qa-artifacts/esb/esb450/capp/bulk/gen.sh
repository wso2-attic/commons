#!/bin/bash

cd /opt/svn/qa-artifacts/esb/esb450/capp/bulk
i=1
while test $i != 101
#mv /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/endpoint0_1.0.0 /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/endpoint${i}_1.0.0
do
mkdir ./ESBproject-1.0.0/endpoint${i}_1.0.0
mkdir ./ESBproject-1.0.0/endpoint${i}_1.0.0/resources
#mv /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/endpoint0_1.0.0 /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/endpoint${i}_1.0.0
echo 111
#mv /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/endpoint${i}_1.0.0/resources/endpoint.xml ESBproject-1.0.0/endpoint${i}_1.0.0/resources/endpoint${i}.xml
sed 's/endpoint/endpoint'$i'/g' registry-info.xml > ./ESBproject-1.0.0/endpoint${i}_1.0.0/registry-info.xml
sed 's/endpoint/endpoint'$i'/g' endpoint.xml > ./ESBproject-1.0.0/endpoint${i}_1.0.0/resources/endpoint${i}.xml
sed 's/endpoint/endpoint'$i'/g' artifact.xml > ./ESBproject-1.0.0/endpoint${i}_1.0.0/artifact.xml
sed 's/endpoint/endpoint'$i'/g ; s/ESBproject/ESBproject'$i'/g  ' artifacts.xml > ./ESBproject-1.0.0/artifacts.xml
#sed 's/ESBproject/ESBproject'$i'/g' artifacts.xml > ./ESBproject-1.0.0/artifacts.xml

cd ./ESBproject-1.0.0
jar -cvf ESBproject${i}-1.0.0.car *

mv ESBproject${i}-1.0.0.car ../bulk
rm -rf /opt/svn/qa-artifacts/esb/esb450/capp/bulk/ESBproject-1.0.0/*
i=`expr $i + 1`
cd ../
done
