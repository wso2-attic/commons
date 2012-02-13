#!/bin/sh
# ----------------------------------------------------------------------------
#
# Start Proxy Client
#
# ----------------------------------------------------------------------------
echo "Proxy Client Started.."
cd $3/samples/axis2Client;
ant stockquote -Daddurl=http://localhost:9000/services/SimpleStockQuoteService -Dprxurl=http://localhost:8280/ > $2/../../results/test-cases/test-case-$1/proxy-client.log 
