
this.schemaTargetNamespace = "http://wso2.org/schema";

this.serviceName = "versionSimple";
this.documentation = "Mashup Server version service";

getVersion.documentation = "Returns the version of the Mashup Server";
getVersion.operationName = "GETVERSION";

getVersion.inputTypes = {};
getVersion.outputType = "any";
function getVersion(){
        return ("The version of the Mashup Server is SNAPSHOT");
}
    