var symbol = mc.getPayloadXML()..*::Code.toString();
mc.setPayloadXML(
  <m:getQuote xmlns:m="http://services.samples">
     <m:request>
        <m:symbol>{symbol}</m:symbol>
     </m:request>
  </m:getQuote>
        );