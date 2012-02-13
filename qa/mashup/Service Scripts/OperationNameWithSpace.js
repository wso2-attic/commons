this.serviceName = "version";
this.documentation = "Mashup Server version service";
getVersion.documentation = "Returns the Mashup Server version";
getVersion.operationName = "GET VERSION";

getVersion.inputTypes = {};
getVersion.outputType = "any";
function getVersion(){
         return ("The version of the Mashup Server is SNAPSHOT"); }