// return Infinity
returnInfinity.inputTypes="none";
returnInfinity.outputType="number";
function returnInfinity(param) {
    return Infinity;
}

// return string
returnString.inputTypes="string";
function returnString(param) {
    return param;
}

// return number
returnNumber.inputTypes="number";
function returnNumber(param) {
    return param;
}

// return boolean
returnBoolean.inputTypes="boolean";
function returnBoolean(param) {
    return param;
}

// return date
returnDate.inputTypes="date";
function returnDate(param) {
    return param;
}

// echo simple + complex compound values
complexEcho.inputTypes = {"simple" : "string", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
complexEcho.outputType = {"simple" : "string", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
function complexEcho(simple, complex, simple2){
    var complexReturn = new objectFunction();
    complexReturn.simple = simple;
    complexReturn.simple2 = simple2;
    complexReturn.complexEchoResponseTypecomplexType = complex;
    return complexReturn;
}

// echo simple + complex compound values with optional parameters
complexEchoOptionalParams.inputTypes = {"simple" : "string?", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
complexEchoOptionalParams.outputType = {"simple" : "string?", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
function complexEchoOptionalParams(simple, complex, simple2){
    var complexReturn = new objectFunction();
    complexReturn.simple2 = simple2;
    complexReturn.complexEchoOptionalParamsResponseTypecomplexType = complex;
    return complexReturn;
}

// echo simple + complex compound values with Array parameters
complexEchoArrayParams.inputTypes = {"simple" : "string*", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
complexEchoArrayParams.outputType = {"simple" : "string*", "complex" : {"nested1" : "number", "nested2" : "boolean"}, "simple2" : "number"};
function complexEchoArrayParams(simple, complex, simple2){
    var complexReturn = new objectFunction();
    complexReturn.simple2 = simple2;
    complexReturn.simple = simple;
    complexReturn.complexEchoArrayParamsResponseTypecomplexType = complex;
    return complexReturn;
}

// echo simple + complex compound values with Array parameters
echoComplexCompositions.inputTypes = {"simple1" : "string*", "simple2" : "xs:int?", "simple3" : "xs:dateTime+", "complex1" : {"nested1" : "number", "nested2" : "xs:boolean*", "nested3" : "object"}, "simple4" : "boolean", "complex2" : {"anotherNested" : "array+"}};
echoComplexCompositions.outputType = {"simple1" : "string*", "simple2" : "xs:int?", "simple3" : "xs:dateTime+", "complex1" : {"nested1" : "number", "nested2" : "xs:boolean*", "nested3" : "object"}, "simple4" : "boolean", "complex2" : {"anotherNested" : "array+"}};
function echoComplexCompositions(simple1, simple2, simple3, complex1, simple4, complex2){
    var complexReturn = new objectFunction();
    complexReturn.simple1 = simple1;
    complexReturn.simple2 = simple2;
    complexReturn.simple3 = simple3;
    complexReturn.simple4 = simple4;
    complexReturn.echoComplexCompositionsResponseTypecomplex1Type = complex1;
    complexReturn.echoComplexCompositionsResponseTypecomplex2Type = complex2;
    return complexReturn;
}

//echo complex object with description
echoComplexParam.inputTypes = {"param" : {"nested" : "string"}};
echoComplexParam.outputType = {"param" : {"nested" : "string"}};
function echoComplexParam(param) {
    var out = new objectFunction();
    out.echoComplexParamResponseTypeparamType = param;
   return out;
}

// Tests whether defining inputTypes using "", {} and "none" are consistant
noInputAsSimple.inputTypes = "";
noInputAsSimple.outputType = "string";
function noInputAsSimple() {
    return "ok";
}

// Tests whether defining inputTypes using "", {} and "none" are consistant
noInputAsComplex.inputTypes = {};
noInputAsComplex.outputType = "string";
function noInputAsComplex() {
    return "ok";
}

// Tests whether defining inputTypes using "", {} and "none" are consistant
noInputAsNone.inputTypes = "none";
noInputAsNone.outputType = "string";
function noInputAsNone() {
    return "ok";
}

// Tests whether defining outputType using "", {} and "none" are consistant
noOutputAsSimple.inputTypes = "string";
noOutputAsSimple.outputType = "";
function noOutputAsSimple(param) {
}

// Tests whether defining outputType using "", {} and "none" are consistant
noOutputAsComplex.inputTypes = "string";
noOutputAsComplex.outputType = {};
function noOutputAsComplex(param) {
}

// Tests whether defining outputType using "", {} and "none" are consistant
noOutputAsNone.inputTypes = "string";
noOutputAsNone.outputType = "none";
function noOutputAsNone(param) {
}


// Added to test Mashup-16 (These test cases were extracted from there)
// Test returning xml
echoXMLNode.inputTypes = "#raw";
echoXMLNode.outputType = "#raw";
function echoXMLNode(payload) {
   return <successful/>;
}

// Added to test Mashup-16 (These test cases were extracted from there)
// Test returning a list with a single element
echoXMLNodeList.inputTypes = "#raw";
echoXMLNodeList.outputType = "#raw";
function echoXMLNodeList(payload) {
   return <><successful/></>;
}

// Added to test Mashup-16 (These test cases were extracted from there)
// Test returning a list with a multiple elements
echoXMLNodeList2.inputTypes = "#raw";
function echoXMLNodeList2(payload) {
   return <><successful/><excess/></>;
}

// Test returning an empty list
echoXMLNodeList3.inputTypes = "#raw";
echoXMLNodeList3.outputType = "#raw";
function echoXMLNodeList3(payload) {
   return <></>;
}

function objectFunction() {
}