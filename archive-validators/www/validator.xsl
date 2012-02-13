<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://org.wso2.wsf/tools">
    <xsl:output method="html"/>

    <xsl:template match="validatorServicesXML">
        <div>
            <h2>Archive Validator - services.xml</h2>
        </div>
        <fieldset style="border:none;">
            <div id="formset">
                <xsl:call-template name="servicesTemplate"/>
            </div>
        </fieldset>
    </xsl:template>

    <xsl:template match="validatorModuleXML">
        <div>
            <h2>Archive Validator - module.xml</h2>
        </div>
        <fieldset style="border:none;">
            <div id="formset">
                <xsl:call-template name="modulesTemplate"/>
            </div>
        </fieldset>
    </xsl:template>

    <xsl:template name="servicesTemplate">

        <form method="POST" name="aarValidatorDivFormId" id="aarValidatorDivFormId"
              enctype="multipart/form-data"
              action="fileupload/*"
              target="globalIFrame">
            <xsl:attribute name="onSubmit">
                javascript:
                return wso2.tool.AV.validateAndSubmitAAR();
            </xsl:attribute>
            <fieldset>
                <legend>Select an AAR (Services Archive)</legend>
                <label>Select AAR</label>
                <input type="file" id="filenameAARId" name="filenameAARId" size="50"/>
                <br/>
                <div class="buttonrow">
                    <input name="upload" type="submit" value="Validate AAR">
                        <xsl:attribute name="onclick">
                            javascript:
                            return wso2.tool.AV.validateAndSubmitAAR();
                        </xsl:attribute>
                    </input>
                </div>
            </fieldset>
        </form>


        <form id="services_xmlValidatorDivIdFormId" method="post"
              name="services_xmlValidatorDivIdFormId"
              action="fileupload/*"
              enctype="multipart/form-data"
              target="globalIFrame">
            <xsl:attribute name="onSubmit">
                javascript:
                return wso2.tool.AV.validateAndSubmitServicesXml();
            </xsl:attribute>
            <fieldset>
                <legend>Select a services.xml</legend>
                <label>Select services.xml</label>
                <input type="file" id="filenameServices_xmlId" name="filenameServices_xmlId" size="50"/>
                <br/>
                <div class="buttonrow">
                    <input type="submit" value="Validate services.xml">
                        <xsl:attribute name="onclick">
                            javascript:
                            return wso2.tool.AV.validateAndSubmitServicesXml();
                        </xsl:attribute>
                    </input>
                </div>
            </fieldset>
        </form>

    </xsl:template>

    <xsl:template name="modulesTemplate">

        <form id="marValidatorDivFormId" method="post"
              action="fileupload/*"
              name="marValidatorDivFormId"
              enctype="multipart/form-data"
              target="globalIFrame">
            <xsl:attribute name="onSubmit">
                javascript:
                return wso2.tool.AV.validateAndSubmitMAR();
            </xsl:attribute>
            <fieldset>
                <legend>Select a MAR (Module Archive)</legend>
                <label>Select MAR</label>
                <input type="file" id="filenameMARId" name="filenameMARId" size="50"/>
                <br/>
                <div class="buttonrow">
                    <input type="submit" value="Validate MAR">
                        <xsl:attribute name="onclick">
                            javascript:
                            return wso2.tool.AV.validateAndSubmitMAR();
                        </xsl:attribute>
                    </input>
                </div>
            </fieldset>
        </form>


        <div id='module_xmlValidatorDivId' style="display:inline;">
            <form id="module_xmlValidatorDivIdFormId" method="post"
                  name="module_xmlValidatorDivIdFormId"
                  action="fileupload/*"  
                  enctype="multipart/form-data"
                  target="globalIFrame">
                <xsl:attribute name="onSubmit">
                    javascript:
                    return wso2.tool.AV.validateAndSubmitModuleXml();
                </xsl:attribute>
                <fieldset>
                    <legend>Select a module.xml</legend>
                    <label>Select module.xml</label>
                    <input type="file" id="filenameModule_xmlId" name="filenameModule_xmlId" size="50"/>
                    <br/>
                    <div class="buttonrow">
                        <input type="submit" value="Validate module.xml">
                            <xsl:attribute name="onclick">
                                javascript:
                                return wso2.tool.AV.validateAndSubmitModuleXml();
                            </xsl:attribute>
                        </input>
                    </div>
                </fieldset>
            </form>
        </div>

    </xsl:template>
</xsl:stylesheet>