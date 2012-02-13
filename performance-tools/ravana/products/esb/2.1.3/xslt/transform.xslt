<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:m="http://po.services.core.carbon.wso2.org">
<xsl:output method="xml" omit-xml-declaration="yes" indent="no" exclude-result-prefixes="m"/>

<xsl:template match="m:skcotSyub">

<m:buyStocks>
<xsl:for-each select="m:redro">
<m:order><m:symbol><xsl:value-of select="m:lobmys"/></m:symbol><m:buyerID><xsl:value-of select="m:DIreyub"/></m:buyerID><m:price><xsl:value-of select="m:ecirp"/></m:price><m:volume><xsl:value-of select="m:emulov"/></m:volume></m:order>
</xsl:for-each>
</m:buyStocks>
</xsl:template>        
</xsl:stylesheet>
