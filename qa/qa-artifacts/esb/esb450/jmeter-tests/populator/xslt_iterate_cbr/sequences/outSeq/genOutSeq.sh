#!/bin/bash

for c in `seq 2 100`; 
do
 cp complexOutSeq_1.xml complexOutSeq_$c.xml ; 
 sed -i  "s/complexOutSeq_1/complexOutSeq_$c/g" complexOutSeq_$c.xml ;
 done
