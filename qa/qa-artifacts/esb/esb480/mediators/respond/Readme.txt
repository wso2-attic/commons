1. Deploy stockquote service
2. Use configuration1.xml
3. send request: 
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://services.samples" xmlns:xsd="http://services.samples/xsd">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getQuote>
         <!--Optional:-->
         <ser:request>
            <!--Optional:-->
            <xsd:symbol>WSO2</xsd:symbol>
         </ser:request>
      </ser:getQuote>
   </soapenv:Body>
</soapenv:Envelope>

Response:
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
   <soapenv:Body>
      <ns:getQuoteResponse xmlns:ns="http://services.samples">
         <ns:return xsi:type="ax21:GetQuoteResponse" xmlns:ax21="http://services.samples/xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <ax21:change>4.253710068646093</ax21:change>
            <ax21:earnings>-8.353811081191068</ax21:earnings>
            <ax21:high>189.5728726879127</ax21:high>
            <ax21:last>184.900188644937</ax21:last>
            <ax21:lastTradeTimestamp>Wed Nov 06 12:00:22 IST 2013</ax21:lastTradeTimestamp>
            <ax21:low>-182.37250579402917</ax21:low>
            <ax21:marketCap>4.205646047918672E7</ax21:marketCap>
            <ax21:name>WSO2 Company</ax21:name>
            <ax21:open>-182.7279537855064</ax21:open>
            <ax21:peRatio>-19.09375108102498</ax21:peRatio>
            <ax21:percentageChange>2.0821280163306373</ax21:percentageChange>
            <ax21:prevClose>204.29627934897417</ax21:prevClose>
            <ax21:symbol>WSO2</ax21:symbol>
            <ax21:volume>8812</ax21:volume>
         </ns:return>
      </ns:getQuoteResponse>
   </soapenv:Body>
</soapenv:Envelope>


Request2: 
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

Response 2: 
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://services.samples/xsd" xmlns:ser="http://services.samples">
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

Note: Respond mediator send the request back. 



---------------------------------------------------------------------------------------------------------------------------------------------------------------------


