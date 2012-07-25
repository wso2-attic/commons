#!/bin/bash

i=1

echo "HDR*1200*2008-01-01" >> input659.txt
while test $i != 1000001
do 
echo "ORD*$i*50.00*500*IBM*REF 10053" >> input659.txt
i=`expr $i + 1`
done








