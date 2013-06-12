=====================================================================================

1. Setup Activemq with WSO2 ESB

2. Create the two proxy services in the ESB according to the configurations

3. Deactivate the 'StockQuoteJMSConsumerProxy' so that then only you can view message
s in the activemq queue 

4. Send the defined two soap messages to the 'StockQuoteJMSProducerProxy' by the jmet
er configuration

5. You will see at the activemq queue there is one message with the Message Details a
s

{name3=value3, name1=value1, name2=value2}

=====================================================================================
