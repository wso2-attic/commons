<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
	xmlns:m0="http://service.carbon.wso2.org"
	xmlns:ax21="http://services.samples/xsd"
	exclude-result-prefixes="m0 ax21 fn">
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
  <xsl:apply-templates select="//m0:echoStringResponse" /> 
</xsl:template>
  
<xsl:template match="m0:echoStringResponse">

<m:echoStringResponse xmlns:m="http://service.carbon.wso2.org">
	<m:return>Hi-Response</m:return>
</m:echoStringResponse>

</xsl:template>
</xsl:stylesheet>
