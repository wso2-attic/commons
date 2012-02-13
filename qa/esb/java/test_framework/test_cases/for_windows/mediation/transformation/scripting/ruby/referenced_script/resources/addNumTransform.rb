<x><![CDATA[
require 'rexml/document'
include REXML

def transformRequest(mc)
  newRequest= Document.new '<m:add xmlns:m="http://add.math.com">'<<
            '<m:x></m:x><m:y></m:y></m:add>'

  newRequest.root.elements[1].text = mc.getPayloadXML().root.elements[1].get_text
  newRequest.root.elements[2].text = mc.getPayloadXML().root.elements[1].get_text

  mc.setPayloadXML(newRequest)

end

def transformResponse(mc)
  newResponse = Document.new '<m:addResponse xmlns:m="http://add.math.com"><m:return>' <<
    '</m:return></m:addResponse>'

  puts(mc)
  puts(newResponse)

  response=mc.getPayloadXML().root.get_elements('ns:return')
 
  puts(mc.getPayloadXML())
  puts(mc.getPayloadXML().root)
  puts(mc.getPayloadXML().root.get_elements('ns:return'))
  puts(response)

  newResponse.root.elements[1].text = response[0].get_text

  mc.setPayloadXML(newResponse)

end
]]></x>
