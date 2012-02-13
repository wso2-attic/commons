<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:data="mailto:jonathan@wso2.com">
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/">
        <description xmlns="http://www.w3.org/2006/01/wsdl"
            xmlns:wsoap="http://www.w3.org/2006/01/wsdl/soap"
            xmlns:wrpc="http://www.w3.org/2006/01/wsdl/rpc"
            xmlns:xs="http://www.w3.org/2001/XMLSchema"
            xmlns:sig="http://complexSigs.services.wsas.wso2.org/xsd"
            xmlns:c="http://complexSigs.services.wsas.wso2.org"
            targetNamespace="http://complexSigs.services.wsas.wso2.org">
            <documentation> This service tests complex signatures.</documentation>
            <types>
                <xs:schema targetNamespace="http://complexSigs.services.wsas.wso2.org/xsd"
                    attributeFormDefault="qualified" elementFormDefault="unqualified">
                    <xsl:for-each select="//data:data/data:simple">
                        <xsl:variable name="local" select="substring-after(@type,':')"/>
                        <xs:element name="echo{$local}">
                            <xs:complexType>
                                <xs:sequence>
                                    <xs:element name="test{$local}" type="{@type}"/>
                                </xs:sequence>
                            </xs:complexType>
                        </xs:element>
                        <xs:element name="echo{$local}Response">
                            <xs:complexType>
                                <xs:sequence>
                                    <xs:element name="return{$local}" type="{@type}"/>
                                </xs:sequence>
                            </xs:complexType>
                        </xs:element>
                    </xsl:for-each>
                </xs:schema>
            </types>
            <interface name="echoInterface">
                <xsl:for-each select="//data:data/data:simple">
                    <xsl:variable name="local" select="substring-after(@type,':')"/>
                    <operation name="echo{$local}" pattern="http://www.w3.org/2006/01/wsdl/in-out"
                        style="http://www.w3.org/2006/01/wsdl/style/rpc" wrpc:signature="test{$local} #in return{$local} #return">
                        <input element="sig:echo{$local}"/>
                        <output element="sig:echo{$local}Response"/>
                    </operation>
                </xsl:for-each>
            </interface>
            <binding name="echoSOAPBinding" interface="c:echoInterface"
                type="http://www.w3.org/2006/01/wsdl/soap" wsoap:version="1.2"
                wsoap:protocol="http://www.w3.org/2003/05/soap/bindings/HTTP">
                <xsl:for-each select="//data:data/data:simple">
                    <xsl:variable name="local" select="substring-after(@type,':')"/>
                    <operation ref="echo{$local}"/>
                </xsl:for-each>
            </binding>
            <service name="complexSigsService" interface="c:echoInterface">
                <endpoint name="echoEndpoint_http" binding="c:echoSOAPBinding"
                    address="http://192.168.1.53:9762/soap/complexSigs"/>
            </service>
        </description>
    </xsl:template>
</xsl:stylesheet>
