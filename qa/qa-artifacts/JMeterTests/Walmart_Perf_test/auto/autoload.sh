#!/bin/bash

i=0

conc[0]=50
conc[1]=100
conc[2]=250
conc[3]=500
conc[4]=750
conc[5]=1000
conc[6]=1250


loop[0]=9000
loop[1]=4500
loop[2]=1800
loop[3]=900
loop[4]=600
loop[5]=450
loop[6]=360


cd ./jmx
while test $i != 7 
do 
sed 's/_loop_/'${loop[$i]}'/g' walmartstratosNormalThreadG.jmx > ./jmtest.jmx
sed 's/_con_/'${conc[$i]}'/g' jmtest.jmx > jmtest_${conc[$i]}.jmx
rm jmtest.jmx
cd ../apache-jmeter-2.6/bin/
./jmeter -n -t ../../jmx/jmtest_${conc[$i]}.jmx -l ../../jtl/results_${conc[$i]}.jtl
i=`expr $i + 1`
cd ../../jmx
done










