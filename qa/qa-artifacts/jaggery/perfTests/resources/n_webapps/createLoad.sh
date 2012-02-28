#!/bin/bash

cd /home/yumani/Documents/projects/jaggery/perfTest/testrun

x=10
while [$x -eq 100]
do
cp /home/yumani/Documents/projects/jaggery/perfTest/testrun/20 /home/yumani/Documents/projects/jaggery/perfTest/testrun/$x 
cd /home/yumani/Documents/projects/jaggery/perfTest/testrun/$x
for i in 1 2 3 4 5
do
mv array1.war array+i+.war
mv demos1.war demos+i+.war
mv example1.war example+i+.war
mv example1.war example+i+.war
mv htmlTags1.war htmlTags+i+.war
mv json1.war json+i+.war
mv mine1.war mine+i+.war 
mv print1.war print+i+.war
mv request1.war request+i+.war
mv sample_yumani1.war sample_yumani+i+.war
done
let x=x+10
done

