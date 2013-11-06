Throttling the VFS Listener
===========================
This feature allow to process files in a batch either using number of count (Files) or on a preset time window. Both the options cannot be use together as it confuse the process and mutually exclusive. If it set to count then thread will exit after the set count and start from the begining. If it is the procing time then thread will put it on sleep for the set time window.

A. transport.vfs.FileProcessCount

1. Start the ESB server
2. Deploy and start the Simple Stock Quote service in AXIS2 server
3. Deploy the proxy vfsThrottleCountStockQuote.xml
4. Create a VFS dir structure as below (Change the proxy accordingly as per your requirement)

VFS
|
+---Fail
+---in 
+---out
+---Pass

Place simple stock Quote request file (NOs 7) in to "in" folder (unzip TEST.ZIP)

Expected Outcome:
-----------------

* Verify the timestamp in AXIS2 sever for the generated quote to confirm whether it has been process in batches of 3

* out folder should contains the response files

* All 7 files should move to "PASS" folder after processing


B. transport.vfs.FileProcessInterval

1. Follow Step 1 & 2 above
2. Deploy the Proxy vfsThrottleTimeStockQuote.xml
3. Follow step 4 above

Expected Outcome:
-----------------
* There will be a log entry similar to below when VFS Sender and Listern transport Debug log enabled.

[2013-11-06 15:35:10,613] DEBUG - VFSTransportListener Put the VFS processor to sleep for : 10000

* Verify the timestamp in AXIS2 sever for the generated quote to confirm whether it process each file in 10000ms (10s)

* out folder should contains the response files

* All 7 files should move to "PASS" folder after processing



Reference:
1. https://redmine.wso2.com/attachments/download/50/1627.docx
2. https://redmine.wso2.com/issues/1627
3. http://docs.wso2.org/display/ESB480/VFS+Transport (Last 2 parameters under VFS Service Level Parameters)