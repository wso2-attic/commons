=====================================================================================================================

Refer 

1. http://docs.wso2.org/display/AM150/Token+APIs#TokenAPIs-GeneratingusertokensGenerating
2. http://docs.wso2.org/display/IS450/SAML2+Bearer+Assertion+Profile+for+OAuth+2.0

For configuration.

If tested with a tenant, the public certificate of the keystore you used to sign the saml request, need to be exported and uploaded as the Identity Provider Public Certificate.

Generate the SAML Assertion using the java app with following command (Use your credentials)

java -jar SAML2AssertionCreator.jar SAML2AssertionCreator tester2@test.com https://localhost:9443/oauth2/token https://localhost:9443/oauth2/token wso2carbon.jks wso2carbon wso2carbon wso2carbon

Use the SAML Assertion string in the jmeter project as the SAML_Assertion under Varables

Run the project

=====================================================================================================================

