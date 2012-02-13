WSO2 WSAS QA Test Framework
============================

Contents
=========

1. pre-requisites
2. Run the framework
3. Advanced configuration
4. How to add a new test


1.Pre-requisites:
---------------
1. Download and install WSO2 WSAS in your local system
2. Start WSO2 WSAS server (Run WSAS_HOME/bin/startserver.bat)
3. Configure your WSAS_HOME and ports in global_config.xml 
<envt>
<WSAS_HOME>C:\wsas\wso2wsas-SNAPSHOT</WSAS_HOME>
<hostname>localhost</hostname>
<httpport>9763</httpport>
<httpsport>9443</httpsport>
</envt>
4. In order to run reliable messaging clients, update <sandesha_version> according to the versions included in your WSAS_HOME/repository/modules directory. In WSAS-3.0 (Carbon), sandesha mar is not shipped. 
   Therefore, please download sandesha mar from http://ww2.wso2.org/~qa/sandesha-mar/ and copy it to WSAS_HOME/repository/modules directory. Please do this step after running "ant deploy.services" target.


2.Run the framework
-----------------
1. Run 'ant deploy.services' to deploy all services in to WSAS
After this step is complete, you may access WSAS management console (https://localhost:9443/carbon) and see a set of services are listed. 
2. Run 'ant run.clients' to run all the associated clients


3.Advanced configuration
-------------------------


Configure services:

The test framework allows you to define the order and choose the services to be deployed.
It can be configured in the global_config.xml.

<services>
<service>contract_first\BaseDataTypes_DocLitWrapped</service>
</services>

Configure clients:

The test framework allows you to define the order and choose the clients to be executed.
It can be configured in the global_config.xml.

<clients>
<client>pojo\echo</client>
</clients>

4. How to add a new test
-------------------------

1. If you want to add a test artifact for an existing feature, add it to a directory in TestFramework/testcases.
For example, if you want to add a test to verify asynchronus invocation with JAXWS, create a directory in 
TestFramework/testcases/jaxws.

TestFramework
	|
	 testcases
		|
		 jaxws
		     |
		      async

2. Create two seperate directories, client and service inside /TestFramework/testcases/jaxws/async
3. Add your service code into src folder in service directory
4. Add a build.xml to generate and deploy services (You can change an existing build.xml)
5. Repeat steps 3-4 with client source
6. Now you will have a directory structure as follows

TestFramework
	|
	 testcases
		|
		 jaxws
		     |
		      async
			|
			service - src, build.xml
			|
			client - src, build.xml
7. Add the relevant service and client entries to global_config.xml
<services>
<service>jaxws\async</service>
</services>

<clients>
<client>jaxws\async</client>
</clients>

Known issues of the framework
========================
1. This framework has been tested only on Windows. 
2. The framework reports "BUILD FAILED" at the end of running all tests, even if a single test fails. You can run the failed test alone and check the root cause.
3. Jaxws and spring tests were commented out due to an issue of test client.  


	 