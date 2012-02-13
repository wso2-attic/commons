<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
	xmlns:m0="http://service.esb.wso2.org"
	exclude-result-prefixes="m0 fn">
<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

<xsl:template match="/">
  <xsl:apply-templates select="//m0:add" /> 
</xsl:template>
  
<xsl:template match="m0:add">

<m:add xmlns:m="http://service.esb.wso2.org">
		<!--m:x><xsl:value-of select="m0:x * 100 / 50 + 10 - 20"/></m:x>
		<m:y><xsl:value-of select="m0:y * 100 / 50 + 10 - 20"/></m:y-->	
		
		<m:x><xsl:value-of select="m0:x * 120"/></m:x>
		<m:y><xsl:value-of select="m0:y * 120"/></m:y>	
</m:add>

</xsl:template>
</xsl:stylesheet>
