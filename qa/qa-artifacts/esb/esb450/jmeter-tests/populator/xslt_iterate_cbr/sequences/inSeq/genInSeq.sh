#!/bin/bash

for c in `seq 2 100`; 
do
 cp complexInSeq_1.xml complexInSeq_$c.xml ; 
 sed -i  "s/complexInSeq_1/complexInSeq_$c/g" complexInSeq_$c.xml ;
 done
