complexInput.inputTypes = {
    "complexInput1" : "string",
    "complexInput2" : "number",
    "complexInput3" : "date",
    "complexInput4" : "any"
};
complexInput.outputType = "string";
function complexInput(complexInput1, complexInput2, complexInput3, complexInput4) {
    return "complexInput1=" + complexInput1 +
            " complexInput2=" + complexInput2 +
           " complexInput3=" + complexInput3 +
           " complexInput4= " + complexInput4
};