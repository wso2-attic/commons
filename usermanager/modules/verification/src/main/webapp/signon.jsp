<html>
  <head>
    <title>EmailRegistration</title>
  </head>

  <body>
    <h1>Email Registration</h1>
	
    <form action="register.jsp" method="GET">
      <table>
       <tr>
        	<td>Username:</td>
        	<td><input type='username' name='username'></td>
        </tr>
        <tr>
        	<td>Email Address</td>
        	<td><input type='text' name='email'></td>
        </tr>
        <tr>
        	<td>Password:</td>
        	<td><input type='password' name='password'></td>
        </tr>
       	<tr><td colspan='2'><input name="Register" type="submit"></td>
        <td colspan='2'><input name="Reset" type="reset"></td></tr>
      </table>

    </form>

  </body>
</html>
