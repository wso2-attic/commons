#!/bin/sh

#sample server script which starts a wso2 server and monitor its CPU and Memory usages
command=$1

export JAVA_HOME="/usr/lib/jvm/java-6-sun-1.6.0.15"

if [ "$command" = "start" ] ; then
	sh bin/wso2server.sh --start &
	sleep 20
	input="WSO2Carbon.pid"
	server_pid=`awk '(NR == 1){print} (NR > 2) {$1=$1-1; print}' $input`
	server_pid=`expr $server_pid + 2`
	top -b -d 03.00 -p $server_pid >cpu.txt &
	top_pid=`pidof top`
	echo "$top_pid\n" >top.pid

elif [ "$command" = "stop" ] ; then
	input="top.pid"
	top_pid=`awk '(NR == 1){print} (NR > 2) {$1=$1-1; print}' $input`
	kill $top_pid
	sh bin/wso2server.sh --stop
	rm top.pid
fi
