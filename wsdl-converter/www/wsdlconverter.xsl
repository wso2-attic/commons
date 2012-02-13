<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://org.wso2.wsf/tools">
    <xsl:output method="html"/>

    <xsl:template match="wsdlconverter">
        <div>
            <h2>WSDL Converter</h2>
        </div>
        <fieldset style="border:none;">
            <div id="formset">
                <div>
                    <form action="fileupload/*"
                          id="wsdl11FormID"
                          method="post"
                          name="wsdlUpload"
                          enctype="multipart/form-data"
                          target="globalIFrame">
                        <xsl:attribute name="onSubmit">
                            javascript:
                            return wso2.tool.WC.validateAndSubmit();
                        </xsl:attribute>
                        <fieldset>
                            <legend>Select WSDL 1.1 document</legend>
                            <div>
                                <label>Select WSDL</label>
                                <input type="file" id="filename" name="filename" size="50"/>
                                <br/>
                                <div class="buttonrow">
                                    <input type="submit" id="wcButtonId" value="Convert WSDL 1.1 --> 2.0 ">
                                        <xsl:attribute name="onclick">
                                            javascript:
                                            return wso2.tool.WC.validateAndSubmit();
                                        </xsl:attribute>
                                    </input>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
                <div>
                    <form method="POST"
                          name="wsdl11URLForm"
                          target="globalIFrame">
                        <xsl:attribute name="onSubmit">
                            javascript:
                            wso2.tool.WC.validateAndSubmitURL();
                            return false;
                        </xsl:attribute>
                        <fieldset>
                            <legend>Enter type WSDL 1.1 document URL</legend>
                            <div>
                                <label>Enter URL</label>
                                <input type="text" id="wsdl11URL" name="wsdl11URL" size="50"/>
                                <br/>
                                <div class="buttonrow">
                                    <input type="submit" id="wcURLButtonId" value="Convert WSDL 1.1 --> 2.0 ">
                                        <xsl:attribute name="onclick">
                                            javascript:
                                            wso2.tool.WC.validateAndSubmitURL();
                                            return false;
                                        </xsl:attribute>
                                    </input>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </fieldset>
    </xsl:template>

    <xsl:template match="tryit">
        <div>
            <h2>Try It</h2>
        </div>
        <fieldset style="border:none;">
            <div id="formset">
                <form id="tryitFormId"
                      method="post"
                      name="tryitWSDLFileUploda"
                      target="globalIFrame">
                    <xsl:attribute name="onSubmit">
                            javascript:
                            wso2.tool.WC.validateAndSubmitTryit();
                            return false;
                        </xsl:attribute>
                    <fieldset>
                        <legend>Enter WSDL 1.1 or 2.0 document location</legend>
                        <div>
                            <label>Enter URL</label>
                            <input type="text" id="tryitFileName" name="tryitFileName" size="50"/>
                            <br/>
                            <div class="buttonrow">
                                <input type="submit" id="tryitButtonId" value="Try It">
                                    <xsl:attribute name="onclick">
                                        javascript:
                                        wso2.tool.WC.validateAndSubmitTryit();
                                        return false;
                                    </xsl:attribute>
                                </input>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </div>
        </fieldset>
    </xsl:template>

</xsl:stylesheet>
