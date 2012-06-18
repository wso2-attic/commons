When executing the given configuration, you should notice the message of the first iterated message as below

<?xml version='1.0' encoding='utf-8'?><soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"><soapenv:Body>
         <m0:getQuotes xmlns:m0="http://services.samples">
		 <m0:getQuote>
				    <m0:request>
				       <m0:symbol>IBM1</m0:symbol>
				    </m0:request>
				    <m0:request>
				       <m0:symbol>IBM2</m0:symbol>
				    </m0:request>
				    <m0:request>
				       <m0:symbol>IBM3</m0:symbol>
				    </m0:request>
				    <m0:request>
				       <m0:symbol>IBM4</m0:symbol>
				    </m0:request>
		 </m0:getQuote>
	</m0:getQuotes>
      </soapenv:Body>
</soapenv:Envelope>

If you set preservePayload=false (you will need to removed the attachPath property values as well), then the message should look like below

<?xml version='1.0' encoding='utf-8'?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Body>
		<m0:getQuotes xmlns:m0="http://services.samples"> ------> Note that the original message root will be omitted and the split element will be added under the SOAP body
		</m0:getQuotes>
		<m0:getQuote xmlns:m0="http://services.samples">
			    <m0:request>
			       <m0:symbol>IBM1</m0:symbol>
			    </m0:request>
			    <m0:request>
			       <m0:symbol>IBM2</m0:symbol>
			    </m0:request>
			    <m0:request>
			       <m0:symbol>IBM3</m0:symbol>
			    </m0:request>	
			    <m0:request>
			       <m0:symbol>IBM4</m0:symbol>
			    </m0:request>
		 </m0:getQuote>
	</soapenv:Body>
</soapenv:Envelope>
