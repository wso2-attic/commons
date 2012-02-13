<html>
<body>
<h2>Simple JSON</h2>
<%
 var json = {"name":"Yumani","company":"WSO2"};
 print("<p>Name: "+json.name+"</p>");
 print("<p>Company: "+json.company+"</p>");
%>
<h2>Nested JSON</h2>
<%
var myJSON= {"name":"Yumani",
		     "company":"WSO2",
		     "address":
		               {"no":"59",
		    	 		"street":"Flower road",
		    	 		"city":"Colombo-07",
		    	 		"country":"Sri Lanka"
		    	 		}
			};
print("<p>Name    : "+myJSON.name+"</p>");
print("<p>Address : "+myJSON.address.country+"</p>");
%>

</br>
<h2>Arrayed JSON</h2>
<%
var myJSONObject = {"bindings": [
{"ircEvent": "PRIVMSG", "method": "newURI", "regex": "^http://.*"},
{"ircEvent": "PRIVMSG", "method": "deleteURI", "regex": "^delete.*"},
{"ircEvent": "PRIVMSG", "method": "randomURI", "regex": "^random.*"}
]
};

print (myJSONObject.bindings[0].method);
%>


</body>
</html>
