var symbol = mc.getPayloadXML()..*::symbol.toString();
var price = mc.getPayloadXML()..*::last.toString();
mc.setPayloadXML(
        <m:CheckPriceResponse xmlns:m="http://services.samples/xsd">
                <m:Code>{symbol}</m:Code>
                <m:Price>{price}</m:Price>
        </m:CheckPriceResponse>
        );