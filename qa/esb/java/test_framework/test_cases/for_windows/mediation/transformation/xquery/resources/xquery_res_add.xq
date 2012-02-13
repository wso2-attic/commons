<x><![CDATA[
  declare namespace m0="http://service.esb.wso2.org";
  declare namespace xs="http://www.w3.org/2001/XMLSchema";
  declare variable $return as xs:integer external;

  <m0:addResponse xmlns:m="http://service.esb.wso2.org">
  	<m0:return>{$return+$return+100}</m0:return>
  </m0:addResponse>      
]]></x>
