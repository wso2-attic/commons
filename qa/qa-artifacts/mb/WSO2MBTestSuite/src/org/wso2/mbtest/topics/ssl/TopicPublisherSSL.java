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
import javax.transaction.TransactionRolledbackException;
import java.util.Properties;

public class TopicPublisherSSL {
    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin";
    String password = "admin";
    String topicName = "asanka_topic14";
    TopicSession topicSession;
    TopicConnection queueConnection;
    TopicConnection topicConnection;


    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
    	TopicPublisherSSL queueSender = new TopicPublisherSSL();
        try {
            queueSender.sendMessages(1000);
        } catch (TransactionRolledbackException e) {
            e.printStackTrace();
        }
    }
    public void sendMessages(int count) throws NamingException, JMSException, TransactionRolledbackException, InterruptedException {
        try{
        	 Properties properties = new Properties();
             properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
             properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
             System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
             InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
             topicConnection = connFactory.createTopicConnection();
            topicConnection.start();
             topicSession = topicConnection.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            // Send message
            Topic topic = topicSession.createTopic(topicName);
            javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);

            for (int j=0; j<count; j++){
                TextMessage tx = topicSession.createTextMessage("Message " + j);
                System.out.println("Writing  messages  " + j);
                topicPublisher.send(tx);
                Thread.sleep(100);
                
                
            }
            topicConnection.close();
            topicSession.close();
            
        } catch (Exception e){
            System.out.println(e.getMessage());
            
        }
       
    }
    public String getTCPConnectionURL(String username, String password) {
        
    	String url = "amqp://admin:admin@carbon/carbon?brokerlist='tcp://204.13.85.3:5682'";
    	//String url = "amqp://admin:admin@carbon/carbon?failover='roundrobin?cyclecount='2''&sync_ack='true'&brokerlist='tcp://204.13.85.2:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1keystore.jks'&key_store_password='asanka';tcp://204.13.85.3:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2keystore.jks'&key_store_password='asanka';tcp://204.13.85.4:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3keystore.jks'&key_store_password='nuwan123''";


        return url;
    }

}