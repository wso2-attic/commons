#!/bin/bash

for c in `seq 2 100`; 
do
 cp Axis2PassThroughProxyService_1.xml Axis2PassThroughProxyService_$c.xml ; 
 sed -i  "s/Axis2PassThroughProxyService_1/Axis2PassThroughProxyService_$c/g" Axis2PassThroughProxyService_$c.xml ;
 sed -i  "s/Axis2PassThroughProxyInSequence_1/Axis2PassThroughProxyInSequence_$c/g" Axis2PassThroughProxyService_$c.xml ; 
 sed -i  "s/Axis2SampleService_1?wsdl/Axis2SampleService_$c?wsdl/g" Axis2PassThroughProxyService_$c.xml ;
done
