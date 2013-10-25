Preconditions
1.the topic should create and subscribe on wso2cep
Note: the topic URL is:http://<replace-hear-with-your-IP>:9763/services/LogService/log 
2.a jms provider and a client should be used

steps
1.Go to path wso2cep-3.0.0/repository/deployment/server

2.copy the files into the relevant folder 
eventbuilders/
eventformatters/
executionplans/
inputeventadaptors/
outputeventadapto

3.start the jms consumer as ant topicConsumer -Dtopic=PurchaseOrders

4.send request from a jms provider ant -Dtopic=PurchaseOrders

