#!/bin/sh
# ----------------------------------------------------------------------------
#
#  Stoping Axis2 Server
#
# ----------------------------------------------------------------------------
echo "Stoping Axis2 Server..."
cat ./../../../results/test-cases/test-case-$1/axis2servers.pid | xargs -i ./kill-process-tree.sh {}
echo "Axis2 Server Killed!"

