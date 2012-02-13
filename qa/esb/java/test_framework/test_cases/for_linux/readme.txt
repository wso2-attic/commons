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
<ESB_HOME>/home/esb/wso2esb-1.7</ESB_HOME>
</envt>
NOTE: You will have to set ESB_HOME as an environment variable as well

E.g.:- export ESB_HOME=/home/esb/wso2esb-1.7
       export PATH=$PATH:$ESB_HOME/bin


2. Start the Axis2 Server
-------------------------
1. Go to <ESB_HOME>/samples/axis2Server and type the commant ./axis2Server.sh to start the Axis2 Server.


3.Run the framework
-----------------
1. Run 'ant deploy.services' to deploy all the services required for the test framework
2. Run 'ant run.clients' to run all the associated clients


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
        <client>mediation/QOS/relaible_messaging/request_reply_addressable_soap11</client>
</clients>

NOTE: To see whether the messages are being sent properly when executing the reliable messaging samples, send the messages through TCPMon
