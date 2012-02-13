/* Purpose	:	This is a sample service to verify the handling of "targetNamespace" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */
 
this.documentation ="This is to test .TargetNamespace annotation" ;
this.targetNamespace = "http://wso2.org/Mashup";

function targetNamespace1(param) {
 	return (param);
 }