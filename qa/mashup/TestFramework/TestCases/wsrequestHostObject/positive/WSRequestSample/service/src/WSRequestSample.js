// Tests the asynchronous invocation of WsRequest
wsRequestAsyncTest.inputTypes = "#raw";
wsRequestAsyncTest.outputType = "#raw";
function wsRequestAsyncTest(){
    var wsReq = new WSRequest();

    // Assign the callback function.
    wsReq.onreadystatechange = asyncCallbackFunc;

    // Create request.
    var options = new Array();
    options["useSOAP"] = false;
    options["HTTPMethod"] = "GET";

    // Add your developer ID here.
    var devId = "";
    var search = "Italy";
	var endPoint = "http://www.youtube.com/api2_rest?method=youtube.videos.list_by_category_and_tag&dev_id=" + devId +
                   "&category_id=2&tag=" + search;

    // Open asynchronously and send request.
    wsReq.open(options, endPoint, true);
    wsReq.send(null);

    // Signal end of main method body.
    return <result>Asynchronous call submitted</result>;

    // This function is called when the response is recieved by the WsRequest host object.
    function asyncCallbackFunc() {
		if (wsReq.responseXML != null) {
            //Creating a File Host Object.
            var file = new File("async-test-file.xml");

            //Writing the response to a file.
            file.openForAppending();
            file.write(wsReq.responseXML);
        }
    }
}

//tests HTTPProperties
wsRequestHTTPPropertiesTest.inputTypes = "none";
wsRequestHTTPPropertiesTest.outputType = "string";
function wsRequestHTTPPropertiesTest(){
    var req = new WSRequest();
    var options = new Array();
    options["useSOAP"] = "false";
    options["HTTPMethod"] = "POST";
    options["HTTPLocation"] = "weather/{city}";
    options["HTTPInputSerialization"] = "application/xml";
    req.open(options,"http://127.0.0.1:11001/services/system/RESTSample",false);
    var payload = "<POSTWeather><city>colombo</city><weatherDetails>35</weatherDetails></POSTWeather>";
    req.send(payload);
    var result = (req.responseE4X["return"]).text();
    if(result != "colombo"){
	throw ("POST did not return expected result. Expected colombo, received " + result);
    }

    options["HTTPMethod"] = "GET";
    options["HTTPInputSerialization"] = "application/x-www-form-urlencoded";
    req = new WSRequest();
    req.open(options,"http://127.0.0.1:11001/services/system/RESTSample",false);
    payload = "<GETWeather><city>colombo</city></GETWeather>";
    req.send(payload);
    result = (req.responseE4X["return"]).text();
    if(result != "35"){
	throw ("GET did not return expected result. Expected 35, received " + result);
    }

    req = new WSRequest();
    options["HTTPMethod"] = "PUT";
    options["HTTPInputSerialization"] = "application/xml";
    req.open(options,"http://127.0.0.1:11001/services/system/RESTSample",false);
    payload = "<PUTWeather><city>colombo</city><weatherDetails>30</weatherDetails></PUTWeather>";
    req.send(payload);
    result = (req.responseE4X["return"]).text();
    if(result != "colombo"){
	throw ("PUT did not return expected result. Expected colombo, received " + result);
    }

    options["HTTPMethod"] = "GET";
    options["HTTPInputSerialization"] = "application/x-www-form-urlencoded";
    req = new WSRequest();
    req.open(options,"http://127.0.0.1:11001/services/system/RESTSample",false);
    payload = "<GETWeather><city>colombo</city></GETWeather>";
    req.send(payload);
    result = (req.responseE4X["return"]).text();
    if(result != "30"){
	throw ("GET did not return expected result. Expected 30, received " + result);
    }

    options["HTTPMethod"] = "DELETE";
    options["HTTPInputSerialization"] = "application/x-www-form-urlencoded";
    req = new WSRequest();
    req.open(options,"http://127.0.0.1:11001/services/system/RESTSample",false);
    payload = "<GETWeather><city>colombo</city></GETWeather>";
    req.send(payload);
    result = (req.responseE4X["return"]).text();
    if(result != "colombo"){
	throw ("DELETE did not return expected result. Expected colombo, received " + result);
    }
    return "succesful";
}
