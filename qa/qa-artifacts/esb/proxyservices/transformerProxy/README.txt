How to test
===========
1. Deploy the https://svn.wso2.org/repos/wso2/trunk/commons/qa/qa-artifacts/app-server/Axis2Service.aar at a server of your choice
2. Start the ESB server with the given synapse.xml
3. Upload the two .xslt files to _system/config collection of registry (this is configurable. But make sure to specify the path in the synapse.xml accordingly)
4. Send the following request using any client
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://www.wso2.org/types">
   <soap:Header/>
   <soap:Body>
      <typ:echoString1>
         <s>hi</s>
      </typ:echoString1>
   </soap:Body>
</soap:Envelope>

5. You should get a response as below if the transformation is successful
<?xml version='1.0' encoding='UTF-8'?>
   <soapenv:Envelope xmlns:soapenv="http://www.w3.org/2003/05/soap-envelope">
      <soapenv:Body>
         <m:echoStringResponse xmlns:m="http://www.wso2.org/types">
            <m:return>Hi-Response</m:return>
         </m:echoStringResponse>
      </soapenv:Body>
   </soapenv:Envelope
