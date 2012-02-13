#!/bin/sh
# ----------------------------------------------------------------------------
#
# Build Sample Axis2 server
#
# ----------------------------------------------------------------------------
echo "Building Sample Axis2 server.."
cd $4/samples/axis2Server/src/$2/;
ant > $3/../../results/test-cases/test-case-$1/axis2server-build.log
