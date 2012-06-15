How to test scenarios

Scenario - Target which has both soapAction and To values
=========================================================
1. Use the synapse configuration given in soapActionWithToTest.xml
2. Using the following command, send a request using javabench

java -jar benchmark.jar -p StockQuoteRequest_getQuote.xml -n 1 -c 1 -k -H "" -T "text/xml; charset=UTF-8" "http://localhost:8281/"

3. You can find the StockQuoteRequest_getQuote.xml at resources collection



Scenario - Setting a new soapAction different to what is sent with the incoming message
=======================================================================================
1. Use the synapse configuration given in setSoapActionTest_synapse.xml
2. Using the following command, send a request using javabench

java -jar benchmark.jar -p StockQuoteRequest_add.xml -n 1 -c 1 -k -H "" -T "text/xml; charset=UTF-8" "http://localhost:8281/"

3. You can find the StockQuoteRequest_add.xml at resources collection



Scenario - A target sequence which has a referring sequence (defined withing ESB) with an anonymous endpoint 
==========================================================================================================
1. Use the configuration available in refSequence_InlineEndpoint_synapse.xml
2. Using the command mentioned above, send a request


Scenario - A target sequence which refers sequences that are saved in configuration and governance registry spaces
===================================================================================================================
1. Use the configuration available in refConfigAndGoveSequence_synapse.xml
2.  Using the command mentioned above, send a request




