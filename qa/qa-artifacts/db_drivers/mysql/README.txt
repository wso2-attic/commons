1. Sample datasource information for MySQL

<datasource>
	<name>WSO2_CARBON_DB</name>
	<description>The datasource used for registry and user manager</description>
	<jndiConfig>
		<name>jdbc/WSO2CarbonDB</name>
	</jndiConfig>
	<definition type="RDBMS">
		<configuration>
			<url>jdbc:mysql://localhost:3306/WSO2CARBON_DB</url>
			<username>wso2carbon</username>
			<password>wso2carbon</password>
			<driverClassName>com.mysql.jdbc.Driver</driverClassName>
			<maxActive>50</maxActive>
			<maxWait>60000</maxWait>
			<testOnBorrow>true</testOnBorrow>
			<validationQuery>SELECT 1</validationQuery>
			<validationInterval>30000</validationInterval>
	   </configuration>
	</definition>
</datasource>

2. Relevant Drivers corresponding to the MySQL version* needs to be placed in $CARBON_HOME/repository/components/lib

* mysql --version
* To check Connector/J and MySQL compatibility refer below URL
http://dev.mysql.com/doc/refman/5.0/en/connector-j-versions.html
