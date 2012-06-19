Steps to test
=============

1. Use the given configuration. Note that I have set <messageCount min="-1" max="2" /> for the first aggregator (which aggregates the messages iterated from the 2nd Iterator). For the second aggregator I have set               <messageCount min="-1" max="2" />. 
2. When sending the message with ../resources/StockQuoteRequestIterateLarge.xml, you should see a response message with, two IBM messages, two MSFT messages and 0 SUN messages.


Similarly, using the same configuration, you can reduce,increment the number of min & max messages and do your tests accordingly.
