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



