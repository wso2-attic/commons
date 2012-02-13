WSO2 MASHUP SERVER QA Test Framework
======================================

Contents
=========

1. pre-requisites
2. Run the framework



1.Pre-requisites:
---------------
1. Download and install WSO2 Mashup server in to your local file system.
2. Start the Mashup Server and create the admin user.
3. Configure the MASHUP_HOME and AXIS_HOME in global_config.xml 
4. For one of the test cases in Email host object tests you need to do following changes in MASHUP_HOME\conf\server.xml and restart the server.
	- Add the email configurations which you are planning to use for the test.
    <EmailConfig>
        <host>smtp.gmail.com</host>
        <port>25</port>
        <username>username@gmail.com</username>
        <password>password</password>
    </EmailConfig>



2.Run the framework
-----------------
1. Run 'ant deploy.services' to deploy all services in to MS Server.
2. Run 'ant run.clients' to run all the associated clients





	 