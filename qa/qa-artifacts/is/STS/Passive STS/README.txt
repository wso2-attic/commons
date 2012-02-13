Set up :
---------
Following article contains instructions to configure the setup

http://wso2.org/library/articles/2011/12/configuring-wso2-identity-server-passive-sts-aspnet-client

Special note:
-------------
In addtion to the above blog, please go through the instructions given below.

1) Import .pfx type certificate instead of .cer when importing the certificates (you created) to certificate store through mmc.

2) You have to install both .NET 3.5 and 4.0 versions.

3) When you browse the resource (https://localhost/RPWebApp), if you get the exception given below you have to grant permission for users.

Description: An error occurred during the processing of a configuration file required to service this request. Please review the specific error details below and modify your configuration file appropriately.

Parser Error Message: ID1024: The configuration property value is not valid.
Property name: 'serviceCertificate'
Error: 'ID1039: The certificate's private key could not be accessed. Ensure the access control list (ACL) on the certificate's private key grants access to the application pool user.
Thumbprint: 'EFA9097853E44E7D317283F413E36EB73354EF27''

Source Error:

Line 98:   </system.diagnostics>-->
Line 99:   <microsoft.identityModel>    
Line 100:    <service>      
Line 101:      <audienceUris mode="Never">        
Line 102:        <add value="https://localhost/RPWebApp/" />


Source File: C:\Users\pavithra\Downloads\RPWebApp\web.config    Line: 100


-Provided access to NETWORK SERVICE and everyone through mmc -> Personal -> Certificates -> my certificate -> Manage private keys. (This should be performed for the .pfx type certificte in certificate store)
- Go to IIS manager -> Application pools. In set application pool defaults, under process model section default value for identity is set to ApplicationPoolIdentity. Change this value to LocalSystem

4) When browsing the resource you have to be careful of the endslash provided in url. If you don't add/remove the slash accordingly, you'll be given an error page.
