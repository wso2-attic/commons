Request should be sent as :

For None_Endpoint.xml :
ant stockquote -Daddurl=http://localhost:9000/services/SimpleStockQuoteService  -Dtrpurl=http://localhost:8280/services/StockQuoteProxy

For Registry, Defined endpoint and xpath endpoint :
ant stockquote -Dtrpurl=http://localhost:8280/services/StockQuoteProxy


To Test the Request Format type :
(ResquesrType_Pox_Rest.xml)

1. Up a tcpmon which listens to port 9001 and target port as 9000.
2. Change the format as format="pox" or format="rest" and check the output in the tcpmon.


To Test Force HTTP 1.0 outgoing messages :

1. Up a tcpmon which listens to port 9001 and target port as 9000. You will be able to see HTTP 1.0


To test disable chunking :

1. Up a tcpmon which listens to port 9001 and target port as 9000. You will be able to see content length (value)


