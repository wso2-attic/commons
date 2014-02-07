1. Sample datasource information for DB2

	<datasource>
		<name>WSO2_CARBON_DB</name>
		<description>The datasource used for registry and user manager</description>
		<jndiConfig>
			<name>jdbc/WSO2CarbonDB</name>
		</jndiConfig>
		<definition type="RDBMS">
			<configuration>
				<url>jdbc:db2://localhost:50000/mb</url>
				<username>wso2carbon</username>
				<password>wso2carbon</password>
				<driverClassName>com.ibm.db2.jcc.DB2Driver</driverClassName>
				<maxActive>50</maxActive>
				<maxWait>60000</maxWait>
				<testOnBorrow>true</testOnBorrow>
				<validationQuery>SELECT 1</validationQuery>
				<validationInterval>30000</validationInterval>
			</configuration>
		</definition>
	</datasource>