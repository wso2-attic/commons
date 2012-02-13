<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://org.wso2.wsf/tools">
    <xsl:output method="html"/>

    <xsl:template match="wizard-resources">
        <fieldset style="border:none;">
            <div>
                <h2>WSDLView</h2>
            </div>
            <div id="formset">
                <form id="wsdlviewjarupload" method="post" name="wsdlviewjarupload"
                      enctype="multipart/form-data"
                      target="globalIFrame">
                    <fieldset>
                 	<legend>Step 1: Add Resources</legend>
		       <div>
                            <xsl:attribute name="id">wsdlViewResourceDivId</xsl:attribute>
                            <label>Resource(s)</label>
                            <input type="file" id="jarResourceWSDLView" name="jarResourceWSDLView" size="50"/>
                            <input type="button" value="+">
                                <xsl:attribute name="onClick">
                                    javascript:wso2.tool.WSDLView.addLibraryFileuplod(document.getElementById("wsdlViewResourceDivId"));
                                    return false;
                                </xsl:attribute>
                            </input>
                        </div>
                        <div class="buttonrow">
                            <input type="button" value="Next &gt;">
                                <xsl:attribute name="onClick">
                                    javascript:wso2.tool.WSDLView.toWizard2();
                                    return false;
                                </xsl:attribute>
                            </input>
                        </div>
                    </fieldset>
                </form>
            </div>
        </fieldset>
    </xsl:template>
</xsl:stylesheet>
