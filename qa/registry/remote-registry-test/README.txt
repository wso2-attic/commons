Remote Registry Automation test framework.
============================================

The framework for testing APP based remote registry API and some basic functional scenarios.

Directory Structure.
===================

Test-home
    |- lib <folder>
    |- src <folder>
    |- buid.xml
    |- README.txt

    lib - contains required libraries for the test execution.
    src - contains test source files.
    build.xml - ant file to executed test framework
    README.txt - File describe about the framework and installation steps.

How to INSTALL and run
======================

Prerequisite:

    a. Working installation of apache ANT.(http://ant.apache.org/manual/install.html).
    b. Extracted Registry distribution.(http://wso2.org/downloads/registry).
    c. You must be connected to the internet to use this application.
    d. If you are going to run the framework again, you must use fresh installation of registry.(fresh database)

If you met with above prerequisites then please follow the below steps.

1. Extract the RemoteRegistryTest-SNAPSHOT.zip directory into your CARBON_HOME (CARBON_HOME is the root directory of your registry installation)
2. Go to CARBON_HOME/bin and run ant inside the directory.

ant

This command will copy all the required jar files into CARBON_HOME/repository/lib

3. Start the carbon server

sh wso2server.sh (UNIX)
wso2server.bat (WINDOWS)

4. Go to test framework home CARBON_HOME/RemoteRegistryTest-SNAPSHOT
5. Then run the following ant command to execute the junit tests. Please pass the command line option -Dregistry.url, which is URL of the running registry instance.

ant run -Dregistry.url=<registry.url>

eg:
ant run -Dregistry.url=https://localhost:9443/registry/


6. Results of the test execution can be found at CARBON_HOME/RemoteRegistry-Test/results.txt

How to run Performance tests
==============================

1. Make sure above prerequisite are available.

2. Extract the RemoteRegistryTest-SNAPSHOT.zip directory into your CARBON_HOME (CARBON_HOME is the root directory of your registry installation)
3. Go to CARBON_HOME/bin and run ant inside the directory.

ant

This command will copy all the required jar files into CARBON_HOME/repository/lib

4. Start the carbon server

sh wso2server.sh (UNIX)
wso2server.bat (WINDOWS)

5. Go to test framework home CARBON_HOME/RemoteRegistryTest-SNAPSHOT
6. Then run the following ant command to execute the junit tests. Please pass the command line option -Dregistry.url, which is URL of the running registry instance.

ant run-perf -Dregistry.url=<registry.url>

eg:
ant run-perf -Dregistry.url=https://localhost:9443/registry/


Note that you can change the perf test parameters though CARBON_HOME/RemoteRegistryTest-SNAPSHOT/conf/remoteregistry.properties file.

Perf results avaialble in perf-results.txt which is under CARBON_HOME/RemoteRegistryTest-SNAPSHOT/