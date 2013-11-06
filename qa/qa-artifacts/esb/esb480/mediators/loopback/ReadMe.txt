1. Start AppServer in offset 1 and get the axis2 service url. 
2. create a proxy and sequence according to given configuraion.xml
3. Request:
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:echo="http://echo.services.core.carbon.wso2.org">
   <soapenv:Header/>
   <soapenv:Body>
      <echo:echoString>
         <!--Optional:-->
         <in>1</in>
      </echo:echoString>
   </soapenv:Body>
</soapenv:Envelope>

Response:
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:echo="http://echo.services.core.carbon.wso2.org">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:echoString xmlns:ser="http://service.carbon.wso2.org">
         <ser:s>a@gmail.com</ser:s>
      </ser:echoString>
   </soapenv:Body>
</soapenv:Envelope>



Note: If you remove, loopbackmediator from the configuration response should be as follows:
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:echo="http://echo.services.core.carbon.wso2.org">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:echoString xmlns:ser="http://service.carbon.wso2.org">
         <ser:s>b@gmail.com</ser:s>
      </ser:echoString>
   </soapenv:Body>
</soapenv:Envelope>

--------------------------------------------------------------------------------------------------------------------------------------------------------

1. Deploy "stockquote" service
2. create a proxy service as per configuration2.xml
3. Request: 
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://services.samples" xmlns:xsd="http://services.samples/xsd">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getQuote>
         <!--Optional:-->
         <ser:request>
            <!--Optional:-->
            <xsd:symbol>CRF</xsd:symbol>
         </ser:request>
      </ser:getQuote>
   </soapenv:Body>
</soapenv:Envelope>

Response: 
Break the flow just after the request and returns the response similar to request. then call the out sequence and print the log. Verify logs:
"value1111 = ==============================="



