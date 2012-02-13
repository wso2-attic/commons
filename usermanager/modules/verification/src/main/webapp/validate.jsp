<%@ page import="org.wso2.usermanager.verification.email.EmailVerifier" %>
<%@ page import="org.wso2.usermanager.UserManagerException" %>
<html>
  <head>
    <title>validation</title>
  </head>
	<body>
		<%
			String value = request.getParameter("confirmation");
			boolean isVerified = false;
			if(value != null){
				try {
					EmailVerifier verifier = new EmailVerifier();
					isVerified = verifier.confirmUser(value);
				} catch (UserManagerException e) {
		%> 
					<h2>Error validating you. Please try again later</h2>
		<%	
				}
			}
			
			if(isVerified){
		%>
			<h2>You are successfully validated. You can login now</h2>
		<%		
			}else{
		%>
			<h2>Your request is invalid or expired.</h2>
			<p>To register again go<a href="signon.jsp">here</a></p>
		<%	
			}
		%>
	</body>
</html>
