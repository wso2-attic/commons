
this.serviceName = "weatherForecast";
this.documentation = "Get one week weather forecast for valid zip code in USA" ;

getWeatherByZipCode.documentation = "Get one week weather forecast for valid zip code in USA" ;
getWeatherByZipCode.inputTypes = { "zipCode" : "string" };
getWeatherByZipCode.outputType = "xml";  
function getWeatherByZipCode(zipCode)
{
    if (zipCode == "")
        throw ("A 'zipCode' value must be specified.");

    //create new WSRequest object for requesting the get weather by zip code
    var getWeatherService = new WSRequest();

    var options = new Array();
    options["useSOAP"] = 1.1;
    options["HTTPMethod"] = "POST";
    options["useWSA"] = "1.0";

    //SOAP action of the GetWeatherByZipCode Web Service
    options["action"] = "http://www.webservicex.net/GetWeatherByZipCode";

    //create payload for GetWeatherByZipCode Web Service
    var payload = 
      <GetWeatherByZipCode xmlns="http://www.webservicex.net">
        <ZipCode>{zipCode}</ZipCode>
      </GetWeatherByZipCode>;

    try {
        var endpoint = "http://www.webservicex.net/WeatherForecast.asmx";

        //open get weather request with synchronous option
        getWeatherService.open(options, endpoint, false);
        getWeatherService.send(payload);
        
    } catch(ex) {
        print(ex);
        throw("There was an error accessing the remote service '" + endpoint + "', The Exception was : " + ex);
    }

	return getWeatherService.responseXML;
}