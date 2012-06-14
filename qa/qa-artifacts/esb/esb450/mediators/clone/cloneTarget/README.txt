How to test scenarios

Scenario - soapActionWithToTest
===============================
1. Use the synapse configuration given in soapActionWithToTest.xml
2. Using the following command, send a request using javabench

java -jar benchmark.jar -p StockQuoteRequest_getQuote.xml -n 1 -c 1 -k -H "" -T "text/xml; charset=UTF-8" "http://localhost:8281/"

3. You can find the StockQuoteRequest_getQuote.xml at resources collection



Scenario - setSoapActionTest
============================
1. Use the synapse configuration given in setSoapActionTest_synapse.xml
2. Using the following command, send a request using javabench

java -jar benchmark.jar -p StockQuoteRequest_add.xml -n 1 -c 1 -k -H "" -T "text/xml; charset=UTF-8" "http://localhost:8281/"

3. You can find the StockQuoteRequest_add.xml at resources collection
