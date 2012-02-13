<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                xmlns:xdt="http://www.w3.org/2005/xpath-datatypes">
    <xsl:output method="xml"/>
    <!-- root match -->
    <xsl:template match="/">
		<report>
			<xsl:apply-templates/>
		</report>
	</xsl:template>
	<!-- template for service -->
	<xsl:template match="service">
		<!-- No Name-->
		<xsl:choose>
			<xsl:when test="not(@name)">
				<error>No name found in service!</error>
			</xsl:when>
			<xsl:otherwise>
				<!-- No description -->
				<xsl:if test="not(Description)">
					<warning>No description found in service!</warning>
				</xsl:if>
				<info>Found <xsl:value-of select="count(operation)"/> Operation elements in service <xsl:value-of select="@name"/>
				</info>
				<info>Found <xsl:value-of select="count(parameter)"/> Parameters elements in service <xsl:value-of select="@name"/>
				</info>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- template for services -->
	<xsl:template match="serviceGroup">
		<info>Service Group Found</info>
		<!-- No service element!! -->
		<xsl:if test="not(service)">
			<error>No nested service elements found!</error>
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:template>
	<!-- template for parameter -->
	<xsl:template match="parameter">
		<!-- No name for the parameter-->
		<xsl:if test="not(@name)">
			<error>No name attribute found in parameter!</error>
		</xsl:if>
		<!-- the locked attribute is missing -->
		<xsl:if test="not(@locked)">
			<warning> No locked attribute found in parameter with name <xsl:value-of select="@name"/>!</warning>
		</xsl:if>
	</xsl:template>
	<!-- template for operation -->
	<xsl:template match="operation">
		<!-- no name for the operation -->
		<xsl:if test="not(@name)">
			<error>No name attribute found in Operation!</error>
		</xsl:if>
		<!-- no mep for the operation -->
		<xsl:if test="not(@mep)">
			<warning> No mep attribute found in Operation <xsl:value-of select="@name"/></warning>
		</xsl:if>
		<!-- no scope for the operation -->
		<xsl:if test="not(@scope)">
			<warning>No scope attribute found in Operation <xsl:value-of select="@name"/>. Default will be picked.</warning>
		</xsl:if>
		<!-- no message receiver  for the operation -->
		<xsl:if test="not(messageReceiver)">
			<warning> No messageReceiver element found in Operation <xsl:value-of select="@name"/>Default will be picked.</warning>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
