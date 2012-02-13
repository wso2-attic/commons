/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Echo JavaScript string
echoJSstring.inputTypes="string";
echoJSstring.outputType="string";
function echoJSstring(param) {
    return param;
}

// Echo JavaScript String
echoJSString.inputTypes="String";
echoJSString.outputType="String";
function echoJSString(param) {
    return param;
}

// Echo JavaScript Number
echoJSNumber.inputTypes="Number";
echoJSNumber.outputType="Number";
function echoJSNumber(param) {
    return param;
}

// Echo JavaScript number
echoJSnumber.inputTypes="number";
echoJSnumber.outputType="number";
function echoJSnumber(param) {
    return param;
}

// Echo JavaScript Boolean
echoJSBoolean.inputTypes="Boolean";
echoJSBoolean.outputType="Boolean";
function echoJSBoolean(param) {
    return param;
}

// Echo JavaScript boolean
echoJSboolean.inputTypes="boolean";
echoJSboolean.outputType="boolean";
function echoJSboolean(param) {
    return param;
}

// Echo JavaScript date
echoJSdate.inputTypes="date";
echoJSdate.outputType="date";
function echoJSdate(param) {
    return param;
}

// Echo JavaScript Date
echoJSDate.inputTypes="Date";
echoJSDate.outputType="Date";
function echoJSDate(param) {
    return param;
}

// Echo string array
echoStringArray.inputTypes="string*";
echoStringArray.outputType="Array";
function echoStringArray(param) {
return param;
}

// return JavaScript native array with indices
returnJSArrayWithIndices.inputTypes="";
returnJSArrayWithIndices.outputType="Array";
function returnJSArrayWithIndices() {
    var o = new Array();
o[1] = "value1";
o[2] = 2.270;
o[3] = <value>2</value>;
    return o;
}

// return JavaScript native array with properties
returnJSArrayWithProperties.inputTypes="";
returnJSArrayWithProperties.outputType="Array";
function returnJSArrayWithProperties() {
    var o = new Array();
o.value1 = "value1";
o.value2 = 2.270;
o.value3 = <value>2</value>;
    return o;
}

// return JavaScript native object with properties
returnJSObject.inputTypes = {"param1" : "string",
    "param2" : "number",
    "param3" : "boolean"
    };
returnJSObject.outputType = "object";
function returnJSObject(param1, param2, param3) {
    var o;
    o = new objectFunction();
    o.param1 = param1;
    o.param2 = param2;
    o.param3 = param3;
    return o;
}

function objectFunction() {
}

// echo enumeration value
echoEnumeration.inputTypes = {"param" : "a | b | c"};
echoEnumeration.outputType = "string";
function echoEnumeration(param){
    return param;
}

// Echo xs:string
echoXSstring.inputTypes="xs:string";
echoXSstring.outputType="xs:string";
function echoXSstring(param) {
    return param;
}

// Echo xs:normalizedString
echoXSnormalizedString.inputTypes="xs:normalizedString";
echoXSnormalizedString.outputType="xs:normalizedString";
function echoXSnormalizedString(param) {
    return param;
}

// Echo xs:token
echoXStoken.inputTypes="xs:token";
echoXStoken.outputType="xs:token";
function echoXStoken(param) {
    return param;
}

// Echo xs:language
echoXSlanguage.inputTypes="xs:language";
echoXSlanguage.outputType="xs:language";
function echoXSlanguage(param) {
    return param;
}

// Echo xs:Name
echoXSName.inputTypes="xs:Name";
echoXSName.outputType="xs:Name";
function echoXSName(param) {
    return param;
}

// Echo xs:NCName
echoXSNCName.inputTypes="xs:NCName";
echoXSNCName.outputType="xs:NCName";
function echoXSNCName(param) {
    return param;
}

// Echo xs:NOTATION
echoXSNOTATION.inputTypes="xs:NOTATION";
echoXSNOTATION.outputType="xs:NOTATION";
function echoXSNOTATION(param) {
    return param;
}

// Echo xs:anyURI
echoXSanyURI.inputTypes="xs:anyURI";
echoXSanyURI.outputType="xs:anyURI";
function echoXSanyURI(param) {
    return param;
}

// Echo xs:float
echoXSfloat.inputTypes="xs:float";
echoXSfloat.outputType="xs:float";
function echoXSfloat(param) {
    return param;
}

// Echo xs:double
echoXSdouble.inputTypes="xs:double";
echoXSdouble.outputType="xs:double";
function echoXSdouble(param) {
    return param;
}

// Echo xs:duration
echoXSduration.inputTypes="xs:duration";
echoXSduration.outputType="xs:duration";
function echoXSduration(param) {
    return param;
}

// Echo xs:integer
echoXSinteger.inputTypes="xs:integer";
echoXSinteger.outputType="xs:integer";
function echoXSinteger(param) {
    return param;
}

// Echo xs:nonPositiveInteger
echoXSnonPositiveInteger.inputTypes="xs:nonPositiveInteger";
echoXSnonPositiveInteger.outputType="xs:nonPositiveInteger";
function echoXSnonPositiveInteger(param) {
    return param;
}

