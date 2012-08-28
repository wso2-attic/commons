#!/bin/bash

for c in `seq 2 100`; 
do cp ComplexProxy_1.xml ComplexProxy_$c.xml ; 
 sed -i  "s/ComplexProxy_1/ComplexProxy_$c/g" ComplexProxy_$c.xml ; 
 sed -i  "s/complexInSeq_1/complexInSeq_$c/g" ComplexProxy_$c.xml ;
 sed -i  "s/complexOutSeq_1/complexOutSeq_$c/g" ComplexProxy_$c.xml ;
done
