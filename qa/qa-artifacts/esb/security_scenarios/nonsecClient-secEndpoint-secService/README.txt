How to execute the scenario
===========================
- Drop the PasswordCallbackHandler.jar to ESB_HOME/repository/components/lib and restart the server
- Deploy the Axis2ServiceSoap11BindingPolicy_Sce3.xml under local entries
- Deploy the NonSecClientSecServiceProxy.xml as a proxy
- Deploy Axis2Service in any application server and apply security scenario 3
- Invoke the proxy from a non-secured client

P.S. 
a) Use the serviceks.jks to secure the Axis2Service.
b) Use the client.jks as the keystore withing the Axis2ServiceSoap11BindingPolicy_Sce3.xml policy
