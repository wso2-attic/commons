<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
                xmlns:ns="http://services.samples/xsd"
                exclude-result-prefixes="ns fn">
<xsl:output method="xml" omit-xml-declaration="yes" indent="no"/>

<xsl:template match="ns:buyStocks">
<m:skcotSyub xmlns:m="http://services.samples/xsd">
<xsl:for-each select="order">
<m:redro><m:lobmys><xsl:value-of select="symbol"/></m:lobmys><m:DIreyub><xsl:value-of select="buyerID"/></m:DIreyub><m:ecirp><xsl:value-of select="price"/></m:ecirp><m:emulov><xsl:value-of select="volume"/></m:emulov></m:redro>
</xsl:for-each>
</m:skcotSyub>
</xsl:template>
</xsl:stylesheet>
