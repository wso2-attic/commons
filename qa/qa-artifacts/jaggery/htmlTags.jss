<html>
<head><title>HTML tags within <% %></title></head>
<body>
<center><h2>Hi Hi ...</h2></center>
<%
print("<i>italic</i></br>");
print("<b>bolded</b></br>");
print("<big>Big letters</big></br>");
print("<small>small letters</small></br>");
print("<cite>Citation</cite></br>");
print("<em>Emphasized text</em><br />");
print("<strong>Strong text</strong><br />");
print("<dfn>Definition term</dfn><br />");
print("<code>A piece of computer code</code><br />");
print("<samp>Sample output from a computer program</samp><br />");
print("<kbd>Keyboard input</kbd><br />");
print("<var>Variable</var><br />");
print("<cite>Citation</cite><br />");
print("<caption>caption</caption></br>");
%>

</br>
<%
print("<dir><li>html</li><li>xhtml</li><li>css</li></dir>");
%>

<br>
<%
print("<div style=\"background: green\"><h3>This is a header</h3><p>This is a paragraph.</p></div>");
%>

</br>

<%
print("<table border=\"1\"><tr><th>Month</th><th>Savings</th></tr><tr><td>January</td><td>$100</td></tr><tr><td>February</td><td>$80</td></tr></table>");
%>

</br>

<%
print("<form>First name: <input type=\"text\" name=\"firstname\"/><br/>Last name: <input type=\"text\" name=\"lastname\" /></form>");
%>

</br>
<%
print("<form><input type=\"checkbox\" name=\"vehicle\" value=\"Bike\" /> I have  bike<br /><input type=\"checkbox\" name=\"vehicle\" value=\"Car\" /> I have a car </form>");

%>



</body>
</html>