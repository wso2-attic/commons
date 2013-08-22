=================================================================================

For jtds driver the connection information should be like following

<url>jdbc:jtds:sqlserver://192.168.18.13:1433;databaseName=is-chamara-local</url>
<username>tester</username>
<password>welcome12!@</password>
<driverClassName>net.sourceforge.jtds.jdbc.Driver</driverClassName>
<maxActive>50</maxActive>
<maxWait>60000</maxWait>
<testOnBorrow>true</testOnBorrow>
<validationQuery>SELECT 1</validationQuery>
<validationInterval>30000</validationInterval>

=================================================================================
=================================================================================

For sqljdbc the connection information should be like following

<url>jdbc:sqlserver://10.100.3.131:1433;databaseName=is-chamara-local</url>
<username>tester</username>
<password>welcome12!@</password>
<driverClassName>com.microsoft.sqlserver.jdbc.SQLServerDriver</driverClassName>
<maxActive>50</maxActive>
<maxWait>60000</maxWait>
<testOnBorrow>true</testOnBorrow>
<validationQuery>SELECT 1</validationQuery>
<validationInterval>30000</validationInterval>


=================================================================================
