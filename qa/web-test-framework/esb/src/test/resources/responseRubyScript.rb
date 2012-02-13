require 'rexml/document'
include REXML
newResponse = Document.new '<m:CheckPriceResponse xmlns:m="http://services.samples/xsd"><m:Code></m:Code><m:Price></m:Price></m:CheckPriceResponse>'
newResponse.root.elements[1].text = $mc.getPayloadXML().root.elements[1].elements[8].get_text
newResponse.root.elements[2].text = $mc.getPayloadXML().root.elements[1].elements[4].get_text
$mc.setPayloadXML(newResponse)
