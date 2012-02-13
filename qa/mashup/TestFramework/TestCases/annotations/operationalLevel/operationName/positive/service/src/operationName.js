/* Purpose	:	This is a sample service to verify the handling of "operationName" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

//When operationName annotation is not used. 
noOpName.inputTypes = "string";
noOpName.outputType = "any";
function noOpName(param){
        return param;
}

//When operationName annotation is used. 
opNameGiven.operationName = "GETVERSION";
opNameGiven.inputTypes = "string";
opNameGiven.outputType = "any";
function opNameGiven(param){
        return param;
}

//When operationName annotation is used twice. 
twoOpNames.operationName = "GETVERSION1";
twoOpNames.operationName = "GET_VERSION";
twoOpNames.inputTypes = "string";
twoOpNames.outputType = "any";
function twoOpNames(param){
        return param;
}
