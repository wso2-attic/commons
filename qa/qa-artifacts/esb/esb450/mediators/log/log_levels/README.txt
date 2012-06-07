How to run the sample
=====================
1. Start the server with the given synapse.xml
2. Using javabench, invoke the sequence using the command - java -jar benchmark.jar -p request.xml -n 1 -c 1 -k -H "SOAPAction: urn:getQuote" -T "text/xml; charset=UTF-8" http://localhost:8280/
3. The request.xml contains the request payload with custom headers
4. Execute the command and look into the Carbon logs. You should see the following log line

*******************************************************************************************************************************************************************************************
[2012-06-07 14:58:40,097] DEBUG - LogMediator To : http://localhost:9000/services/SimpleStockQuoteService, MessageID : urn:uuid:b68b1e33-292f-4b6f-ba0f-d4228e9b8705, Action : urn:getQuote, 
version : 20, datetime : Wed Dec 23 08:56:00 CST 2009, storeNumber : 110, messageID : 9823749238792342
*******************************************************************************************************************************************************************************************
