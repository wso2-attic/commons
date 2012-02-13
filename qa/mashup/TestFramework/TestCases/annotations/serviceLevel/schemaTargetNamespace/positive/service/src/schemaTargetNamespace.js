/* Purpose	:	This is a sample service to verify the handling of "schemaTargetNamespace" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

this.documentation = "This is to test .schemaTargetNamespace annotation" ;
this.schemaTargetNamespace = "http://wso2.org/schema";

function helloTNameSpace(param) {
 	return param;
 }