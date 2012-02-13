require 'rexml/document'
include REXML
newRequest= Document.new '<m:getQuote xmlns:m="http://services.samples"><m:request><m:symbol>...test...</m:symbol></m:request></m:getQuote>'
newRequest.root.elements[1].elements[1].text = $mc.getPayloadXML().root.elements[1].get_text
$mc.setPayloadXML(newRequest)
