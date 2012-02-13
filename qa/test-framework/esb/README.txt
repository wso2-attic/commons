
========================================================================================
							ESB Testing Framework
========================================================================================

Constance 
-----------
	* Prerequisites
	* Installing Ant-contrib
	* Running the framework
	* Adding Test Cases

Prerequisites
---------------
	* Ant-contrib
	* and all the Prerequisites to run WSO2 ESB


Installing Ant-contrib
-----------------------
	Download Ant-contrib from; 
	http://sourceforge.net/projects/ant-contrib/files/

	Copy the 'ant-contrib-x.x.jar' to the 'lib' directory of your Ant installation. 
	(E.g. /usr/share/ant/lib/)


Running the framework
----------------------
	1. Extract 'wso2esb-3.0.0'
	2. Go to the directory'test-framework/esb'
	3. The ANT command according to the deploying environment

		ant -DtestList=[the test-case numbers divides by comma] \
			-DesbHome=[ESB_HOME] \
			-DactivemqPath=[Path to the folder which contains apache-activemq-x.x.x-bin.tar.gz] \	#(Only for JMS related testing)
			-DactivemqV=[Version of the Apache ActiveMQ used] 										#(Only for JMS related testing)
			
		E.g.

		ant -DtestList=0,1,250 -DesbHome=/home/suho/esb/wso2esb-3.0.0 -DactivemqPath=/home/suho/esb/jms -DactivemqV=5.3.1 

	4. For further configuration  edit the "build.xml" situated in "test-framework/esb/"
	   Edit these properties when log entries, or the time need for the processes is changed.

		<!--Default properties-->
		<property name="esb-start-indicator" value="StartupFinalizerServiceComponent WSO2 Carbon started in"/>
		<property name="axis2server-start-indicator" value="HttpCoreNIOListener HTTP Listener started on port"/>
		<property name="activemq-start-indicator" value=") started"/>
		<property name="clientResSec" value="3"/>
		<property name="esb-max-time-sec" value="60"/>
		<property name="axis2server-max-time-sec" value="40"/>
		<property name="activemq-max-time-sec" value="40"/>


Adding Test Cases
------------------

To add a new test-case, first create a Directory in "test-framework/esb/test-suite" with the name "test-case-[the number of the new test-case]".
Then inside the new directory copy the 'sample.xml' and rename it as 'build.xml'

Then follow the given instructions in that file to construct a proper test-case


