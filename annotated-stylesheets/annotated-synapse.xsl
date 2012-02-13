<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:syn="http://ws.apache.org/ns/synapse"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="syn">
	<!-- 
		Created by: Jonathan Marsh <jonathan@wso2.com>
		6 November 2006
	-->
	
	<xsl:output method="html" indent="yes" encoding="UTF-8"/>
		
	<!-- Mozilla doesn't support the namespace axis, which makes simulating namespace declarations
	problematic.  At least we can try alternate reconstruction methods if we know the functionality
	isn't there.  -->
	<xsl:variable name="supports-namespace-axis" select="count(/*/namespace::*) &gt; 0"/>
	
	<!-- ===  Main  ========================================
		Main template for the wsdl document
	-->
	<xsl:template match="/">
		<html>
			<head>
				<title>Synapse configuration</title>
				<style type="text/css"><![CDATA[
body {font: "Lucida Grande","Lucida Sans","Lucida Sans Unicode","trebuchet ms",verdana,sans-serif}
.root {padding-left:5em; text-indent:-5em}
.top-level {padding-left: 6em; padding-bottom:1em; text-indent:-5em}
.second-level {padding-left: 6em; padding-bottom:1em; text-indent:-4em}
.indent {margin-left:1em}
.double-indent {margin-left:2em}
.autolist {font-size:smaller; padding-left:.5em; padding-bottom:1em}
.trivialText {color:gray}

td {vertical-align:top; font: 80%/1.3 "Lucida Grande","Lucida Sans","Lucida Sans Unicode","trebuchet ms",verdana,sans-serif}
.sidebar        {width:17em; overflow:hidden; padding-bottom:1em;margin-bottom:2px}
.arrow          {border-top: 1px dashed black; width:2em; position:relative; top:.5em}
.note           {background-color:rgb(255,255,210); text-align:right; border: 1px dashed black; padding:.5em}
.registry       {background-color:rgb(255,242,210); text-align:right; border: 1px dashed black; padding:.5em}
.definitions    {background-color:rgb(255,242,210); text-align:right; border: 1px dashed black; padding:.5em} 
.proxies        {background-color:rgb(255,242,210); text-align:right; border: 1px dashed black; padding:.5em} 
.rules          {background-color:rgb(255,226,178); text-align:right; border: 1px dashed black; padding:.5em} 
.sequence       {background-color:rgb(225,237,246); text-align:right; border: 1px dashed black; padding:.5em} 
.set-property   {background-color:rgb(225,237,246); text-align:right; border: 1px dashed black; padding:.5em} 
.endpoint       {background-color:rgb(225,237,246); text-align:right; border: 1px dashed black; padding:.5em} 
.mediator       {background-color:rgb(225,246,235); text-align:right; border: 1px dashed black; padding:.5em}
.sidebar-title  {font-size:115%} 
.sidebar-title-highlight {font-weight:bold}
.sidebar-text   {font-size:85%; padding-top:.3em}
.referenced-item {}
.referenced-list {}

#index {margin-top:1em; margin-bottom:1em} 

.markup {color:gray}
.markup-element {color:gray; text-indent:-2em; }
.markup-extension-element {color:navy}
.markup-attribute {color:gray}
.markup-extension-attribute {color:navy}
.markup-attribute-value {}
.markup-name-attribute-value {font-weight:bold}
.markup-namespace {color:purple}
.markup-namespace-uri {color:purple}
.markup-text-content  {margin-left:-4em; text-indent:0em}
.markup-comment  {color:green}
]]>
				</style>
			</head>
			<body>
				<!-- use a table to ensure sidebars longer than their associated tag don't run into the following sidebar.  
					CSS would be preferable (progressive rendering would be improved) if there were a way to do this (I can't find one.) -->
				<table cellpadding="0" cellspacing="0">
					<xsl:apply-templates select="syn:synapse"/>
				</table>
				<p/>
				<hr/>
				<table cellpadding="0" cellspacing="0" id="index">
					<xsl:call-template name="generate-index"/>
				</table>
			</body>
		</html>
	</xsl:template>
	<!-- ===  Elements  ========================================
		The following templates format elements of various flavors
		(syn:synapse, children of syn:synapse, grandchildren etc. of syn:synapse, and extension elements)
	-->
	<xsl:template match="syn:synapse">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td class="sidebar">
					<xsl:call-template name="index-reference"/>
				</td>
				<td><div class="arrow">&#160;</div></td>
				<td class="root">
					<div>
						<xsl:call-template name="element-start"/>
					</div>
				</td>
			</tr>
			<xsl:apply-templates />
			<tr>
				<td colspan="2"/>
				<td class="root">
					<div>
						<xsl:call-template name="element-end"/>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="syn:synapse/syn:*">
		<tr>
			<td colspan="2"/>
			<td class="top-level">
				<div>
					<xsl:call-template name="element-start"/>
				</div>
			</td>
		</tr>
		<xsl:apply-templates />
		<tr>
			<td colspan="2"/>
			<td class="top-level">
				<xsl:call-template name="element-end"/>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="syn:synapse/syn:*/syn:* ">
		<xsl:variable name="identifier">
			<xsl:if test="@name">
				<xsl:text>_</xsl:text>
				<xsl:value-of select="local-name()"/>
				<xsl:text>_</xsl:text>
				<xsl:value-of select="@name"/>
			</xsl:if>
			<xsl:if test="parent::syn:rules">
				<xsl:text>_mediator_</xsl:text>
				<xsl:value-of select="local-name()"/>
			</xsl:if>
		</xsl:variable>
		<tr>
			<xsl:choose>
				<xsl:when test="not(self::tbd)">
					<xsl:variable name="genericName">
						<xsl:choose>
							<xsl:when test="parent::syn:rules">
								<xsl:text>mediator</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="local-name()"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<td class="sidebar">
						<div class="{$genericName} sidebar-title">
							<xsl:if test="$identifier">
								<xsl:attribute name="id">
									<xsl:value-of select="$identifier"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="parent::syn:rules">
									<span class="sidebar-title-highlight">
										<xsl:value-of select="local-name()"/>
									</span>
									<xsl:text> 	mediator</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<span class="sidebar-title-highlight">
										<xsl:value-of select="@name"/>
									</span>
									<xsl:text> </xsl:text>
									<xsl:value-of select="local-name()"/>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:call-template name="referenced-by"/>
						</div>
					</td>
					<td><div class="arrow">&#160;</div></td>
				</xsl:when>
				<xsl:otherwise>
					<td colspan="2"/>
				</xsl:otherwise>
			</xsl:choose>
			<td>
				<div class="second-level">
					<xsl:call-template name="element-start"/>
				</div>
				<div class="indent">
					<xsl:apply-templates />
				</div>
				<div class="second-level">
					<xsl:call-template name="element-end"/>
				</div>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="syn:*">
		<div class="indent">
			<div class="indent">
				<xsl:call-template name="element-start"/>
			</div>
			<xsl:apply-templates/>
			<div class="indent">
				<xsl:call-template name="element-end"/>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="syn:synapse/*[not(self::syn:*)]">
		<!-- Top-level extension elements -->
		<tr>
			<td colspan="2"/>
			<td class="top-level">
				<div class="extension">
					<xsl:call-template name="element-start">
						<xsl:with-param name="class" select="'markup-extension-element'"/>
					</xsl:call-template>
				</div>
				<xsl:apply-templates />
				<div class="extension">
					<xsl:call-template name="element-end">
						<xsl:with-param name="class" select="'markup-extension-element'"/>
					</xsl:call-template>
				</div>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="*">
		<!-- If we've got to here, we're dealing with non-top-level extension elements.  -->
		<div class="indent">
			<div class="extension">
				<xsl:call-template name="element-start">
					<xsl:with-param name="class" select="'markup-extension-element'"/>
				</xsl:call-template>
			</div>
			<xsl:apply-templates />
			<div class="extension">
				<xsl:call-template name="element-end">
					<xsl:with-param name="class" select="'markup-extension-element'"/>
				</xsl:call-template>
			</div>
		</div>
	</xsl:template>
	<!-- ===  Attributes  =========================================
		The following templates format attributes of various flavors
	-->
	<xsl:template match="syn:*/@ref">
		<xsl:call-template name="attribute">
			<xsl:with-param name="value-class">markup-name-attribute-value</xsl:with-param>
			<xsl:with-param name="reference">
				<xsl:text>#_</xsl:text>
				<xsl:value-of select="local-name(..)"/>
				<xsl:text>_</xsl:text>
				<xsl:value-of select="."/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="syn:*/@id | syn:*/@name">
		<a name="{@id}"/>
		<xsl:call-template name="attribute"/>
	</xsl:template>
	<xsl:template match="@*">
		<xsl:call-template name="attribute"/>
	</xsl:template>
	
	<!-- ===  Text nodes  ========================================
		The following template formats text nodes
	-->
	<xsl:template match="text()[normalize-space(.) != '']">
		<div class="markup-text-content">
			<xsl:value-of select="."/>
		</div>
	</xsl:template>
	
	<!-- ===  Comments  ========================================
		The following template formats comment nodes
	-->
	<xsl:template match="syn:synapse/comment() | syn:synapse/syn:*/comment()">
		<tr>
			<td colspan="2" />
			<td class="top-level">
				<div class="markup-comment indent">
					<xsl:text>&lt;!--</xsl:text>
					<xsl:value-of select="."/>
					<xsl:text>--&gt;</xsl:text>
				</div>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="comment()">
		<div class="markup-comment double-indent">
			<xsl:text>&lt;!--</xsl:text>
			<xsl:value-of select="."/>
			<xsl:text>--&gt;</xsl:text>
		</div>
	</xsl:template>
	
	<!-- ===  Library templates  ========================================
		Library of useful named templates
	-->
	
	<xsl:template name="attribute">
		<xsl:param name="value-class" select="'markup-attribute-value'"/>
		<xsl:param name="reference"/>
		<xsl:param name="native-attribute" select="parent::syn:* and namespace-uri(.)=''"/>
		<xsl:text> </xsl:text>
		<span>
			<xsl:choose>
				<xsl:when test="$native-attribute"><xsl:attribute name="class">markup-attribute</xsl:attribute></xsl:when>
				<xsl:otherwise><xsl:attribute name="class">markup-extension-attribute</xsl:attribute></xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="name(.)"/>
		</span>
		<span class="markup">
			<xsl:text>="</xsl:text>
		</span>
		<span class="{$value-class}">
			<xsl:choose>
				<xsl:when test="$reference">
					<a href="{$reference}">
						<xsl:value-of select="."/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="."/>
				</xsl:otherwise>
			</xsl:choose>
		</span>
		<span class="markup">
			<xsl:text>"</xsl:text>
		</span>
	</xsl:template>
	<xsl:template name="namespaces">
		<xsl:variable name="current" select="current()"/>
		<!-- Unfortunately Mozilla doesn't support the namespace axis, need to check for that and simulate declarations -->
		<xsl:choose>
			<xsl:when test="$supports-namespace-axis">
				<!--
					When the namespace axis is present (e.g. Internet Explorer), we can simulate
					the namespace declarations by comparing the namespaces in scope on this element
					with those in scope on the parent element.  Any difference must have been the
					result of a namespace declaration.  Note that this doesn't reflect the actual
					source - it will strip out redundant namespace declarations.
				-->
				<xsl:for-each select="namespace::*[. != 'http://www.w3.org/XML/1998/namespace']">
					<xsl:if test="not($current/parent::*[namespace::*[. = current()]])">
						<span class="markup-namespace">
							<xsl:text> xmlns</xsl:text>
							<xsl:if test="name() != ''">:</xsl:if>
							<xsl:value-of select="name()"/>
							<xsl:text>="</xsl:text>
						</span>
						<span class="markup-namespace-uri">
							<xsl:value-of select="."/>
						</span>
						<span class="markup-namespace">
							<xsl:text>"</xsl:text>
						</span>
					</xsl:if>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<!-- 
					When the namespace axis isn't supported (e.g. Mozilla), we can simulate
					appropriate declarations from namespace elements.
					This currently doesn't check for namespaces on attributes.
					In the general case we can't reliably detect the use of QNames in content, but
					in the case of schema, we know which content could contain a QName and look
					there too.  This mechanism is rather unpleasant though, since it records
					namespaces where they are used rather than showing where they are declared 
					(on some parent element) in the source.  Yukk!
				-->
				<xsl:if test="namespace-uri(.) != namespace-uri(parent::*)">
					<span class="markup-namespace">
						<xsl:text> xmlns</xsl:text>
						<xsl:if test="substring-before(name(),':') != ''">:</xsl:if>
						<xsl:value-of select="substring-before(name(),':')"/>
						<xsl:text>="</xsl:text>
					</span>
					<span class="markup-namespace-uri">
						<xsl:value-of select="namespace-uri(.)"/>
					</span>
					<span class="markup-namespace">
						<xsl:text>"</xsl:text>
					</span>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="element-start">
		<xsl:param name="class" select="'markup-element'"/>
		<span class="markup">&lt;</span>
		<span class="{$class}">
			<xsl:value-of select="name(.)"/>
		</span>
		<xsl:apply-templates select="@*"/>
		<xsl:call-template name="namespaces"/>
		<span class="markup">
			<xsl:if test="not(node())">
				<xsl:text> /</xsl:text>
			</xsl:if>
			<xsl:text>&gt;</xsl:text>
		</span>
	</xsl:template>
	<xsl:template name="element-end">
		<xsl:param name="class" select="'markup-element'"/>
		<xsl:if test="node()">
			<span class="markup">
				<xsl:text>&lt;/</xsl:text>
			</span>
			<span class="{$class}">
				<xsl:value-of select="name(.)"/>
			</span>
			<span class="markup">
				<xsl:text>&gt;</xsl:text>
			</span>
		</xsl:if>
	</xsl:template>


	<xsl:template name="referenced-by">
		<xsl:variable name="target" select="@name"/>
		<xsl:if test="true()">
			<div class="sidebar-text">
				<xsl:variable name="extended-by" select="//syn:*[@ref = $target]"/>
				<xsl:if test="count($extended-by) > 0">
					<div class="referenced-list">Referenced by:</div>
				</xsl:if>
				<xsl:for-each select="$extended-by">
					<xsl:sort select="@ref"/>
					<div class="referenced-item">
						<xsl:call-template name="insert-reference"/>
					</div>
				</xsl:for-each>
			</div>
		</xsl:if>
	</xsl:template>
	<xsl:template name="insert-reference">
		<xsl:for-each select="ancestor-or-self::*[last() - 2]">
			<xsl:choose>
				<xsl:when test="parent::syn:rules">
					<a href="#_mediator_{local-name()}">
						<xsl:value-of select="local-name()"/>
					</a>
					<xsl:text> mediator</xsl:text>
				</xsl:when>	
				<xsl:otherwise>
					<a href="#_{local-name(.)}_{@name}">
						<xsl:value-of select="@name"/>
					</a>
					<xsl:text> </xsl:text>
					<xsl:value-of select="local-name(.)"/>					
				</xsl:otherwise>	
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="componentListItem">
		<xsl:if test="position()>1">
			<xsl:text>, </xsl:text>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="parent::syn:rules">
				<a href="#_mediator_{local-name()}">
					<xsl:value-of select="local-name()"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="#_{local-name()}_{@name}">
					<xsl:value-of select="@name"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="generate-index">
		<xsl:if test="syn:synapse/syn:definitions/syn:set-property">
			<tr>
				<td id="set-property-list">
					<div class="set-property sidebar"><span class="sidebar-title-highlight">Index of set-properties</span></div>
				</td>
				<td><div class="arrow">&#160;</div></td>
				<td class="autolist">
					<xsl:for-each select="syn:synapse/syn:definitions/syn:set-property">
						<xsl:sort select="@name"/>
						<xsl:call-template name="componentListItem"/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="syn:synapse/syn:definitions/syn:sequence">
			<tr>
				<td id="sequence-list">
					<div class="sequence sidebar"><span class="sidebar-title-highlight">Index of seqeuences</span></div>
				</td>
				<td><div class="arrow">&#160;</div></td>
				<td class="autolist">
					<xsl:for-each select="syn:synapse/syn:definitions/syn:sequence">
						<xsl:sort select="@name"/>
						<xsl:call-template name="componentListItem"/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="syn:synapse/syn:definitions/syn:endpoint">
			<tr>
				<td id="endpoint-list">
					<div class="endpoint sidebar"><span class="sidebar-title-highlight">Index of endpoints</span></div>
				</td>
				<td><div class="arrow">&#160;</div></td>
				<td class="autolist">
					<xsl:for-each select="syn:synapse/syn:definitions/syn:endpoint">
						<xsl:sort select="@name"/>
						<xsl:call-template name="componentListItem"/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="syn:synapse/syn:rules/syn:*">
			<tr>
				<td id="mediator-list">
					<div class="mediator sidebar"><span class="sidebar-title-highlight">Index of mediators</span></div>
				</td>
				<td><div class="arrow">&#160;</div></td>
				<td class="autolist">
					<xsl:for-each select="syn:synapse/syn:rules/syn:*">
						<xsl:sort select="@name"/>
						<xsl:call-template name="componentListItem"/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template name="index-reference">
		<div class="note sidebar-text">See the <a href="#index">index</a> of 
			<xsl:variable name="ss1">
				<xsl:if test="syn:definitions/syn:set-property">
					<a href="#set-property-list">set-properties</a>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="ss2">
				<xsl:copy-of select="$ss1"/>
				<xsl:if test="syn:definitions/syn:sequence">
					<xsl:if test="$ss1!=''">, </xsl:if>
					<a href="#sequence-list">sequences</a>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="ss3">
				<xsl:copy-of select="$ss2"/>
				<xsl:if test="syn:definitions/syn:endpoint">
					<xsl:if test="$ss2!=''">, </xsl:if>
					<a href="#endpoint-list">endpoints</a>
				</xsl:if>
			</xsl:variable>
			<xsl:variable name="ss4">
				<xsl:copy-of select="$ss3"/>
				<xsl:if test="syn:rules/syn:*">
					<xsl:if test="$ss3!=''">, </xsl:if>
					<a href="#mediator-list">mediators</a>
				</xsl:if>
			</xsl:variable>
			<xsl:copy-of select="$ss4"/>
			<xsl:text> defined in this file.</xsl:text>
		</div>		
	</xsl:template>
</xsl:stylesheet>
