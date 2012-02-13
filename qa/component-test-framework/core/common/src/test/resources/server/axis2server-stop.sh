#!/bin/sh
# ----------------------------------------------------------------------------
#
#  Stoping Axis2 Server
#
# ----------------------------------------------------------------------------
echo "Stoping Axis2 Server..."
cat $2/samples/axis2Server/repository/logs/testLogs/$1/axis2servers-$3.pid | xargs -i ./kill-process-tree.sh {}
echo "Axis2 Server Killed!"
