#!/bin/bash

i=0

conc[0]=50
conc[1]=100
conc[2]=250
conc[3]=500
conc[4]=750
conc[5]=1000
conc[6]=1250

loop[0]=5000
loop[1]=2500
loop[2]=1000
loop[3]=500
loop[4]=334
loop[5]=250
loop[6]=200

cd ./jmx
while test $i != 7
do 
sed 's/_loop_/'${loop[$i]}'/g' walmartstratosNormalThreadG.jmx > ./jmtest.jmx
sed 's/_con_/'${conc[$i]}'/g' jmtest.jmx > jm10test_${conc[$i]}.jmx
rm jmtest.jmx
cd ../apache-jmeter-2.6/bin/
./jmeter -n -t ../../jmx/jm10test_${conc[$i]}.jmx -l ../../jtl/10Tresults_${conc[$i]}.jtl
i=`expr $i + 1`
cd ../../jmx
done
