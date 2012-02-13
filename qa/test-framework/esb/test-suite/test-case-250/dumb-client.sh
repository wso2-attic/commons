#!/bin/sh
# ----------------------------------------------------------------------------
#
# Start Dump Client
#
# ----------------------------------------------------------------------------
echo "Dump Client Started.."
cd $3/samples/axis2Client;
ant jmsclient -Djms_type=pox -Djms_dest=dynamicQueues/StockQuoteProxy -Djms_payload=MSFT > $2/../../results/test-cases/test-case-$1/dumb-client.log
