Preconditions
1.Mysql should be installed
2.WSO2MB should be started with an offset of 1
3.the topic should create and subscribe on wso2MB
Note: the topic URL is:http://<replace-hear-with-your-IP>:9763/services/LogService/log 
4.Data Source should be configured
5.The keystore path should be give on KeyStoreUtil.java

steps
1.Go to path wso2cep-3.0.0/repository/deployment/server

2.copy the files into the relevant folder 
eventbuilders/
eventformatters/
executionplans/
inputeventadaptors/
outputeventadapto

3.Run the PubSubClient.java from eclipse
The topic for which the user subscribed should be passed with the client
