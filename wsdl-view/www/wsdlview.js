//JS related to wsdlview options

if (typeof wso2 == "undefined") {
    var wso2 = {};
}
if (typeof wso2.tool == "undefined") {
    wso2.tool = {};
}

wso2.tool.WSDLView = {

    classFqn : "" ,

    options : '',

    fileUuids : '',

    getOptions : function() {
        var body_xml = '<req:getOptions xmlns:req="http://org.wso2.wsf/tools">\n' +
                       '</req:getOptions>\n';

        var callURL = wso2.wsf.Util.getServerURL() + "/" + "WSDLView" ;
        new wso2.wsf.WSRequest(callURL, "urn:getOptions", body_xml, getOptionsCallback);
    },

    showWSDLView : function() {
        this.viewHelper("wizard-resources", "divWSDLViewWizardThree", "wsdlview_wizard_resources.xsl");
    },

    viewHelper : function(documentName, divName, xslFile, paramsArray) {
        var tmpTransformationNode;
        if (window.XMLHttpRequest && !wso2.wsf.Util.isIE()) {
            tmpTransformationNode =
            document.implementation.createDocument("", documentName, null);
        } else if (window.ActiveXObject) {
            tmpTransformationNode = new ActiveXObject("Microsoft.XmlDom");
            var sXml = "<" + documentName + "></" + documentName + ">";
            tmpTransformationNode.loadXML(sXml);
        }
        var objDiv = document.getElementById(divName);
        var xsltHelperObj = new wso2.wsf.XSLTHelper();
        xsltHelperObj.transform(objDiv, tmpTransformationNode, "extensions/tools_wsdlview/xslt/"+xslFile, true, false, paramsArray);
        wso2.wsf.Util.showOnlyOneMain(objDiv);
    },

    startWSDLView : function(options) {
        this.options = options;
        var bodyXML = '<req:wsdlviewWithResources xmlns:req="http://org.wso2.wsf/tools">\n';
        for (var o in options) {
            bodyXML += '<options><![CDATA[' + o + ']]></options>\n';
            var oVal = options[o];
            if (oVal != null && oVal.length != 0) {
                bodyXML += '<options><![CDATA[' + oVal + ']]></options>\n';
            }
        }
        if (typeof(wso2.tool.WSDLView.fileUuids) == "object") {
            for (var p in wso2.tool.WSDLView.fileUuids) {
                bodyXML += '<uuids>' + wso2.tool.WSDLView.fileUuids[p] + '</uuids>\n';
            }
        } else {
            bodyXML += '<uuids>' + wso2.tool.WSDLView.fileUuids + '</uuids>\n';
        }
        bodyXML += '</req:wsdlviewWithResources>';
        var callURL = wso2.wsf.Util.getServerURL() + "/" + "WSDLView" ;
        wso2.wsf.Util.cursorWait();
        new wso2.wsf.WSRequest(callURL, "urn:wsdlviewWithResources", bodyXML, startWSDLViewCallback);

    },

    toWizard2 : function() {
        try {
            var objForm = document.getElementById('wsdlviewjarupload');
            var fileArray = document.getElementsByName('jarResourceWSDLView');
            var len = fileArray.length;
            var hasValues = false;
            for (var j = 0; j < len; j++) {
                if (fileArray[j].value != "") {
                    hasValues = true;
                    break;
                }
            }
            if (hasValues) {
                objForm.action = "fileupload/*";
                FileExcutor.execute = this.executeWizard2;
                objForm.submit();
                return true;
            }
            wso2.wsf.Util.alertWarning('Please select the resource(s)');
            return false;
        } catch(e) {
            wso2.wsf.Util.alertWarning('You have selected an invalid resource file.');
        }
    },

    executeWizard2 : function(obj) {
        wso2.tool.WSDLView.fileUuids = obj;
        wso2.tool.WSDLView.getOptions();
    },

    addLibraryFileuplod : function(objDiv) {
        var blankLabelElem = document.createElement('label');
        blankLabelElem.innerHTML = "&nbsp;";
        var elem = document.createElement('input');
        var brElem = document.createElement('br');
        var nameAttr = document.createAttribute('name');
        nameAttr.value = "jarResourceWSDLView";
        var idAttr = document.createAttribute('id');
        idAttr.value = "jarResourceWSDLView";
        var sizeAttr = document.createAttribute('size');
        sizeAttr.value = "50";
        var typeAttr = document.createAttribute('type');
        typeAttr.value = "file";
        elem.attributes.setNamedItem(nameAttr);
        elem.attributes.setNamedItem(idAttr);
        elem.attributes.setNamedItem(sizeAttr);
        elem.attributes.setNamedItem(typeAttr);
        objDiv.appendChild(brElem);
        objDiv.appendChild(blankLabelElem);
        objDiv.appendChild(elem);
    }
}

function getOptionsCallback() {
    var divObject = document.getElementById("divWSDLViewWizardTwo");
    var xsltHelperObj = new wso2.wsf.XSLTHelper();
    var xslAbsPath = "";
    var proto = wso2.wsf.Util.getProtocol();
    if (proto == "https") {
        xslAbsPath += URL;
    } else if (proto == "http") {
        xslAbsPath += GURL;
    }
    xslAbsPath += "/extensions/tools_wsdlview/xslt/wsdlview_wizard2.xsl";
    xsltHelperObj.transform(divObject, this.req.responseXML, xslAbsPath, true);
    wso2.wsf.Util.showOnlyOneMain(document.getElementById("divWSDLViewWizardTwo"));
}

function startWSDLViewCallback() {
    var data = this.req.responseXML;
    var responseTextValue = data.getElementsByTagName("return")[0].firstChild.nodeValue;
    var openBaseURL = "http://" + HOST +
                      (HTTP_PORT != 80 ? (":" + HTTP_PORT + ROOT_CONTEXT) : ROOT_CONTEXT);
    window.open(openBaseURL + responseTextValue);
    wso2.tool.WSDLView.showWSDLView();
}