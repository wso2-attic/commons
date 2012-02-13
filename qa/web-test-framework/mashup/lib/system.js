// Test the wait() method of System host object
systemWaitTest.inputTypes = "string";
systemWaitTest.outputType = "string";
function systemWaitTest(param){
	system.wait();
	return successful;
}
// Test the wait(Long) method of System host object
systemLongWaitTest.inputTypes = "#raw";
systemLongWaitTest.outputType = "#raw";
function systemLongWaitTest(param){
	system.wait(2000);
	return <wait>wait successfull</wait>;
}

// Test the include() method of System host object
systemFileIncludeTest.inputTypes = "#raw";
systemFileIncludeTest.outputType = "#raw";
function systemFileIncludeTest(param){
	system.include("include.js");
	return getName("WSO2");
}
// Test the include() method of System host object
systemRelativeURLIncludeTest.inputTypes = "#raw";
systemRelativeURLIncludeTest.outputType = "#raw";
function systemRelativeURLIncludeTest(param){
	system.include("system/hostObjectService/include.js");
	return getName("WSO2");
}
// Test the include() method of System host object
systemAbsoluteURLIncludeTest.inputTypes = "#raw";
systemAbsoluteURLIncludeTest.outputType = "#raw";
function systemAbsoluteURLIncludeTest(param){
	system.include("http://127.0.0.1:11001/services/system/hostObjectService/include.js");
	return getName("WSO2");
}
