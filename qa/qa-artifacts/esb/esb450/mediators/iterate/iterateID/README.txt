This configuration can be used to verify several parameters of the Iterate mediator

iterateID
==========
This config has nested iterate mediators with different iterateIDs. First the messages are broken into 3 payload sets as the following payloads.

<?xml version='1.0' encoding='utf-8'?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
<soapenv:Body>
         <m0:getQuotes xmlns:m0="http://services.samples">
		 <m0:getQuote>
			    <m0:request>
			       <m0:symbol>SUN9</m0:symbol>
			    </m0:request>
			    <m0:request>
			       <m0:symbol>SUN10</m0:symbol>
			    </m0:request>
		 </m0:getQuote>
	</m0:getQuotes>
</soapenv:Body>
</soapenv:Envelope>

Then, from the second iterate mediator, these messages are again iterated into smaller messages as below

<?xml version='1.0' encoding='utf-8'?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Body>
			 <m0:getQuote xmlns:m0="http://services.samples">
				    <m0:request>
				       <m0:symbol>SUN10</m0:symbol>
				    </m0:request>
			 </m0:getQuote>
	</soapenv:Body>
</soapenv:Envelope>

Then only these messages will be send to the backend server to be processed.

Once processed, the requests will be send back to ESB where the aggregate mediator will do the aggregation depending on the iterateID. First, it would aggregate the messages iterated from iterate mediator with ID - iterate2. Then these messages will be aggregated back to a single message using aggregate mediator with id iterate1.