// Echo xs:negativeInteger
echoXSnegativeInteger.inputTypes="xs:negativeInteger";
echoXSnegativeInteger.outputType="xs:negativeInteger";
function echoXSnegativeInteger(param) {
    return param;
}

// Echo xs:long
echoXSlong.inputTypes="xs:long";
echoXSlong.outputType="xs:long";
function echoXSlong(param) {
    return param;
}

// Echo xs:int
echoXSint.inputTypes="xs:int";
echoXSint.outputType="xs:int";
function echoXSint(param) {
    return param;
}

// Echo xs:short
echoXSshort.inputTypes="xs:short";
echoXSshort.outputType="xs:short";
function echoXSshort(param) {
    return param;
}

// Echo xs:byte
echoXSbyte.inputTypes="xs:byte";
echoXSbyte.outputType="xs:byte";
function echoXSbyte(param) {
    return param;
}

// Echo xs:nonNegativeInteger
echoXSnonNegativeInteger.inputTypes="xs:nonNegativeInteger";
echoXSnonNegativeInteger.outputType="xs:nonNegativeInteger";
function echoXSnonNegativeInteger(param) {
    return param;
}

// Echo xs:unsignedLong
echoXSunsignedLong.inputTypes="xs:unsignedLong";
echoXSunsignedLong.outputType="xs:unsignedLong";
function echoXSunsignedLong(param) {
    return param;
}

// Echo xs:unsignedInt
echoXSunsignedInt.inputTypes="xs:unsignedInt";
echoXSunsignedInt.outputType="xs:unsignedInt";
function echoXSunsignedInt(param) {
    return param;
}

// Echo xs:unsignedShort
echoXSunsignedShort.inputTypes="xs:unsignedShort";
echoXSunsignedShort.outputType="xs:unsignedShort";
function echoXSunsignedShort(param) {
    return param;
}

// Echo xs:unsignedByte
echoXSunsignedByte.inputTypes="xs:unsignedByte";
echoXSunsignedByte.outputType="xs:unsignedByte";
function echoXSunsignedByte(param) {
    return param;
}

// Echo xs:positiveInteger
echoXSpositiveInteger.inputTypes="xs:positiveInteger";
echoXSpositiveInteger.outputType="xs:positiveInteger";
function echoXSpositiveInteger(param) {
    return param;
}

// Echo xs:decimal
echoXSdecimal.inputTypes="xs:decimal";
echoXSdecimal.outputType="xs:decimal";
function echoXSdecimal(param) {
    return param;
}

// Echo xs:boolean
echoXSboolean.inputTypes="xs:boolean";
echoXSboolean.outputType="xs:boolean";
function echoXSboolean(param) {
    return param;
}

// Echo xs:dateTime
echoXSdateTime.inputTypes="xs:dateTime";
echoXSdateTime.outputType="xs:dateTime";
function echoXSdateTime(param) {
    return param;
}

// Echo xs:date
echoXSdate.inputTypes="xs:date";
echoXSdate.outputType="xs:date";
function echoXSdate(param) {
    return param;
}

// Echo xs:time
echoXStime.inputTypes="xs:time";
echoXStime.outputType="xs:time";
function echoXStime(param) {
    return param;
}

// Echo xs:gYearMonth
echoXSgYearMonth.inputTypes="xs:gYearMonth";
echoXSgYearMonth.outputType="xs:gYearMonth";
function echoXSgYearMonth(param) {
    return param;
}

// Echo xs:gMonthDay
echoXSgMonthDay.inputTypes="xs:gMonthDay";
echoXSgMonthDay.outputType="xs:gMonthDay";
function echoXSgMonthDay(param) {
    return param;
}

// Echo xs:gYear
echoXSgYear.inputTypes="xs:gYear";
echoXSgYear.outputType="xs:gYear";
function echoXSgYear(param) {
    return param;
}

// Echo xs:gDay
echoXSgDay.inputTypes="xs:gDay";
echoXSgDay.outputType="xs:gDay";
function echoXSgDay(param) {
    return param;
}

// Echo xs:gMonth
echoXSgMonth.inputTypes="xs:gMonth";
echoXSgMonth.outputType="xs:gMonth";
function echoXSgMonth(param) {
    return param;
}

// Echo xs:anyType
echoXSanyType.inputTypes="xs:anyType";
echoXSanyType.outputType="xs:anyType";
function echoXSanyType(param) {
    return param;
}

// Echo xs:QName
echoXSQName.inputTypes="xs:QName";
echoXSQName.outputType="xs:QName";
function echoXSQName(param) {
    return param;
}

// Echo xs:hexBinary
echoXShexBinary.inputTypes="xs:hexBinary";
echoXShexBinary.outputType="xs:hexBinary";
function echoXShexBinary(param) {
    return param;
}

// Echo xs:base64Binary
echoXSbase64Binary.inputTypes="xs:base64Binary";
echoXSbase64Binary.outputType="xs:base64Binary";
function echoXSbase64Binary(param) {
    return param;
}

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