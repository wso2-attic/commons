Use the given configuration and invoke the client. If continueParent is set to true, the log message "*****PARENT MESSAGE******" should be printed. If continueParent is false, this message will not be printed

In my sequence, I have four targets + a seperate send mediator which will be called by the parent message. 
Therefore, when you invoke this sequence, if continueParent is true, then you should see 5 logs at backend server - "Thu Jun 14 13:37:40 IST 2012 samples.services.SimpleStockQuoteService :: Generating quote for : IBM"
