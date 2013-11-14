

################################################################################################
This project is a collection of JMS clients created for Wso2 Message Broker 2.1.0 Testing
And importantly this is not automated test suite. 
So you all have to run the clients manually as appropriately and simply knowing what you are doing.

Normally all the clients comes with following set of properties and you can customize them appropriately
   
    private static String CARBON_CLIENT_ID = "carbon";          
    private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
    private static String CARBON_DEFAULT_HOSTNAME = "localhost";   //if you MB run on a remote machine change localhost to that machine IP Address
    private static String CARBON_DEFAULT_PORT = "5672";   //Change the port appropriately if you are running with MB with Port offset
    
    String queueName = "asanka";    //Change queue name as you want
    or 
    String topicName = "SimpleStockQuoteService";  //Change topic name as you want




#################################################################################################