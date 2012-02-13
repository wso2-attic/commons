#!/bin/sh
# ----------------------------------------------------------------------------
#
# ActiveMQ start
#
# ----------------------------------------------------------------------------
echo "ActiveMQ Stating.."
nohup $2/bin/activemq > ./../../results/test-cases/test-case-$1/activemq.log 2>&1 &
echo $! >> ./../../results/test-cases/test-case-$1/activemq.pid
#echo "ActiveMQ Stated"
