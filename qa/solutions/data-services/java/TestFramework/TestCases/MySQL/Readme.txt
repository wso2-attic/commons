Instruction to run Dataservices Test FrameworK
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Prerequisites
~~~~~~~~~~~~~
1. Install Apache ant
2. Install MySQL with username and password 
3. Change TestFramework\TestCases\MySQL\global_config.xml as follows.
	
	1.1 <DS_HOME>Dataservice Installation Folder path</DS_HOME>
	1.2 <hostname>Machine IP</hostname>
	1.3 <httpport>9763</httpport>
	1.4 <httpsport>9443</httpsport>

3.1. Change following properties for configure database settings.

	3.1.1 <DBURL>jdbc:mysql://MachineIP:3306</DBURL>
	3.1.2 <UserID>MySQL Username</UserID>
	3.1.3 <Password>MYSQL Password</Password>

4.Copy MySQL driver (Ex: mysql-connector-java-5.1.5-bin.jar) in to lib\extentions folder in dataservices home directory.

5. Run Test Framework
   ~~~~~~~~~~~~~~~~~~
	4.1.To setup sample database run "Generate.database" target in "TestCases\MySQL" build.xml
	4.2 To Copy Test Dataservice file run "deploy.services" target in "TestCases\MySQL" build.xml
	4.3 Start WSO2 Data Services Server.
	4.3 To Run Test Framework execute "run.clients" target in "TestCases\MySQL" build.xml

NOte:- you should grant executable permissions to test framework ,If you are using Linux based OS.