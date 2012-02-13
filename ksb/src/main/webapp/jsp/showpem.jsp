<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
"http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>WSO2 Keystore Brower | Private key in PEM format</title>
<link rel="stylesheet" href="css/styles.css" type="text/css">
</head>
<body>
<div class="header-back2">
    <table cellpadding="0" cellspacing="0" border="0" class="menu">
        <tr>
            <td><img src="images/icon-home.gif" align="top" hspace="10"  /> <a href="ShowMain.action">Home</a></td>
            <td><img src="images/icon-listkeys.gif" align="top" hspace="10"  /><a href="ShowMain.action">List Keystores</a></td>
            <td><img src="images/icon-convertkeys.gif" align="top" hspace="10" /><a href="startConvert.action">Convert Keystores</a></td>
        </tr>
    </table>
</div> 
<h2>Keystore : <s:property value="storeName"/></h2>
<h2>Private key alias : <s:property value="alias"/></h2>
<hr/>
<pre>
<s:property value="pemKey"/>
</pre>
<hr/>
<a href="ShowMain.action">Back</a>

<p>&copy 2007 <a href="http://www.wso2.org">WSO2</a> Inc.</p>
</body>
</html>
