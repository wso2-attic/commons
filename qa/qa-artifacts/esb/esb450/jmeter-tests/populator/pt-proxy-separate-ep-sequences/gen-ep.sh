#!/bin/bash

for c in `seq 2 10`; 
do
 cp Axis2SampleServiceEP_1.xml Axis2SampleServiceEP_$c.xml ; 
 sed -i  "s/Axis2SampleServiceEP_1/Axis2SampleServiceEP_$c/g" Axis2SampleServiceEP_$c.xml ;
 sed -i  "s/Axis2SampleService_1/Axis2SampleService_$c/g" Axis2SampleServiceEP_$c.xml ; 
done
