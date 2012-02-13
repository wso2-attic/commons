
//  Example stubs for echoService operations.  This function is not intended 
//  to be called, but rather as a source for copy-and-paste development.

function stubs()
{
    // throwAxisFault operation
    try {
        /* xs:string */ throwAxisFaultReturn = echoService.throwAxisFault();
    } catch (e) {
        // fault handling
    }

    // echoOMElement operation
    try {
        /* xs:anyType */ echoOMElementReturn = echoService.echoOMElement(/* xs:anyType */ param_omEle);
    } catch (e) {
        // fault handling
    }

    // echoStringArrays operation
    try {
        /* ns:SimpleBean */ echoStringArraysReturn = echoService.echoStringArrays(/* array of xs:string */ param_a, /* array of xs:string */ param_b, /* xs:int */ param_c);
    } catch (e) {
        // fault handling
    }

    // echoInt operation
    try {
        /* xs:int */ echoIntReturn = echoService.echoInt(/* xs:int */ param_in);
    } catch (e) {
        // fault handling
    }

    // echoString operation
    try {
        /* xs:string */ echoStringReturn = echoService.echoString(/* xs:string */ param_in);
    } catch (e) {
        // fault handling
    }

}

var echoService = new WebService("http://192.168.1.53:9762/soap/echo");

echoService.throwAxisFault =
    function throwAxisFault()
    {
        var request = 
            "";
        var response = echoService._call(request);
        return /* xs:string */ response["return"].toString();
    }

echoService.echoOMElement =
    function echoOMElement(/* xs:anyType */ _omEle)
    {
        var request = 
            <echoOMElement xmlns="http://echo.services.wsas.wso2.org/xsd">
                <omEle xmlns="">{_omEle}</omEle>
            </echoOMElement> ;
        var response = echoService._call(request);
        return /* xs:anyType */ response;
    }

echoService.echoStringArrays =
    function echoStringArrays(/* array of xs:string */ _a, /* array of xs:string */ _b, /* xs:int */ _c)
    {
        var _array_of_a = <array-wrapper/>;
        for (var i=0; i < _a.length; i++)
            _array_of_a.appendChild(<a>{_a[i]}</a>);
    
        var _array_of_b = <array-wrapper/>;
        for (var i=0; i < _b.length; i++)
            _array_of_b.appendChild(<b>{_b[i]}</b>);
    
        var request = 
            <echoStringArrays xmlns="http://echo.services.wsas.wso2.org/xsd">
                {_array_of_a.a}
                {_array_of_b.b}
                <c xmlns="">{_c}</c>
            </echoStringArrays> ;
        var response = echoService._call(request);
        return /* ns:SimpleBean */ response;
    }

echoService.echoInt =
    function echoInt(/* xs:int */ _in)
    {
        var request = 
            <echoInt xmlns="http://echo.services.wsas.wso2.org/xsd">
                <in xmlns="">{_in}</in>
            </echoInt> ;
        var response = echoService._call(request);
        return /* xs:int */ parseInt(response["return"]);
    }

echoService.echoString =
    function echoString(/* xs:string */ _in)
    {
        var request = 
            <echoString xmlns="http://echo.services.wsas.wso2.org/xsd">
                <in xmlns="">{_in}</in>
            </echoString> ;
        var response = echoService._call(request);
        return /* xs:string */ response["return"].toString();
    }



// WebService object.
function WebService(endpoint)
{
    this._endpoint = endpoint;
    
    // private helper functions
    this._WSRequest = new SOAPHttpRequest();
    this._call =
    function callWS(reqContent)
    {
        netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
        
        var option = new Array();
        option = {useSOAP:true};
        this._WSRequest.open(option, this._endpoint, false);
        this._WSRequest.send(reqContent.toXMLString());
       
        //responseXML contains response dom 
        try {
            var resultContent = this._WSRequest.responseText;
            // convert to E4X native XML object
            if (resultContent != "") {
                return new XML(resultContent);
            } else {
                throw("no response");
            }
        } catch (e) {
            throw(e);
        }
    }				
}

// library function for parsing xs:date, xs:time, and xs:dateTime types into Date objects.
function xs_dateTime_to_date(dateTime)
{
    var buffer = dateTime;
    var p = 0; // pointer to current parse location in buffer.
    
    // parse date, if there is one.
    if (buffer.substr(p,1) == '-')
    {
        var era = -1;
        p++;
    } else {
        var era = 1;
    }
    
    if (buffer.charAt(p+2) != ':')
    {
        var year = era * buffer.substr(p,4);
        p += 5;
        var month = buffer.substr(p,2);
        p += 3;
        var day = buffer.substr(p,2);
        p += 3;
    } else {
        var year = 1970;
        var month = 1;
        var day = 1;
    }
    
    // parse time, if there is one
    if (buffer.charAt(p) != '+' && buffer.charAt(p) != '-')
    {
        var hour = buffer.substr(p,2);
        p += 3;
        var minute = buffer.substr(p,2);
        p += 3;
        var second = buffer.substr(p,2);
        p += 2;
        if (buffer.charAt(p) == '.')
        {
            var millisecond = parseFloat(buffer.substr(p))*1000;
            // Note that JS fractional seconds are significant to 3 places - xs:time is significant to more - 
            // though implementations are only required to carry 3 places.
            p++;
            while (buffer.charCodeAt(p) >= 48 && buffer.charCodeAt(p) <= 57) p++;
        } else {
            var millisecond = 0;
        }
    } else {
        var hour = 0;
        var minute = 0;
        var second = 0;
        var millisecond = 0;
    }
    
    // parse time zone
    if (buffer.charAt(p) != 'Z' && buffer.charAt(p) != '') {
        var sign = (buffer.charAt(p) == '-' ? -1 : +1);
        p++;
        var tzhour = sign * buffer.substr(p,2);
        p += 3;
        var tzminute = sign * buffer.substr(p,2);
    }
    
    var thisDate = new Date();
    thisDate.setUTCFullYear(year);
    thisDate.setUTCMonth(month-1);
    thisDate.setUTCDate(day);
    thisDate.setUTCHours(hour);
    thisDate.setUTCMinutes(minute);
    thisDate.setUTCSeconds(second);
    thisDate.setUTCMilliseconds(millisecond);    
    thisDate.setUTCHours(thisDate.getUTCHours() - tzhour);
    thisDate.setUTCMinutes(thisDate.getUTCMinutes() - tzminute);
    return thisDate;
}
