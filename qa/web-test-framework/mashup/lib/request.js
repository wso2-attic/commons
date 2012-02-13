function returnIP() {
var requestIP  = request.remoteIP;
return requestIP;
}     

function returnURL() {
var requestURL = request.address;
return requestURL;
}   

function returnAuthUser() {
var requestUser=  request.authenticatedUser ;
return requestUser;
}