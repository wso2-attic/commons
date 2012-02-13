<x><![CDATA[
  declare namespace m0="http://service.esb.wso2.org";
  declare namespace xs="http://www.w3.org/2001/XMLSchema";
  declare variable $x as xs:integer external;
  declare variable $y as xs:integer external;

  <m0:add xmlns:m0="http://service.esb.wso2.org">
    <m0:x>{$x+$x+100}</m0:x>
    <m0:y>{$y+$y+100}</m0:y>
   </m0:add>
]]></x>
