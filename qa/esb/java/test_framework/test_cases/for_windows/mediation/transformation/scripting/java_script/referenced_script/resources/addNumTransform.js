<x><![CDATA[
  function transformRequest(mc) {
     var x = mc.getPayloadXML()..*::x.toString();
     var y = mc.getPayloadXML()..*::y.toString();
     mc.setPayloadXML(
        <ns1:add xmlns:ns1="http://add.math.com">
			<ns1:x>{x}</ns1:x>
			<ns1:y>{y}</ns1:y>
        </ns1:add>);
  }

  function transformResponse(mc) {
     var response = mc.getPayloadXML()..*::response.toString();
     mc.setPayloadXML(
        <ns1:addResponse xmlns:ns1="http://add.math.com">
			<ns1:return>{response}</ns1:return>
        </ns1:addResponse>);
  }
]]></x>
