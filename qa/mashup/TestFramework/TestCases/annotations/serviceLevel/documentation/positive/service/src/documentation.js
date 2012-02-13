/* Purpose	:	This is a sample service to verify the handling of "documentation" annotation of WSO2 Mashup Server.
 * Author	: 	Yumani Ranaweera
 */

// Testing service level documentation 
this.documentation = "This is to test service level documentation";
 function DocumentationTest1(param) {
 	return param;
}

// Testing operational level documentation 
DocumentationTest2.documentation = "This is to test operational level documentation";
 function DocumentationTest2(param) {
 	return param;
}