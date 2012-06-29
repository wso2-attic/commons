No Arguments
---------------------------------- 
1. Start WSO2 appserver (offset1) and deploy Axis2Service.
2. Use the synapse configuration given in no_arguments.xml
3. Send the given request to http://localhost:8280/services/ProxyPF using Soap UI. [i.e. send an echoString request with an echoInt payload (SOAPAction: "urn:echoString")]


<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://service.carbon.wso2.org">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:echoInt>
         <!--Optional:-->
         <ser:x>11</ser:x>
      </ser:echoInt>
   </soapenv:Body>
</soapenv:Envelope>


 
