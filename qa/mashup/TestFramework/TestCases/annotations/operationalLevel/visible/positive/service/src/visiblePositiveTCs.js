this.documentation = "This is to test .visible annotation" ;


function visibleDefault(param) {
return param;
}


visibleSetToTrue.visible = true;
function visibleSetToTrue(param) {
return param;
}



visibleSetToFalse.visible = false;
function visibleSetToFalse(param) {
return param;
}



visibleSetTo0.visible = 0;
function visibleSetTo0(param) {
return param;
}


visibleSetToNaN.visible = NaN;
function visibleSetToNaN(param) {
return param;
}