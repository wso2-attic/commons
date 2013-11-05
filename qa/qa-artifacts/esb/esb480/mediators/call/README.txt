Requests should be sent as :

1) For None_Endpoint.xml :
ant stockquote -Daddurl=http://localhost:9000/services/SimpleStockQuoteService  -Dtrpurl=http://localhost:8280/services/StockQuoteProxy

For Registry, Defined endpoint and xpath endpoint :
ant stockquote -Dtrpurl=http://localhost:8280/services/StockQuoteProxy


2) To Test the Request Format type :
(ResquesrType_Pox_Rest.xml)

1. Up a tcpmon which listens to port 9001 and target port as 9000.
2. Change the format as format="pox" or format="rest" and check the output in the tcpmon.


3) To Test Force HTTP 1.0 outgoing messages :

1. Up a tcpmon which listens to port 9001 and target port as 9000. You will be able to see HTTP 1.0


4) To test disable chunking :

1. Up a tcpmon which listens to port 9001 and target port as 9000. You will be able to see content length (value)

5) To Test ServiceURL and action - Dynamically set properties, send the following Soap request to "http://localhost:8280/services/StockQuoteProxy" endpoint,
 
<?xml version='1.0' encoding='UTF-8'?>
   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
      <soapenv:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
         <wsa:To>http://localhost:9000/services/SimpleStockQuoteService</wsa:To>
         <wsa:MessageID>urn:uuid:3b018648-0556-4333-9979-6371c6115dea</wsa:MessageID>
         <wsa:Action>urn:getQuote</wsa:Action>
      </soapenv:Header>
      <soapenv:Body>
         <m0:getQuote xmlns:m0="http://services.samples">
            <m0:request>
               <m0:symbol>IBM</m0:symbol>
            </m0:request>
         </m0:getQuote>
      </soapenv:Body>
   </soapenv:Envelope>


6) To Test In Only / Out_Only 202 invocation send the following request to "http://localhost:8280/services/StockQuoteProxy" endpoint,

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://services.samples" xmlns:xsd="http://services.samples/xsd">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:placeOrder>
         <!--Optional:-->
         <ser:order>
            <!--Optional:-->
            <xsd:price>10</xsd:price>
            <!--Optional:-->
            <xsd:quantity>100</xsd:quantity>
            <!--Optional:-->
            <xsd:symbol>WSO2</xsd:symbol>
         </ser:order>
      </ser:placeOrder>
   </soapenv:Body>
</soapenv:Envelope>
