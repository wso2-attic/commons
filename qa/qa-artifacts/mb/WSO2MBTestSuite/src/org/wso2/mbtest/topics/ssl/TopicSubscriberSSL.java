package org.wso2.mbtest.topics.ssl;/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
public class TopicSubscriberSSL {
	public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin";
    String password = "admin";
    String topicName = "asanka_topic14";
    TopicSession topicSession;
    TopicConnection topicConnection;

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
        
    	TopicSubscriberSSL queueReceiver2 = new TopicSubscriberSSL();
        queueReceiver2.receiveMessages();

    }
    public void receiveMessages() throws NamingException, JMSException, InterruptedException {
        try{
        	 Properties properties = new Properties();
 	        properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
 	        properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
 	        System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
 	        InitialContext ctx = new InitialContext(properties);
 	        // Lookup connection factory
 	        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
 	        TopicConnection topicConnection = connFactory.createTopicConnection();
 	        topicConnection.start();
 	         topicSession =
 	                topicConnection.createTopicSession(false,QueueSession.AUTO_ACKNOWLEDGE);
 	        Topic topic = topicSession.createTopic(topicName);
 	       
 	        javax.jms.TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            TextMessage message;
            int messageCount = 1;
            while ((message = (TextMessage) topicSubscriber.receive()) != null) {
                System.out.println(messageCount+"------"+message.getText() + " timestamp: " + System.currentTimeMillis()/1000);
                messageCount++;
            }

        }catch (Exception e){
            System.out.println(e);

        } finally {
            
            topicSession.close();
            topicConnection.close();
        }


    }



    public String getTCPConnectionURL(String username, String password) {
    	
    	String url = "amqp://admin:admin@carbon/carbon?brokerlist='tcp://204.13.85.3:5682'";
    	//String url = "amqp://admin:admin@carbon/carbon?failover='roundrobin?cyclecount='2''&sync_ack='true'&brokerlist='tcp://204.13.85.2:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1keystore.jks'&key_store_password='asanka';tcp://204.13.85.3:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2keystore.jks'&key_store_password='asanka';tcp://204.13.85.4:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3keystore.jks'&key_store_password='nuwan123''";


        return url;
        
    }
    
}