#!/bin/bash

for c in `seq 2 100`; 
do
 cp Axis2PassThroughProxyInSequence_1.xml Axis2PassThroughProxyInSequence_$c.xml ; 
 sed -i  "s/Axis2PassThroughProxyInSequence_1/Axis2PassThroughProxyInSequence_$c/g" Axis2PassThroughProxyInSequence_$c.xml ;
 sed -i  "s/Axis2SampleServiceEP_1/Axis2SampleServiceEP_$c/g" Axis2PassThroughProxyInSequence_$c.xml ; 
done
