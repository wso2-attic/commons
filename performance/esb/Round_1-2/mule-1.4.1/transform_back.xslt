<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  ~  Licensed to the Apache Software Foundation (ASF) under one
  ~  or more contributor license agreements.  See the NOTICE file
  ~  distributed with this work for additional information
  ~  regarding copyright ownership.  The ASF licenses this file
  ~  to you under the Apache License, Version 2.0 (the
  ~  "License"); you may not use this file except in compliance
  ~  with the License.  You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing,
  ~  software distributed under the License is distributed on an
  ~   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~  KIND, either express or implied.  See the License for the
  ~  specific language governing permissions and limitations
  ~  under the License.
  -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
                xmlns:ns="http://services.samples/xsd"
                exclude-result-prefixes="ns fn">
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>

    <xsl:template match="/">
        
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">        
            <soapenv:Body>
                <xsl:apply-templates select="//ns:getQuoteResponse" />
            </soapenv:Body>
        </soapenv:Envelope>
    </xsl:template>

    <xsl:template match="ns:getQuoteResponse">

        <m:CheckPriceResponse xmlns:m="http://www.apache-synapse.org/test">
            <m:Code><xsl:value-of select="ns:return/ns:symbol"/></m:Code>
            <m:Price><xsl:value-of select="ns:return/ns:last"/></m:Price>
        </m:CheckPriceResponse>

    </xsl:template>
</xsl:stylesheet>
