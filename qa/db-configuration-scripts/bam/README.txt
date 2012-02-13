How to run DB configuration test framework
============================================

Applies to:
============
     WSO2 carbon framework 2.0.2
     
Prerequisite:
===============

1. copy following jars in to ANT_HOME/lib
     a.ant-contrib-1.0b2.jar (http://sourceforge.net/projects/ant-contrib/files/)
     b.xmltask-v1.15.1.jar (http://sourceforge.net/projects/xmltask/files/)

2. Create required users and databases in your RDBMS.
Note that you don't need to create databases for H2 and Derby. Databases will be created atomatically with create=true option.

3. Start the DB servers.

Limitations:
============

* Not supported for automatioc exection of oracle BAM schema script with -Dscript=false option.

Here are the steps to run ant script for DB configuration.
==========================================================

1. Checkout the ant build file from https://wso2.org/repos/wso2/trunk/commons/qa/db-configuration-scripts/bam/build.xml

2. Edit the carbon.home property in buid.xml and set it to your product installation location.

<property name="carbon.home" value="/opt/greg/wso2greg-3.0.2"/>

3. Edit the bam.schema.location property in buid.xml and set it to BAM schema file location.

<property name="bam.schema.location" value="/home/krishantha/svn/sql"/>

4. Edit database properties in build.xml.

    <property name="oracle.driver.location" value="/home/krishantha/oracle/product/10.2.0/db_1/jdbc/lib/ojdbc14.jar"/>
    <property name="oracle.userName" value="wso2"/>
    <property name="oracle.password" value="wso2"/>
    <property name="oracle.connection.url" value="jdbc:oracle:thin:@krishantha:1523:registry"/>
    <property name="oracle.dialet" value="oracle"/>
    <property name="oracle.driver.name" value="oracle.jdbc.driver.OracleDriver"/>

5. Run ant commands.

ant commands
=============
Usage: ant [command] [system-properties]

startServerUnix          :Start the sever on Linux.
startServerWindows       :Start the sever on Windows.
startServer              :Start server without configuring databases.

system-properties:
    -Dscript             Option to run though script or -Dsetup option, set 'true' for script execution and
                         'false' to run with -Dsetup.

    -Ddatabase.name      Name of the database, available options are oracle, h2, mysql, mssql, derby.

Eg: 
ant startServerUnix -Dscript=true -Ddatabase.name=mysql       --command to start the server on Linux with mysql
                                                                by running the db script.
                                                                
ant startServerWindows -Dscript=false -Ddatabase.name=mssql  --command to start the server on Windows with mssql
                                                                using -Dsetup option.

note that -Dscript=false option doesn't support for execting oracle bam schema files.






