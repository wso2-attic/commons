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


7) To Test Call Mediator with MTOM,

1. Build MTOMSwASampleService in samples.
2. Start axis2server.
3. Use a tcpmon to view the messages which listens to 9001 and targets to 9000.
4. Go to the axis2client and run "ant optimizeclient -Dopt_mode=mtom"

8) To test Fault Sequence,

1. Up tcpmon which listens to 8281 and target 8280.
2.  ant stockquote -Daddurl=http://localhost:9001/services/SimpleStockQuoteService -Dtrpurl=http://localhost:8281/ -Dsymbol=MSFT this will be a successfull request.
3. ant stockquote -Daddurl=http://localhost:9001/services/SimpleStockQuoteService -Dtrpurl=http://localhost:8281/ -Dsymbol=SUN This will reproduces a soap fault.      

    <soapenv:Body>
         <soapenv:Fault>
            <faultcode xmlns:tns="http://www.w3.org/2003/05/soap-envelope">tns:Receiver</faultcode>
            <faultstring>Connection refused or failed for : bogus:9009, IO Exception occured : bogus</faultstring>
         </soapenv:Fault>
      </soapenv:Body>
   </soapenv:Envelope>0

9) To test Soap client rest service, (In this client sends a SOAP message to the ESB, which transforms it to a REST message and sends it to the back-end service)
1. Download, install, and start the WSO2 Application Server. The application server ships with JAX-RS, which contains a set of pure RESTful services, so we will use this as our back-end service.
2. Send a message to the back-end service through the ESB using SoapUI. The service used in this scenario is the getCustomer service, which requires the customer ID to complete its task. A sample SOAP message that is used to achieve this is as follows:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
   <soapenv:Header/>
   <soapenv:Body>
     <getCustomer>
        <id>123</id>
     </getCustomer>
   </soapenv:Body>
</soapenv:Envelope>

3. This message simply represents the relevant method to be invoked and its associated value. Upon successful execution, SoapUI should display the following message in response.
<Customer>
   <id>123</id>
   <name>John</name>
</Customer>

10) To Test Rest Client and Reat Service (In This ESB simply changes the message type of the client into XML and then passes it to the REST service. Once the ESB has received the XML message, it transforms it back into a JSON message and sends it to the client. )

1. Download, install, and start the WSO2 Application Server. The application server ships with JAX-RS, which contains a set of pure RESTful services, so we will use this as our back-end service.
2. Send curl -v -i -H "Accept: application/json" http://localhost:8280/services/CustomerServiceProxy/customers/123
3. It will sends a reply as, 
{"Customer":{"id":123,"name":"John"}}



