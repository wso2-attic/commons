#!/bin/bash

for c in `seq 2 100`; 
do
 cp RestToSoapProxy_1.xml RestToSoapProxy_$c.xml ; 
 sed -i  "s/RestToSoapProxy_1/RestToSoapProxy_$c/g" RestToSoapProxy_$c.xml ;
 done
