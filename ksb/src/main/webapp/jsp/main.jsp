<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
"http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>WSO2 Keystore Browser</title>
<link rel="stylesheet" href="css/styles.css" type="text/css">
</head>
<body>
<div class="header-back">
	<table cellpadding="0" cellspacing="0" border="0" class="menu">
		<tr>
			<td><img src="images/icon-home.gif" align="top" hspace="10"  /> <a href="ShowMain.action">Home</a></td>
			<td><img src="images/icon-listkeys.gif" align="top" hspace="10"  /><a href="ShowMain.action">List Keystores</a></td>
			<td><img src="images/icon-convertkeys.gif" align="top" hspace="10" /><a href="startConvert.action">Convert Keystore</a></td>
		</tr>
	</table>
</div>
<div class="content">
<h1>WSO2 Keystore Browser</h1>

	
		<s:form action="UploadKeyStore" method="POST" enctype="multipart/form-data">
			<s:file name="keyStoreFile" label="Keystore file"/>
			<s:select label="Store type"
			       name="storeType"
			       headerKey="01" headerValue="JKS"
			       list="#{'01':'JKS', '02':'PKCS12'}"
			       value="storeType"
			       required="true"
			/>
			<s:password label="Store password" name="storePasswd" />
			<s:submit type="button" cssClass="button" label="Upload"/>
			
		</s:form>
	
</div>

<div class="content-results">
	    <s:iterator value="keyStoreDescriptions">
			<h3>	
				Keystore :<s:property value="name" />			
				Type :<s:property value="storeType" />
			</h3>
	        
	        	<h2>Certificates :</h2>
	        	
	        	<s:iterator value="certificates">
		        	<table cellpadding="0" cellspacing="0" border="0" class="entryBox">
		        		<tr><td colspan="2" class="table-top">Alias : <s:property value="alias"/></td></tr>
				        <tr><td class="attribute-name">Subject : </td><td><s:property value="cert.SubjectDN"/></td></tr>
					<tr><td class="attribute-name">Issuer : </td><td><s:property value="cert.IssuerDN"/></td></tr>
					<tr><td class="attribute-name">Serial number : </td><td><s:property value="cert.SerialNumber"/></td></tr>
					<tr><td class="attribute-name">Version : </td><td><s:property value="cert.Version"/></td></tr>
					<tr><td class="attribute-name">Not before : </td><td><s:date name="cert.NotBefore" format="dd/MM/yyyy" /></td></tr>
					<tr><td class="attribute-name">Not after : </td><td><s:date name="cert.NotAfter" format="dd/MM/yyyy" /></td></tr>
				</table>
			</s:iterator>
			
			<h2>Private Keys :</h2>
			<s:iterator value="privateKeys">
			<table cellpadding="0" cellspacing="0" border="0" class="entryBox">
	        		<tr>
	        			<td colspan="2" class="table-top">
						<s:url id="PEMUrl" namespace="/" action="privKeyPasswdForPEMUrl">
						    <s:param name="alias" value="%{alias}" />
						    <s:param name="ksId" value="%{uuid}" />
						</s:url>
				
						Alias : <s:property value="alias"/>
						<form method="post" action="ShowPEMPrivateKey.action?alias=<s:property value="alias"/>&ksId=<s:property value="uuid"/>">
							<input type="password" name="keyPasswd"/>
							<input type="submit" value="PEM"/>
						</form>
					</td>
				</tr>
				<tr><td class="attribute-name">Subject : </td><td><s:property value="cert.SubjectDN"/></td></tr>
				<tr><td class="attribute-name">Issuer : </td><td><s:property value="cert.IssuerDN"/></td></tr>
				<tr><td class="attribute-name">Serial number : </td><td><s:property value="cert.SerialNumber"/></td></tr>
				<tr><td class="attribute-name">Version : </td><td><s:property value="cert.Version"/></td></tr>
				<tr><td class="attribute-name">Not before : </td><td><s:date name="cert.NotBefore" format="dd/MM/yyyy"/></td></tr>
				<tr><td class="attribute-name">Not after : </td><td><s:date name="cert.NotAfter" format="dd/MM/yyyy"/></td></tr>
			</table>
			</s:iterator>
			
			<br/>
	    </s:iterator>
</div>
<div class="footer">&copy 2008 <a href="http://www.wso2.org">WSO2</a> Inc.</div>

</body>
</html>
