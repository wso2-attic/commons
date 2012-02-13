/* Purpose	:	This is a sample service to verify the handling of "safe" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */
 
this.documentation = "This is to test .safe annotation" ;

helloSafe.safe = true;
 function helloSafe(param) {
 	return (param);
 }