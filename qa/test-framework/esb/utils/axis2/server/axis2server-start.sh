#!/bin/sh
# ----------------------------------------------------------------------------
#
# Starting Sample Axis2 server
#
# ----------------------------------------------------------------------------
echo "Sample Axis2 server Stating.."
cd $2/samples/axis2Server/
./axis2server.sh > $3/../../results/test-cases/test-case-$1/axis2server.log 2>&1 &
echo $! >> $3/../../results/test-cases/test-case-$1/axis2servers.pid
