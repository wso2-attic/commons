getWeather.safe = true;
getWeather.httpMethod = "GET";
//getWeather.httpLocation = "weather/{city}";
getWeather.inputTypes = "string";
getWeather.outputType = "string";
function getWeather(city){
var details = session.get(city);
if (details == null) {
   throw ("Cannot find weather details of city " + city + ".")
}
return details;
}



POSTWeather.httpMethod = "POST";
POSTWeather.httpLocation = "weather/{city}";
POSTWeather.inputTypes = {"city" : "string",
"weatherDetails" : "string"};
POSTWeather.outputType = "string";
function POSTWeather(city, weatherDetails){
var details = session.get(city);
if (details != null) {
   throw ("Weather details of city " + city + " already exists.")
}
session.put(city ,weatherDetails);
return city;
}



DeleteWeather.httpMethod = "DELETE";
DeleteWeather.httpLocation = "weather/{city}";
DeleteWeather.inputTypes = "string";
DeleteWeather.outputType = "string";
function DeleteWeather(city){
var details = session.get(city);
if (details == null) {
   throw ("Cannot find weather details of city " + city + " to delete.")
}
session.remove(city);
return city;
}



PUTWeather.httpMethod = "PUT";
PUTWeather.httpLocation = "weather/{city}";
PUTWeather.inputTypes = {"city" : "string",
"weatherDetails" : "string"};
PUTWeather.outputType = "string";
function PUTWeather(city, weatherDetails){
var details = session.get(city);
if (details == null) {
   throw ("Cannot find weather details of city " + city + " to update.")
}
session.put(city ,weatherDetails);
return city;
}
