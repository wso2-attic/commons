ESB SAMPLE TEST FRAMEWORK
--------------------------

Prerequisites :

Following prerequisites needed to run esb sample test framework.
1. Test framework applies to WSO2 ESB 4.0.3 - higher
2. Maven 2.2.0 - Higher
3. Ant 1.7.1 - Higher


Run ESB-Sample-Test-Framework :

Following steps need to be taken to run esb-sample-test-framework
1. Shutdown all ESB and axis2 servers if you are running already
1. Open esb.properties file located in "esb-test-framework/modules/integration/src/test/resources"
2. Change the properties.

   carbon.home      - ESB binary location (/home/chamara/wso2/project/automation/esb/wso2esb-4.0.3)
   host.name        - IP address or host name of esb running instance (localhost)
   nhttp.port       - nhttp port of ESB instance (8280)
   nhttps.port      - nhttps port of ESB instance (8243)
   https.port       - Servlet https port of ESB instance (9443)
   user.name        - Administrator user name (admin)
   admin.password   - Administrator password (admin)
   context.root     - web context root (esb)


3. Run pom.xml located in esb-sample-test-framework home.
   FrameworkHome> mvn clean install

NOTE :
 Please note that no need to start ESB or AXIS2 servers manually before run this framework. All necessary servers will start automatically during the running of this framework.
 As a limitation framework not scalable to test ESB instances hosted in remote location. Axis2/Derby and other necessary servers will starts within the framework during the framework running.
 Hence you no need to setup above servers.
   
