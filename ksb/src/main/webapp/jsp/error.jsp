<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
"http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>WSO2 Keytstore Converter</title>
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
<div class="content">
<h1>Java Keystore (JKS) / Personal Information Exchange (PFX)</h1>
<h1> Error has occurred :( </h1>
    <p>
      <s:property value="%{exception.message}"/>
    </p>    
</div>
<div class="footer">&copy 2008 <a href="http://www.wso2.org">WSO2</a> Inc.</div>
</body>
</html>
