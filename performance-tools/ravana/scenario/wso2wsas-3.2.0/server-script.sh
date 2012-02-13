#!/bin/sh

#sample server script which starts a wso2 server and monitor its CPU and Memory usages
command=$1

export JAVA_HOME="/usr/lib/jvm/java-6-sun-1.6.0.15"

if [ "$command" = "start" ] ; then
	sh bin/wso2server.sh --start --n 2 --cleanRegistry &
	sleep 20
	input="WSO2Carbon.pid"
	server_pid=`awk '(NR == 1){print} (NR > 2) {$1=$1-1; print}' $input`
	server_pid=`expr $server_pid + 2`
	LC_ALL=C sar 1 > cpu.txt &

elif [ "$command" = "stop" ] ; then
	sar_pid=`pidof sar`
    kill $sar_pid
	sh bin/wso2server.sh --stop

fi
