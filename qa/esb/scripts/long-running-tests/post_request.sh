#!/bin/bash

#set -x

while true; do java -jar benchmark.jar -p $1 -n $2 -c $3 -k -H "SOAPAction: urn:getQuote" -T "application/x-www-form-urlencoded"  http://10.100.4.100:7000/; sleep 10; done


