/* Purpose	:	This is a sample service to verify the handling of "input/OutputTypes" annotation of WSO2 Mashup Server.
 * Author	: 	Kieth Chapman
 */

// Check results when there's no inputs. 
NoInput.inputTypes = "";
NoInput.outputType = "string";
function NoInput() {
 return "test"}


// Check the serialization when declared as "string" 
testString.inputTypes = "string";
testString.outputType = "string";
function testString(param) {
     return param;
}


// Check the serialization when declared as "number"
testNumber.inputTypes = "number";
testNumber.outputType = "number";
function testNumber(param) {
    return param;
}


// Check the serialization when declared as "boolean"
testBoolean.inputTypes = "Boolean";
testBoolean.outputType = "Boolean";
function testBoolean(param) {
      return param;
}


// Check the serialization when declared as "date
testDate.inputTypes = "date";
testDate.outputType = "date";
function testDate(param) {
    return param;
}


// Check the serialization when declared as "object"
testObject.inputTypes = "Object";
testObject.outputType = "Object";
function testObject(param) {
    return param;
}



// Check the serialization when declared as "xs:string"
xsString.inputTypes = "xs:string";
 xsString.outputType = "xs:string";
 function xsString(param) {
  return param;
 }


// Check the serialization when declared as "xs:int"
xsInt.inputTypes = "xs:int";
 xsInt.outputType = "xs:int";
 function xsInt(param) {
  return param;
 }


// Check the serialization when declared as "xs:float"
xsfloat.inputTypes = "xs:float";
xsfloat.outputType = "xs:float";
function xsfloat(param) {
  return param;
 }


// Check the serialization when declared as "xs:normalizedString"
xsNormalizedString.inputTypes = "xs:normalizedString";
 xsNormalizedString.outputType = "xs:normalizedString";
 function xsNormalizedString(param) {
  return param;
 }



// Check the serialization when declared as "xs:Name"
xsName.inputTypes = "xs:Name";
 xsName.outputType = "xs:Name";
 function xsName(param) {
  return param;
 }


// Check the serialization when declared as "xs:token"
xsToken.inputTypes = "xs:token";
xsToken.outputType = "xs:token";
 function xsToken(param) {
  return param;
 }




// Check when .outputTypes='none'
ouputNon.inputTypes = {
   	"Input1" : "string"
};
ouputNon.outputType = 'none';
function ouputNon(Input1) {

}



// Test xs:dateTime binding
readDate.inputTypes = { "param" : "xs:dateTime" };
function readDate(param) {
    //print(typeof(instant));
    return param;
}


// Test xs:dateTime binding
photoForDate.inputTypes = {"param" : "xs:date"};
photoForDate.outputType = "xml";
function photoForDate(param) {
	//print(requestedDate);
	//print(typeof(requestedDate));
	return param;
}
