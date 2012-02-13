#!/bin/sh
# ----------------------------------------------------------------------------
#
# Start ESB
#
# ----------------------------------------------------------------------------
echo "ESB Stating with sample $1.."
$2/bin/wso2esb-samples.sh -sn $1 > ./../../results/test-cases/test-case-$1/esb.log 2>&1 &
echo $! >> ./../../results/test-cases/test-case-$1/esb.pid
#echo "ESB Stated"
