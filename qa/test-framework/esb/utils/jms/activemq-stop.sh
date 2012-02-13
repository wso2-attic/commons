#!/bin/sh
# ----------------------------------------------------------------------------
#
#  Stoping ActiveMQ Server
#
# ----------------------------------------------------------------------------
echo "Stoping ActiveMQ Server..."
cat ./../../results/test-cases/test-case-$1/activemq.pid | xargs -i ./kill-process-tree.sh {}
echo "ActiveMQ Server Killed!"

