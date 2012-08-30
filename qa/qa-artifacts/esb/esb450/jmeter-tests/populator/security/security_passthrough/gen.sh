#!/bin/bash

for c in `seq 2 100`; 
do cp PassthroughSecurityProxy_1.xml PassthroughSecurityProxy_$c.xml ; 
 sed -i  "s/PassthroughSecurityProxy_1/PassthroughSecurityProxy_$c/g" PassthroughSecurityProxy_$c.xml ; 
done
