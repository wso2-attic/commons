
ReadMe.txt
--------------

Issue
-----
In ESB 4.7.0 the response message is corrupted when the message size is larger than what is set as the paramter io_buffer_size ( default 16K). Refer[2].


Setup
--------
1.) Follow the instructions as referd in [1].

Steps
------
1.) Send a request to a proxy follows. 

curl -v -X POST -H "Content-Type: text/xml" -H "SOAPAction:urn:buyStocks" -H "valid:true" -H "routing:xadmin;server1;community#1.0##" -H"Expect:" -d @requests/100K_buyStocks.xml http://localhost:8291/services/CBRProxy

Sources to refer
---------------- 
[1].) http://wso2.com/library/articles/2013/01/esb-performance-65/
2.) Refer mail staring with Performance comparison : ESB 4.8 M2 for the steaming xpath corruption issue.




