#!/bin/bash

for c in `seq 2 100`; 
do cp SecurityTerminate_1.xml SecurityTerminate_$c.xml ; 
 sed -i  "s/SecurityTerminate_1/SecurityTerminate_$c/g" SecurityTerminate_$c.xml ; 
done
