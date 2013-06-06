#!/bin/bash

#cd /home/nirodha/WSO2/ARTIFACTS/qa-artifacts/app-server/createload/xsds
x=2
while test $x != 100
do
cp seq1.xml ./seqs.xml
sed 's/seq1/seq'$x'/g' ./seqs.xml > ./seq${x}.xml
x=`expr $x + 1`
done

