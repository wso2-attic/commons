How to run the sample
=====================

Pre-requisites.
---------------
1. Start ActiveMQ server
2. Drop the required activeMQ jars at ESB and WSAS/Simple Axis2 Server end
3. Create two quese through ActiveMQ console
	a) JMSTempQueue
	b) SimpleTempQueue

1. Deploy the archive file given @archives location at your backend server.
2. Copy the axis2.xml files as per mentioned in the README.txt inside @config_files location
3. Execute the Java client given @client folder.

If the scenario is successful, you will see a order being placed through the backend service console as shown below.

************************************************************************************************************************************
*Fri Feb 10 16:20:37 IST 2012 samples.services.SimpleStockQuoteService  :: Accepted order #8 for : 1000 stocks of MSFT at $ 145.222*
************************************************************************************************************************************

