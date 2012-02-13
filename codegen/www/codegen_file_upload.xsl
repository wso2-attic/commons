<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="codegenFileUpload">
        <div id="formset">
            <form method="post" name="codegenFileUpload" action="fileupload/*"
                  enctype="multipart/form-data" target="globalIFrame">
                <fieldset>
                    <legend>Upload file</legend>
                    <div>
                        <input type="file" size="40" name="codegenFile"/>
                    </div>
                    <div>
                        <input type="submit" value="Submit">
                            <xsl:attribute name="onclick">
                                javascript:
                                var fileName = document.codegenFileUpload.codegenFile.value;
                                if (fileName == '') {
                                    wso2.wsf.Util.alertWarning('Please select a file to upload');
                                    return false;
                                }
                            </xsl:attribute>
                        </input>
                        <input type="button" value="Cancel">
                            <xsl:attribute name="onclick">
                                javascript:
                                var divObj = document.getElementById("divCodegenFileupload");
                                if (divObj) {
                                    divObj.innerHTML = "";
                                    divObj.style.display = "none";
                                }
                                return false;
                            </xsl:attribute>
                        </input>
                    </div>
                </fieldset>
            </form>
        </div>
    </xsl:template>
</xsl:stylesheet>