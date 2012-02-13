this.documentation = "Mashup Server version service";

getVersion.documentation = "Returns the Mashup Server version";
getVersion.operationName = "GETVERSION";
getVersion.operationName = "GET_VERSION";

getVersion.inputTypes = {};
getVersion.outputType = "any";
function getVersion(){
        return ("The version of the Mashup Server is SNAPSHOT");
}