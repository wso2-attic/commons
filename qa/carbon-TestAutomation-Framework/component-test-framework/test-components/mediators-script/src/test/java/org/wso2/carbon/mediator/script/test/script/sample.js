function mediate(mc) {
     var symbol = mc.getPayloadXML()..*::Code.toString();
     mc.setPayloadXML(
        <m:getQuote xmlns:m="http://services.samples">
           <m:request>
              <m:symbol>MSFT</m:symbol>
           </m:request>
        </m:getQuote>);
}
