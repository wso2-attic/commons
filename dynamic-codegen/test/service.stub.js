
//  Example stubs for version operations.  This function is not intended 
//  to be called, but rather as a source for copy-and-paste development.

function stubs()
{
    // getVersion operation
    try {
        /* xs:string*/ getVersionReturn = version.getVersion();
    } catch (e) {
        // fault handling
    }

}

var version = new WebService("https://10.100.1.213:9443/services/version");

version.getVersion =
    function getVersion()
    {
        var request = 
            '<getVersion xmlns="http://version.services.wsas.wso2.org/xsd">' +
            '</getVersion>' ;
        version._options.action = "urn:getVersion";
        if (typeof(version.getVersion.callback) == 'function') {
            version._callback = version.getVersion._callback;
        } else {
            version._callback = ""; 
        }
        var response = version._call(request);
        
        return /* */ response;
    }
version.getVersion._callback =
    function callback()
    {
        if (version._WSRequest.readyState == 4)
            version.getVersion.callback(version._WSRequest.responseXML);
    }
    


// WebService object.
function WebService(endpoint)
{
    this._endpoint = endpoint;
    
    // private helper functions
    try {
        this._WSRequest = new ActiveXObject("WSRequest");
    } catch(e){
        alert("WSRequest object not defined!");
    }

    this._options = new Array();
    // set binding-wide options
    this._options.useSOAP = true;

    this._call =
    function callWS(reqContent)
    {
        isAsync = (typeof(this._callback) == 'function');
        
        if (isAsync) this._WSRequest.onreadystatechange = this._callback;
        this._WSRequest.open(this._options, this._endpoint, isAsync);
        this._WSRequest.send(reqContent);
        
        if (isAsync) {
            return "";
        } else {
            try {
                var resultContent = this._WSRequest.responseText;
                if (resultContent == "") { 
                    throw ("no response");
                }
                var resultXML = this._WSRequest.responseXML;
                if (resultXML.documentElement.nodeName == 'Fault' &&
                        resultXML.documentElement.namespaceURI == 'http://www.w3.org/2003/05/soap-envelope') {
                    throw(resultContent);
                }
            } catch (e) {
                throw(e);
            }
            return resultXML;
        }
    }				
}

