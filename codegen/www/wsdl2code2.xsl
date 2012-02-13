<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://org.wso2.wsf/tools">
    <xsl:output method="html"/>

    <xsl:param name="idSuffix">wsf_tools_id</xsl:param>
    <xsl:param name="namePrefix">-</xsl:param>

    <xsl:template match="ns:getCodegenOptionsResponse/return">
        <!--  Link header to be populated by java script -->
        <div>
            <h2>WSDL2Code</h2>
        </div>
        <fieldset style="border:none;">
            <div id="formset">
                <form>
                    <fieldset>
                        <legend>WSDL To Code Options</legend>
                        <table class="styledLeft">
                            <thead>
                                <tr>
                                    <th>Option</th>
                                    <th>Description</th>
                                    <th>Select/Type Value</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:apply-templates select="codegen"/>
                            </tbody>
                        </table>
                        <xsl:call-template name="buttonRow">
                            <xsl:with-param name="parmCodegen" select="codegen"/>
                        </xsl:call-template>

                    </fieldset>
                </form>
            </div>
        </fieldset>
    </xsl:template>

    <xsl:template match="codegen">
        <xsl:apply-templates select="argument"/>
    </xsl:template>

    <xsl:template name="buttonRow">
        <xsl:param name="parmCodegen"/>
        <div class="codegenButtonRaw">
            <input>
                <xsl:attribute name="type">submit</xsl:attribute>
                <xsl:attribute name="value">Generate</xsl:attribute>
                <xsl:attribute name="id">genericCodegenButtonId</xsl:attribute>
                <xsl:attribute name="onclick">
                    var options = new Object();
                    <xsl:for-each select="$parmCodegen/argument">
                        <xsl:variable name="uiType" select="./@uiType"/>
                        <xsl:variable name="name" select="normalize-space(name)"/>
                        <xsl:if test="not($uiType='skip')">
                            <xsl:choose>
                                <xsl:when test="$uiType='text' or $uiType='text-area'">
                                    var obj_<xsl:value-of select="$name"/> = document.getElementById('<xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/>');
                                    if (obj_<xsl:value-of select="$name"/>.value != '') {
                                       options['<xsl:value-of select="$namePrefix"/><xsl:value-of select="$name"/>'] = obj_<xsl:value-of select="$name"/>.value;
                                    }
                                </xsl:when>
                                <xsl:when test="$uiType='check'">
                                    var obj_<xsl:value-of select="$name"/> = document.getElementById('<xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/>');
                                    if (obj_<xsl:value-of select="$name"/>.checked){
                                       options['<xsl:value-of select="$namePrefix"/><xsl:value-of select="$name"/>'] = "";
                                    }
                                </xsl:when>
                                <xsl:when test="$uiType='option'">
                                    var obj_<xsl:value-of select="$name"/> = document.getElementById('<xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/>');
                                    options['<xsl:value-of select="$namePrefix"/><xsl:value-of select="$name"/>'] = obj_<xsl:value-of select="$name"/>[obj_<xsl:value-of select="$name"/>.selectedIndex].value;
                                </xsl:when>
                            </xsl:choose>
                        </xsl:if>
                    </xsl:for-each>
                    wso2.wsas.Tools.startCodegen(options);
                    return false;
                </xsl:attribute>
            </input>
        </div>
    </xsl:template>

    <!-- This will be a table rows-->
    <xsl:template match="argument">
        <xsl:variable name="uiType" select="./@uiType"/>
        <xsl:variable name="uploadFile" select="./@uploadFile"/>
        <xsl:variable name="readOnly" select="./@readOnly" />
        <xsl:if test="not($uiType='skip')">
            <xsl:variable name="description" select="normalize-space(description)"/>
            <xsl:variable name="name" select="normalize-space(name)"/>
            <tr>
                <td>
                    <xsl:value-of select="$namePrefix"/><xsl:value-of select="$name"/>
                </td>
                <td>
                    <xsl:value-of select="$description"/>
                </td>
                <td>
                    <xsl:choose>
                        <xsl:when test="$uiType='text-area'">
                            <textarea>
                                <xsl:attribute name="class">toolsClass</xsl:attribute>
                                <xsl:attribute name="style">height:100px;width:345px</xsl:attribute>
                                <xsl:attribute name="id"><xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/></xsl:attribute>
                            </textarea>
                        </xsl:when>
                        <xsl:when test="$uiType='text'">
                            <input>
                                <xsl:attribute name="class">toolsClass</xsl:attribute>
                                <xsl:attribute name="type">text</xsl:attribute>
                                <xsl:choose>
                                    <xsl:when test="$uploadFile">
                                        <xsl:attribute name="size">37</xsl:attribute>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="size">40</xsl:attribute>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <xsl:attribute name="id"><xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/></xsl:attribute>
                            </input>
                            <xsl:if test="$uploadFile">
                                <input>
                                    <xsl:attribute name="type">button</xsl:attribute>
                                    <xsl:attribute name="style">width:20px;</xsl:attribute>
                                    <xsl:attribute name="value">...</xsl:attribute>
                                    <xsl:attribute name="onclick">codeGenFileUploadeHelper('<xsl:value-of select="$name"/>','<xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/>'); return false;</xsl:attribute>
                                </input>
                            </xsl:if>

                        </xsl:when>
                        <xsl:when test="$uiType='check'">
                             <input>
                                 <xsl:attribute name="class">toolsClass</xsl:attribute>
                                 <xsl:attribute name="type">checkbox</xsl:attribute>
                                 <xsl:attribute name="id"><xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/></xsl:attribute>
                             </input>
                        </xsl:when>
                        <xsl:when test="$uiType='option'">
                            <select>
                                <xsl:attribute name="class">toolsClass</xsl:attribute>
                                <xsl:attribute name="id"><xsl:value-of select="$name"/>_<xsl:value-of select="$idSuffix"/></xsl:attribute>
                                <xsl:for-each select="values/value">
                                     <option>
                                         <xsl:variable name="opValue"><xsl:value-of select="."/></xsl:variable>
                                         <xsl:attribute name="value"><xsl:value-of select="$opValue"/></xsl:attribute>
                                         <xsl:value-of select="$opValue"/>
                                     </option>
                                </xsl:for-each>
                            </select>
                        </xsl:when>
                    </xsl:choose>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>