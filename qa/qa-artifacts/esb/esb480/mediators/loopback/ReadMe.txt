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

