#!/bin/sh
# ----------------------------------------------------------------------------
#
# Start Dump Client
#
# ----------------------------------------------------------------------------
echo "Dump Client Started.."
cd $3/samples/axis2Client;
ant stockquote -Dtrpurl=http://localhost:8280/services/StockQuote > $2/../../results/test-cases/test-case-$1/dumb-client.log
