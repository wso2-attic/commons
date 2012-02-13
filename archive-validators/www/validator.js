if (typeof wso2 == "undefined") {
    var wso2 = {};
}
if (typeof wso2.tool == "undefined") {
    wso2.tool = {};
}


wso2.tool.AV = {

    showValidatorServicesXML : function() {
        var tmpTransformationNode;
        if (window.XMLHttpRequest && !wso2.wsf.Util.isIE()) {
            tmpTransformationNode =
            document.implementation.createDocument("", "validatorServicesXML", null);
        } else if (window.ActiveXObject) {
            tmpTransformationNode = new ActiveXObject("Microsoft.XmlDom");
            var sXml = "<validatorServicesXML></validatorServicesXML>";
            tmpTransformationNode.loadXML(sXml);
        }
        var objDiv = document.getElementById("divArchivesValidatorServicesXML");
        var xsltHelperObj = new wso2.wsf.XSLTHelper();
        xsltHelperObj.transform(objDiv, tmpTransformationNode, "extensions/tools_av/xslt/validator.xsl", true);
        wso2.wsf.Util.showOnlyOneMain(objDiv);
    },

    showValidatorModuleXML : function() {
        var tmpTransformationNode;
        if (window.XMLHttpRequest && !wso2.wsf.Util.isIE()) {
            tmpTransformationNode =
            document.implementation.createDocument("", "validatorModuleXML", null);
        } else if (window.ActiveXObject) {
            tmpTransformationNode = new ActiveXObject("Microsoft.XmlDom");
            var sXml = "<validatorModuleXML></validatorModuleXML>";
            tmpTransformationNode.loadXML(sXml);
        }
        var objDiv = document.getElementById("divArchivesValidatorModuleXML");
        var xsltHelperObj = new wso2.wsf.XSLTHelper();
        xsltHelperObj.transform(objDiv, tmpTransformationNode, "extensions/tools_av/xslt/validator.xsl",true);
        wso2.wsf.Util.showOnlyOneMain(objDiv);
    },

    showDiv : function(objDiv) {
        if (objDiv == null) {
            return;
        }
        if (objDiv.style.display == 'inline') {
            objDiv.style.display = 'none';
        } else {
            objDiv.style.display = 'inline';
        }
    },

    validateAndSubmitAAR : function() {
        try {
            var formObj = document.getElementById('aarValidatorDivFormId')
            var inputObj = document.getElementById('filenameAARId')
            if (inputObj.value == "") {
                wso2.wsf.Util.alertWarning("Please Select a AAR to Validate");
                return false;
            }
            formObj.action = "fileupload/*";
            FileExcutor.execute = this.aarExecutor;
            formObj.submit();
            return true;
        } catch (e) {
            wso2.wsf.Util.alertWarning("You have selected an invalid Axis2 serivce archive.");
        }
    },

    aarExecutor : function(uuid) {
        var bodyXml = '<req:validateRequest xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<fileId>' + uuid + '</fileId>\n' +
                      '</req:validateRequest>\n';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "Service";
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:validate", bodyXml, aarMarExcutorCallback, [1]);

    },

    aarServicesXMLExecutor : function(uuid) {
        var bodyXml = '<req:validateServicesXML xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<fileId>' + uuid + '</fileId>\n' +
                      '</req:validateServicesXML>\n';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "Service";
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:validateServicesXML", bodyXml, aarMarExcutorCallback, [1]);

    },

    validateAndSubmitServicesXml : function() {
        try {
            var formObj = document.getElementById('services_xmlValidatorDivIdFormId')
            var inputObj = document.getElementById('filenameServices_xmlId')
            if (inputObj.value == "") {
                wso2.wsf.Util.alertWarning("Please Select a services.xml to Validate");
                return false;
            }
            formObj.action = "fileupload/*";
            FileExcutor.execute = this.aarServicesXMLExecutor;
            formObj.submit();
            return true;
        } catch(e) {
             wso2.wsf.Util.alertWarning("You have selected an invalid services.xml file.");
        }
    },

    validateAndSubmitMAR : function() {
        try {
            var formObj = document.getElementById('marValidatorDivFormId')
            var inputObj = document.getElementById('filenameMARId')
            if (inputObj.value == "") {
                wso2.wsf.Util.alertWarning("Please Select a MAR to Validate");
                return false;
            }
            formObj.action = "fileupload/*";
            FileExcutor.execute = this.marExecutor;
            formObj.submit();
            return true;
        } catch(e) {
            wso2.wsf.Util.alertWarning("You have selected an invalid Axis2 module archive.");
        }
    },

    marExecutor : function(uuid) {
        var bodyXml = '<req:validateRequest xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<fileId>' + uuid + '</fileId>\n' +
                      '</req:validateRequest>\n';

        var callURL = wso2.wsf.Util.getServerURL() + "/" + "Module";
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:validate", bodyXml, aarMarExcutorCallback, [2]);
    },

    validateAndSubmitModuleXml : function() {
        try {
            var formObj = document.getElementById('module_xmlValidatorDivIdFormId')
            var inputObj = document.getElementById('filenameModule_xmlId')
            if (inputObj.value == "") {
                wso2.wsf.Util.alertWarning("Please Select a module.xml to Validate");
                return false;
            }
            formObj.action = "fileupload/*";
            FileExcutor.execute = this.marModuleXMLExecutor;
            formObj.submit();
            return true;
        } catch(e) {
           wso2.wsf.Util.alertWarning("You have selected an invalid module.xml file.");
        }
    },

    marModuleXMLExecutor : function(uuid) {
        var bodyXml = '<req:validateModuleXMLRequest xmlns:req="http://org.wso2.wsf/tools">\n' +
                      '<fileId>' + uuid + '</fileId>\n' +
                      '</req:validateModuleXMLRequest>\n';

        var callURL = wso2.wsf.Util.getServerURL() + "/" + "Module";
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:validateModuleXML", bodyXml, aarMarExcutorCallback, [2]);
    }

}

function aarMarExcutorCallback() {
    var data = this.req.responseXML;
    var responseTextValue = data.getElementsByTagName("return")[0].firstChild.nodeValue;
    var proto = wso2.wsf.Util.getProtocol();
    var url = "";
    if (proto == "https") {
        url = URL + responseTextValue;
    } else if (proto == "http") {
        url = GURL + responseTextValue
    }
    window.open(url);
    if (this.params) {
        if (this.params[0] == 1) {
            wso2.tool.AV.showValidatorServicesXML();            
        } else if (this.params[0] == 2) {
            wso2.tool.AV.showValidatorModuleXML();
        }
    }
}