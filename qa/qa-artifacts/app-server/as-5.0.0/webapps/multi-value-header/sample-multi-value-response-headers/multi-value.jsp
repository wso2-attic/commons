<%-- 
    Document   : This returns multiple transport header values
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>This is a servlet which returns multi-value headers</h1>
<%        
request.getSession(true).setAttribute("foo", "bar");
response.addHeader("Header", "Header1");
response.addHeader("Header", "Header2");
response.addHeader("Header", "Header3");
%>
    </body>
</html>
