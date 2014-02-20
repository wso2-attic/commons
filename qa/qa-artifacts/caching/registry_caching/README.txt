How to test Registry caching
============================
1. Open the log4j.properties file (E.g.:- for products based on > Carbon 4.0.0 it would be under $CARBON_HOME/repository/conf. In products based on < Carbon 4.0.0, the file is located at $CARBON_HOME/lib)
2.Add following configuration to the bottom of the file 

! Log all JDBC calls except for ResultSet calls 
! Log timing information about the SQL that is executed. 
log4j.logger.jdbc.sqltiming=DEBUG,sqltiming 
log4j.additivity.jdbc.sqltiming=false 

! the appender used for the JDBC API layer call logging above, sql timing 
log4j.appender.sqltiming=org.apache.log4j.FileAppender 
log4j.appender.sqltiming.File=./repository/logs/sqltiming.log 
log4j.appender.sqltiming.Append=false 
log4j.appender.sqltiming.layout=org.apache.log4j.PatternLayout 
log4j.appender.sqltiming.layout.ConversionPattern=-----> %d{yyyy-MM-dd HH:mm:ss.SSS} %m%n%n 

3. Open wso2greg-3.6.0/repository/conf/registry.xml & do following changes. 
I have commented two lines (jdbc URL & driver classname) & added two new lines. 

      <dbConfig name="wso2registry"> 
        <url>jdbc:log4jdbc:oracle:thin:@<<IP>>:1521:ORADB</url> 
        <!-- <url>jdbc:oracle:thin:@10.100.0.193:1521:ORADB</url> --> 
        <userName>scott</userName> 
        <password>tiger</password>          
        <driverName>net.sf.log4jdbc.DriverSpy</driverName> 
        <!-- <driverName>oracle.jdbc.OracleDriver</driverName> --> 
        <maxActive>50</maxActive> 
        <maxWait>60000</maxWait> 
        <minIdle>5</minIdle> 
    </dbConfig> 

4. Drop the attached log4jdbc4-1.2beta2.jar into wso2greg-3.6.0/repository/components/lib 

5. Boot up the server 

6. 
