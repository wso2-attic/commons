#!/bin/sh
# ----------------------------------------------------------------------------
#
# Start Smart Client
#
# ----------------------------------------------------------------------------
echo "Smart Client Started.."
cd $3/samples/axis2Client;
ant stockquote -Daddurl=http://localhost:9000/services/SimpleStockQuoteService -Dtrpurl=http://localhost:8280/ > $2/../../results/test-cases/test-case-$1/smart-client.log
