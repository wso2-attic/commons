

invokeGetVersion.inputTypes = "string";
invokeGetVersion.outputType = "#raw";

function invokeGetVersion(keystore_path){
 var request = new WSRequest();
 var options = new Array();
 options["username"] = "admin"; 
 options["password"] = "admin";  
 options["encryptionUser"] = "wso2carbon";
 options["keystore.type"] = "JKS";
 options["keystore.file"] = keystore_path;
 options["keystore.password"] = "wso2carbon";  
 options["privateKeyAlias"] = "wso2carbon";
 options["privateKeyPassword"] = "wso2carbon"; 
 
var service = new
QName("http://services.mashup.wso2.org/version","version");
request.openWSDL("http://localhost:9763/services/version?wsdl",false,
options,service,"SecureSOAP11Endpoint");
request.send("getVersion",null);
return request.responseXML;
}
