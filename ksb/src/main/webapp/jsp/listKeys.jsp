<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
"http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>WSO2 Keystore Converter</title>
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
<h1>Select the keys and certificates</h1>
    <s:form action="generateZip" theme="css_xhtml" >
    
       <s:iterator value="keyInfoList" id="bean" status="stat">   
                <s:hidden name="%{'keyInfoList['+#stat.index+'].alias'}" value="%{#bean.alias}"/>
                <s:div id="8" cssStyle="background-color:#E5E5E5; padding-top:0px;">
                <s:if test="%{#bean.privateKey}">                 
                    <s:password name="%{'keyInfoList['+#stat.index+'].password'}" label="%{#bean.alias}" labelposition="left" />
                    <s:checkbox name="%{'keyInfoList['+#stat.index+'].include'}" label="Include this key" labelposition="right" cssStyle="text-indent:20px;"/>                  
                </s:if> 
                </s:div>
                <s:else>
                    <s:label value="%{#bean.alias}"></s:label>
                    <s:checkbox name="%{'keyInfoList['+#stat.index+'].include'}" label="Include this certificate" labelposition="right"/>
                </s:else>                        	      
	   </s:iterator>	
	   <br/>   	
	   <s:submit value="Convert keystore" align="left" type="button" cssClass="button"/>
	   <br/> 
    </s:form>
</div>    
<div class="footer">&copy 2008 <a href="http://www.wso2.org">WSO2</a> Inc.</div>

</body>
</html>
