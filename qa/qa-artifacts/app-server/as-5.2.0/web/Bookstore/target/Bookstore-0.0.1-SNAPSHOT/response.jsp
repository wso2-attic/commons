<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML >
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Response Page</title>
  </head>
  <body>
    <jsp:useBean id="user" scope="session" class="mypkg.UserBean" />
    <jsp:setProperty name="user" property="*" />
    <h1>Hello, <jsp:getProperty name="user" property="username" />!</h1>
  </body>
</html>