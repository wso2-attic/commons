Deploying archive file
=======================

Deploy this service at your backend.

This service has service parameter defined as follows.


<parameter locked="false" name="transport.jms.ConnectionFactory">myQueueConnectionFactory</parameter>
<parameter locked="false" name="transport.jms.Destination">dynamicQueues/SimpleTempQueue</parameter>
