Following steps need to be taken for run ESB Test framework

ESB Binary Pack Test
--------------------

1. Download WSO2 ESB binary package from http://wso2.com/products/enterprise-service-bus/
2. Extract binary package
3. Add the necessary services to the axis2server. To add this,
    run 'ant' command from the directories
        ESB_HOME/samples/axis2Server/src/FastStockQuoteService
        ESB_HOME/samples/axis2Server/src/ReliableStockQuoteService
        ESB_HOME/samples/axis2Server/src/SecureStockQuoteService
        ESB_HOME/samples/axis2Server/src/MTOMSwASampleService
        ESB_HOME/samples/axis2Server/src/SimpleStockQuoteService

4. Start the axis2server on port 9000 (with default configurations)
5. Start ESB with default configurations
6. Add ESB_HOME to carbon.home in the component-test-framework/config/framework.properties
    e.g.
        for linux
        ----------
        carbon.home = /home/wso2/esb/wso2esb

        for windows
        ------------
        carbon.home = C:\\Documents and Settings\\proj\\wso2\\esb\\wso2esb\\


7. If you want to run full test framework,do "mvn clean install -Dproduct=esb" component-test-framework directory.
8. If you want to run individual test module,build following dependency modules before that.
    authenticator
    synapse-config-admin
    common
    servicemanagement
    resourceadmin
    logviewer
    securitymanagement
    test-setup-config
    throttleadmin
    proxyservices
    sequences
    endpoints



Stratos ESB Test
----------------

1. set following properties in framework.properties file

# Testing server details
#---------------------------------------------------------------------------------------------------
host.name= stratos-esb name                                     Eg: esb.cloud.wso2.com
https.port = stratos-esb HTTPS port                             Eg: 8243
http.port = stratos-esb HTTP port                               Eg: 8280
server.username = stratos User name
server.password = password
carbon.home = /component-test-framework/lib/stratos-artifacts
#---------------------------------------------------------------------------------------------------
#Backend server details
#---------------------------------------------------------------------------------------------------
backendserver_host.name= hosted service url                     Eg: appserver.cloud.wso2.com
#---------------------------------------------------------------------------------------------------
#Stratos related properties
#---------------------------------------------------------------------------------------------------
stratos = true  (if you are testing stand along pack make it as a "false")
tenant.name =  tenant id                                        Eg: t/esbtest.com
supertenant.username = tenant super admin user name             Eg: admin
supertenant.password = tenant super admin password              Eg: abc123
#---------------------------------------------------------------------------------------------------
# P2 Repository
#---------------------------------------------------------------------------------------------------
p2.repo=P2 repo location                                        Eg: http://builder.wso2.org/~carbon/releases/carbon/3.0.1/RC3/p2-repo/

2. Start wsas service and upload SimpleStockQuoteService.aar (service provided inside lib directory)
3. Enter "mvn clean install -Dproduct=stratos_esb" in command window.
