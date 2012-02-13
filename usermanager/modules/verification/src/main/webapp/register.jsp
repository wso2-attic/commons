<%@ page import="org.wso2.usermanager.verification.email.EmailVerifier"%>
<html>
  <head>
    <title>UserMessage</title>
  </head>

  <body>
  	<%
  		String email =  request.getParameter("email");
  		String password = request.getParameter("password");
  		String username =  request.getParameter("username");

  		EmailVerifier verifier = new EmailVerifier();
		verifier.requestUserVerification(username, email, password, null);
  	%>
    <h2>Please check mail and click on the link sent</h2>
  </body>
</html>
