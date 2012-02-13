WSO2 ESB QA Test Framework
============================

Contents
=========

1. Pre-requisites
2. Start the Axis2 server
3. Run the framework
4. Advanced configuration


1.Pre-requisites:
---------------
1. Download the latest WSO2 ESB release from http://wso2.org/downloads/esb and extract it to a prefered location
2. Configure your ESB_HOME in global_config.xml 
<envt>
<ESB_HOME>C:\esb\wso2esb-2.0-SNAPSHOT</ESB_HOME>
</envt>
NOTE: You will have to set ESB_HOME as a windows environment variable as well

.





2. Start the Axis2 Server

-------------------------

1. Go to <ESB_HOME>/samples/axis2Server and type the command axis2Server.bat to start the Axis2 Server.




3.Run the framework
-----------------
1. Run 'ant deploy.services' to deploy all the services required for the test framework
2. Run 'ant clients' to run all the associated clients

NOTE: Currently when each test case is being executed the wso2carbon server will be started and you will have to manually shutdown the server once the client receives the response. If not the rest of the sample will not execute successfully. A patch to resolve this will be sent in the near future. 



4.Advanced configuration
-------------------------
Configuring services:

The test framework is written in such a way that it lets the user to specify the order which services to be deployed.
It can be configured in the global_config.xml.

<services>
        <service>services/rm</service>
</services>

Configure clients:

The test framework allows you to define the order and choose the clients to be executed.
It can be configured in the global_config.xml.

<clients>
        <client>mediation\transformation\xquery</client>
</clients>

NOTE: To see whether the messages are being sent properly when executing the reliable messaging samples, send the messages through TCPMon
