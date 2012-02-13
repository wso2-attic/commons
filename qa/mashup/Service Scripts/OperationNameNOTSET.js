this.serviceName = "versionSimple";
this.documentation = "Mashup Server version service";

getVersion.documentation = "Returns the Mashup Server version ";

getVersion.inputTypes = {};
getVersion.outputType = "any";
function getVersion(){
        return ("The version of the Mashup Server is SNAPSHOT");
}