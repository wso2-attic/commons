#!/bin/sh
# ----------------------------------------------------------------------------
#
# Starting Sample Axis2 server
#
# ----------------------------------------------------------------------------
echo "Axis2 server Stating.."
cd $2/samples/axis2Server/
mkdir $2/samples/axis2Server/repository/logs
mkdir $2/samples/axis2Server/repository/logs/testLogs
mkdir $2/samples/axis2Server/repository/logs/testLogs/$1
if [ $3 = default ]
then
./axis2server.sh > $2/samples/axis2Server/repository/logs/testLogs/$1/axis2server-$3.log 2>&1 &
else
./axis2server.sh -http $4 -https $5 -name $3 > $2/samples/axis2Server/repository/logs/testLogs/$1/axis2server-$3.log 2>&1 &
fi
echo $! >> $2/samples/axis2Server/repository/logs/testLogs/$1/axis2servers-$3.pid