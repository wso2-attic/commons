#!/bin/bash

#sample server script which starts a wso2 server and monitor its CPU and Memory usages
command=$1

if [ "$command" = "start" ] ; then
	LC_ALL=C sar 1 > cpu.txt &

elif [ "$command" = "stop" ] ; then
	sar_pid=`pidof sar`
    kill $sar_pid

fi
