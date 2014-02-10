package org.wso2.mbtest.queue.ssl;/*
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

public class QueueSenderSSL {
    public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
    private static final String CF_NAME_PREFIX = "connectionfactory.";
    private static final String QUEUE_NAME_PREFIX = "queue.";
    private static final String CF_NAME = "qpidConnectionfactory";
    String userName = "admin";
    String password = "admin";
    String queueName = "kumara";
    QueueSession queueSession;
    QueueConnection queueConnection;
    MessageProducer queueSender;

    public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
    	QueueSenderSSL queueSender = new QueueSenderSSL();
        try {
            queueSender.sendMessages(10);
        } catch (TransactionRolledbackException e) {
            e.printStackTrace();
        }
    }
    public void sendMessages(int count) throws NamingException, JMSException, TransactionRolledbackException, InterruptedException {
        try{
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
            properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
            properties.put(QUEUE_NAME_PREFIX + queueName, queueName);
            System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
            InitialContext ctx = new InitialContext(properties);

            // Lookup connection factory
            QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
            queueConnection = connFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue)ctx.lookup(queueName);
            queueSender = queueSession.createProducer(queue);
            queueConnection.start();

            for (int j=0; j<count; j++){
                TextMessage tx = queueSession.createTextMessage("Message " + j);
                System.out.println("Writing  messages  " + j);
                queueSender.send(tx);
                Thread.sleep(1000);
            }
            queueConnection.close();
            queueSession.close();
            queueSender.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            
        }
       
    }
    public String getTCPConnectionURL(String username, String password) {
        // amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}' ?ssl='true'
    	String url = "amqp://admin:admin@carbon/carbon?failover='roundrobin?cyclecount='2''&sync_ack='true'&brokerlist='tcp://204.13.85.2:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box1/box1keystore.jks'&key_store_password='asanka';tcp://204.13.85.3:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box2/box2keystore.jks'&key_store_password='asanka';tcp://204.13.85.4:8682?retries='5'&connectdelay='2000'&ssl='true'&ssl_cert_alias='RootCA'&trust_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3truststore.jks'&trust_store_password='asanka'&key_store='/home/asankav/servers/support/Time2/WILLIAMSDEV-12/keystore/box3/box3keystore.jks'&key_store_password='nuwan123''";


        return url;
    }

}