Steps:

1. Start mysql server and create databases with name TomcatTestDB, TomcatCatalinaTestDB, CatalinaWebAppTestDB ,CarbonTestDB

2. Copy the catalina-server.xml and context.xml files to /repository/conf/tomcat folder
3. Add a datasource with "jdbc/CarbonTestDB" jndi name using admin console
4. Deploy the JNDISampleApp.war 
5. Invoke the application
