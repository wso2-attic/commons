=================================================================================

For ojdbc drivers the connection information should be like following

<url>jdbc:oracle:thin:@10.100.3.227:1521/daniddb</url>
<username>ischamaralocal</username>
<password>ischamaralocal</password>
<driverClassName>oracle.jdbc.driver.OracleDriver</driverClassName>
<maxActive>50</maxActive>
<maxWait>60000</maxWait>
<testOnBorrow>true</testOnBorrow>
<validationQuery>SELECT 1 FROM DUAL</validationQuery>
<validationInterval>30000</validationInterval>

=================================================================================

