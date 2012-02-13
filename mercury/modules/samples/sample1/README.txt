This sample shows how to engage the RM and send sequence of messages.
It demostrate  InOut, InOnly and Fault Operations for Annonymous (Replay model)
and Duplex mode. Port numbers are configured to pass them through Tcpmon.
(eg start a listner at 8088 targeting 8080 for sending messages and start a listner
at 8092 targeting the 8090 for receiving messages in duplex mode)
Readers can start and stop Tcpmon channels to check for unreliability.

Building the service
====================

Download and extract the Axis2 1.4 distribution[1].
Go to the folder to which the mercury-0.91-bin.zip is extracted.
(MERCURY_HOME)
Open MERCURY_HOME/samples/sample1/server/build/build.xml
Change the axis2_home variable to correct AXIS2_HOME
run the ant script by using ant command

This should copy the SampleService.aar file to $AXIS2_HOME/repository/services folder
Copy the mercury-mar-0.91.mar to $AXIS2_HOME/repository/modules folder
Copy the mercury-core-0.91.jar to $AXIS2_HOME/lib folder

Start the Axis2 simple http servier. check the following URL for service.

http://localhost:8080/axis2/services/SampleService?wsdl

Creating Client
===============

Generate the client code
------------------------
Go to MERCURY_HOME/samples/sample1/client/bin
run the client.sh script with sh client.sh
(Just run client.bat in windows)


This generates the client stub. Make sure Simple http server is running while this command is excecute.

Create two directories, repository and modules so that it look like this
MERCURY_HOME/samples/sample1/client/repository/modules

Copy the mercury-mar-0.91.mar to MERCURY_HOME/samples/sample1/client/repository/modules
Copy the addressing-1.4.mar to MERCURY_HOME/samples/sample1/client/repository/modules

Run the InOutSample.java, InOnlySample.java and FaultSample.java using an IDE by Adding
AXIS2_HOME/lib jars and mercury-core-0.91.jar to class path.

[1] http://ws.apache.org/axis2/download/1_4/download.cgi

