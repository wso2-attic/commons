To verify this scenario, 
1. Use the configuration given in the synapse.xml
2. Using the following command, send a request
java -jar benchmark.jar -p requests/StockQuoteRequestIterateLarger.xml -n 1 -c 1 -H "urn:getQuote" -T "text/xml; charset=UTF-8" "http://evan-ThinkPad:8281/services/aggregateMediatorTestProxy"

3. Get the output response into a file. Then compare the number of messages between the StockQuoteRequestIterateLarger.xml file and the response.

NOTE: a) Reduce the completeCondition timeout value to something around 2-3 seconds and invoke the proxy services. Then when you compare the number of messages, you will notice that the number of messages in the response file is less than the number of messages in the request file. The reason for this is that the aggregation terminates after the timeout expires. 

b) Increase the completeCondition timeout value to ~100 seconds and do the same above. Then, when you compare the request and the response messages, you will notice that the count is equal.
