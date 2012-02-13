/**
 * This js has the tools related js functions
 */

if (typeof wso2 == "undefined") {
    var wso2 = {};
}
if (typeof wso2.tool == "undefined") {
    wso2.tool = {};
}

wso2.tool.WC = {
    validateAndSubmit : function() {
        try {
            var objForm = document.getElementById('wsdl11FormID');
            var inputObj = document.getElementById('filename');
            if (inputObj.value == "") {
                wso2.wsf.Util.alertWarning('Please Select a WSDL of Version 1.1');
                return false;
            }
            objForm.action = "fileupload/*";
            FileExcutor.execute = this.wcserviceClient;
            objForm.submit();
            return true;
        } catch(e) {
              wso2.wsf.Util.alertWarning('You have selected an invalid WSDL document.');
        }
    },

    validateAndSubmitURL : function() {
        var inputObj = document.getElementById('wsdl11URL');
        if (inputObj.value == "") {
            wso2.wsf.Util.alertWarning('WSDL 1.1 URL cannot be empty.');
            return false;
        }
        var bodyXml = '<req:convertFromURL xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<url><![CDATA[' + inputObj.value + ']]></url>\n' +
                      '</req:convertFromURL>\n';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "WSDLConverter" ;
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:convertFromURL", bodyXml, wcserviceClientCallback, [1]);

    },

    validateAndSubmitTryit : function() {
        var inputObj = document.getElementById('tryitFileName');
        if (inputObj.value == "") {
            wso2.wsf.Util.alertWarning('Please Select a WSDL of Version 1.1 or 2.0');
            return false;
        }
        var bodyXml = '<req:generateAJAXApp xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<url><![CDATA[' + inputObj.value + ']]></url>\n' +
                      '<hostName><![CDATA['+ HOST+']]></hostName>\n' +
                      '</req:generateAJAXApp>\n';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "GenericAJAXClient" ;
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:generateAJAXApp", bodyXml, wcserviceClientCallback, [2]);

    },

    showWC : function() {
        this.showHelper("wsdlconverter","divWSDLConverterTool");
    },

    showTryit : function() {
        this.showHelper("tryit", "divTryitTool");
        document.tryitWSDLFileUploda.tryitFileName.focus();
    },

    showHelper : function(documentName, divName) {
        var tmpTransformationNode;
        if (window.XMLHttpRequest && !wso2.wsf.Util.isIE()) {
            tmpTransformationNode =
            document.implementation.createDocument("", documentName, null);
        } else if (window.ActiveXObject) {
            tmpTransformationNode = new ActiveXObject("Microsoft.XmlDom");
            var sXml = "<"+documentName+"></"+documentName+">";
            tmpTransformationNode.loadXML(sXml);
        }
        var objDiv = document.getElementById(divName);
        var xsltHelperObj = new wso2.wsf.XSLTHelper();
        xsltHelperObj.transform(objDiv, tmpTransformationNode, "extensions/tools_wc/xslt/wsdlconverter.xsl", true);
        wso2.wsf.Util.showOnlyOneMain(objDiv);
    },

    wcserviceClient : function(uuid) {
        var bodyXml = '<req:convertRequest xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<fileId>' + uuid + '</fileId>\n' +
                      '</req:convertRequest>\n';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "WSDLConverter" ;
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:convert", bodyXml, wcserviceClientCallback);
    }
}

function wcserviceClientCallback() {
    var data = this.req.responseXML;
    var responseTextValue = data.getElementsByTagName("return")[0].firstChild.nodeValue;
    var openBaseURL = "http://" + HOST +
                      (HTTP_PORT != 80 ? (":" + HTTP_PORT + ROOT_CONTEXT)  : ROOT_CONTEXT);
    window.open(openBaseURL + responseTextValue);    
    if (this.params) {
        if (this.params == 1) {
            wso2.tool.WC.showWC();
        } else if (this.params == 2) {
            wso2.tool.WC.showTryit();
        }
    }
}