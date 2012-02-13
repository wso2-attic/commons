<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns1="http://po.services.core.carbon.wso2.org">
<xsl:output method="xml" omit-xml-declaration="yes" indent="no" exclude-result-prefixes="ns1"/>

<xsl:template match="ns1:buyStocks">

<ns1:skcotSyub>
<xsl:for-each select="ns1:order">
<ns1:redro><ns1:lobmys><xsl:value-of select="ns1:symbol"/></ns1:lobmys><ns1:DIreyub><xsl:value-of select="ns1:buyerID"/></ns1:DIreyub><ns1:ecirp><xsl:value-of select="ns1:price"/></ns1:ecirp><ns1:emulov><xsl:value-of select="ns1:volume"/></ns1:emulov></ns1:redro>
</xsl:for-each>
</ns1:skcotSyub>
</xsl:template>        
</xsl:stylesheet>
