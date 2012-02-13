#!/bin/sh
# ----------------------------------------------------------------------------
#
# Build Sample Axis2 client
#
# ----------------------------------------------------------------------------
echo "Building Sample Axis2 client.."
thisDir=`pwd`
cd $1/samples/axis2Client;
ant > $thisDir/../../../results/client-build/axis2client-build.log
