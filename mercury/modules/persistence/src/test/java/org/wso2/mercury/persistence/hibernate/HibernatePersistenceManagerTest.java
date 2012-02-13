/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.mercury.persistence.hibernate;

import junit.framework.TestCase;
import org.wso2.mercury.persistence.dto.*;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.persistence.PersistenceManager;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


public class HibernatePersistenceManagerTest extends TestCase {

    public static final String URL_STRING = "jdbc:derby:/home/amila/projects/mercury/modules/test/repository_client/database/MERCURY_DB";
//    public static final String URL_STRING = "jdbc:derby:/home/amila/projects/mercury/modules/test/repository_server/database/MERCURY_DB";

    private Configuration getConfiguration() {

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
        configuration.setProperty("hibernate.connection.url", URL_STRING);
        configuration.setProperty("hibernate.connection.username", "mercury");
        configuration.setProperty("hibernate.connection.password", "mercury");
        configuration.setProperty("hibernate.connection.pool_size", "1");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");

//        configuration.setProperty("hibernate.show_sql","true");
        configuration.setProperty("hibernate.hbm2ddl.auto","create");
        configuration.addResource("org/wso2/mercury/persistence/hibernate/rm.hbm.xml");

        return configuration;
    }

    public void testInternalKey() {

        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());
        InternalKeyDto internalKeyDto = null;
        List internalKeys = null;
        try {
            internalKeyDto = new InternalKeyDto("key1", "http://localhost:8088/org");
            persistenceManager.save(internalKeyDto);
            internalKeyDto = new InternalKeyDto("key2", "http://localhost:8088/org");
            persistenceManager.save(internalKeyDto);

            internalKeys = persistenceManager.getInternalKey("key1", "http://localhost:8088/org");
            // list down the objects
            displayKeys(internalKeys);
            internalKeys = persistenceManager.getInternalKey("key2", "http://localhost:8088/org");
            // list down the objects
            displayKeys(internalKeys);
        } catch (PersistenceException e) {
            fail();
        }

    }

    private void displayKeys(List internalKeys) {
        InternalKeyDto key;
        for (Iterator iter = internalKeys.iterator(); iter.hasNext();) {
            key = (InternalKeyDto) iter.next();
            System.out.println("ID ==> " + key.getId());
        }
    }

    public void testRMSSequenceDTO() {

        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());
        RMSSequenceDto rmsSequenceDto = new RMSSequenceDto();
        rmsSequenceDto.setSequenceID("testID");
        rmsSequenceDto.setState(2);
        rmsSequenceDto.setMessageNumber(234);
        rmsSequenceDto.setLastMessageNumber(23);
        rmsSequenceDto.setEndPointAddress("http://localhost:9090");
        rmsSequenceDto.setAckToEpr("http://localshost");
        rmsSequenceDto.setSequenceOffer("testOffer");
        rmsSequenceDto.setStartTime(234);
        rmsSequenceDto.setEndTime(234);
        rmsSequenceDto.setLastAccessedTime(234);
        rmsSequenceDto.setInternalKeyID(5);

        Axis2InfoDto axis2InfoDto = new Axis2InfoDto();
        axis2InfoDto.setServiceName("testService");
        axis2InfoDto.setCurrentHanlderIndex(2);
        axis2InfoDto.setCurrentPhaseIndex(3);

        RMSSequenceDto result = null;
        try {
            persistenceManager.save(rmsSequenceDto, axis2InfoDto);
            result = persistenceManager.getRMSSquenceWithID(rmsSequenceDto.getId());
            assertEquals(result.getSequenceID(), "testID");
            assertEquals(result.getState(), 2);
            rmsSequenceDto.setState(3);
            rmsSequenceDto.setSequenceID("testNewID");
            persistenceManager.update(rmsSequenceDto);
            result = persistenceManager.getRMSSquenceWithID(rmsSequenceDto.getId());
            assertEquals(result.getSequenceID(), "testNewID");
            assertEquals(result.getState(), 3);
        } catch (PersistenceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void testRMSMessageDTO() {
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        RMSSequenceDto rmsSequenceDto = new RMSSequenceDto();
        rmsSequenceDto.setSequenceID("testID");
        rmsSequenceDto.setState(2);
        rmsSequenceDto.setMessageNumber(234);
        rmsSequenceDto.setLastMessageNumber(23);

        Axis2InfoDto axis2InfoDto = new Axis2InfoDto();
        axis2InfoDto.setServiceName("testService");
        axis2InfoDto.setCurrentHanlderIndex(2);
        axis2InfoDto.setCurrentPhaseIndex(3);

        try {
            persistenceManager.save(rmsSequenceDto, axis2InfoDto);

            RMSMessageDto rmsMessageDto1 = new RMSMessageDto();
            rmsMessageDto1.setMessageNumber(1);
            rmsMessageDto1.setLastMessage(true);
            rmsMessageDto1.setSoapEnvelpe("org");
            rmsMessageDto1.setSend(false);
            rmsMessageDto1.setRmsSequenceID(rmsSequenceDto.getId());


            RMSMessageDto rmsMessageDto2 = new RMSMessageDto();
            rmsMessageDto2.setMessageNumber(1);
            rmsMessageDto2.setLastMessage(true);
            rmsMessageDto2.setSoapEnvelpe("org");
            rmsMessageDto2.setSend(false);
            rmsMessageDto2.setRmsSequenceID(rmsSequenceDto.getId());

            persistenceManager.save(rmsMessageDto1, rmsSequenceDto);
            persistenceManager.save(rmsMessageDto2, rmsSequenceDto);

            Set testAcknowledgedMessages = new HashSet();
            rmsMessageDto1.setSend(true);
            rmsMessageDto2.setSend(true);
            testAcknowledgedMessages.add(rmsMessageDto1);
            testAcknowledgedMessages.add(rmsMessageDto2);

            persistenceManager.updateMessagesAsSend(testAcknowledgedMessages, rmsSequenceDto);
            List rmsMessageDtos = persistenceManager.getRMSMessagesWithRMSSequenceID(rmsSequenceDto.getId());
            System.out.println("RMSMessage ID ==> " + rmsMessageDto1.getId());
            displayRMSMessages(rmsMessageDtos);
            testDisplayDataBase();
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testRMDSequenceDTO(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        RMDSequenceDto rmdSequenceDto = new RMDSequenceDto();
        rmdSequenceDto.setSequenceID("testID");
        rmdSequenceDto.setState(3);
        rmdSequenceDto.setAcksTo("http://localhost");
        rmdSequenceDto.setLastMessageNumber(34);
        rmdSequenceDto.setStartTime(34);
        rmdSequenceDto.setEndTime(45);

        try {
            persistenceManager.save(rmdSequenceDto);
            testDisplayDataBase();
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testInvokerBuffer(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        RMDSequenceDto rmdSequenceDto = new RMDSequenceDto();
        rmdSequenceDto.setSequenceID("testID");
        rmdSequenceDto.setState(3);
        rmdSequenceDto.setAcksTo("http://localhost");
        rmdSequenceDto.setLastMessageNumber(34);
        rmdSequenceDto.setStartTime(34);
        rmdSequenceDto.setEndTime(45);

        InvokerBufferDto invokerBufferDto = new InvokerBufferDto();
        invokerBufferDto.setState(3);
        invokerBufferDto.setLastMessage(45);
        invokerBufferDto.setLastMessageToApplication(34);
        invokerBufferDto.setRmdSequenceID(5);

        Axis2InfoDto axis2InfoDto = new Axis2InfoDto();
        axis2InfoDto.setServerSide(true);
        axis2InfoDto.setServiceName("testservice");
        axis2InfoDto.setCurrentHanlderIndex(2);
        axis2InfoDto.setCurrentPhaseIndex(5);

        try {
            persistenceManager.save(invokerBufferDto,rmdSequenceDto,axis2InfoDto);
            testDisplayDataBase();
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testRMDMessage(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        RMDMessageDto rmdMessageDto1 = new RMDMessageDto();
        rmdMessageDto1.setMessageNumber(34);
        rmdMessageDto1.setSoapEnvelope("org soap envelope");
        rmdMessageDto1.setSend(true);
        rmdMessageDto1.setInternalBufferID(1);
        rmdMessageDto1.setOperationName(new QName("http://wos2.com/test","testelement"));

        RMDMessageDto rmdMessageDto2 = new RMDMessageDto();
        rmdMessageDto2.setMessageNumber(34);
        rmdMessageDto2.setSoapEnvelope("org soap envelope");
        rmdMessageDto2.setSend(true);
        rmdMessageDto2.setInternalBufferID(1);
        rmdMessageDto2.setOperationName(new QName("http://wos2.com/test","testelement"));

        RMDMessageDto rmdMessageDto3 = new RMDMessageDto();
        rmdMessageDto3.setMessageNumber(34);
        rmdMessageDto3.setSoapEnvelope("org soap envelope");
        rmdMessageDto3.setSend(true);
        rmdMessageDto3.setInternalBufferID(1);
        rmdMessageDto3.setOperationName(new QName("http://wos2.com/test","testelement"));

        try {
            persistenceManager.save(rmdMessageDto1);
            persistenceManager.save(rmdMessageDto2);
            persistenceManager.save(rmdMessageDto3);
            testDisplayDataBase();
            List rmdMessages = persistenceManager.getRMDMessagesWithInvokerBufferID(1);
            displayRMDMessages(rmdMessages);
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testSequenceReceivedNumber(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        SequenceReceivedNumberDto sequenceReceivedNumber = new SequenceReceivedNumberDto();
        sequenceReceivedNumber.setNumber(34);
        sequenceReceivedNumber.setRmdSequenceID(34);

        try {
            persistenceManager.save(sequenceReceivedNumber);
            testDisplayDataBase();
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testBufferReceivedNumber() {
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        BufferReceivedNumberDto bufferReceivedNumber1 = new BufferReceivedNumberDto();
        bufferReceivedNumber1.setNumber(34);
        bufferReceivedNumber1.setInternalBufferID(1);

        BufferReceivedNumberDto bufferReceivedNumber2 = new BufferReceivedNumberDto();
        bufferReceivedNumber2.setNumber(34);
        bufferReceivedNumber2.setInternalBufferID(1);

        try {
            persistenceManager.save(bufferReceivedNumber1);
            persistenceManager.save(bufferReceivedNumber2);
            testDisplayDataBase();
            List bufferReceivedNumbers = persistenceManager.getBufferReceivedNumbersWithInvokerBufferID(1);
            displayBufferReceivedNumbers(bufferReceivedNumbers);
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testGetRMDSequeceWithSequenceID(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        RMDSequenceDto rmdSequenceDto1 = new RMDSequenceDto();
        rmdSequenceDto1.setSequenceID("testSequenceID1");
        rmdSequenceDto1.setState(2);
        rmdSequenceDto1.setAcksTo("testAcksTo");
        rmdSequenceDto1.setLastMessageNumber(3);

        RMDSequenceDto rmdSequenceDto2 = new RMDSequenceDto();
        rmdSequenceDto2.setSequenceID("testSequenceID2");
        rmdSequenceDto2.setState(2);
        rmdSequenceDto2.setAcksTo("testAcksTo");
        rmdSequenceDto2.setLastMessageNumber(3);

        try {
            persistenceManager.save(rmdSequenceDto1);
            persistenceManager.save(rmdSequenceDto2);
//            RMDSequenceDto result = persistenceManager.getRMDSequeceWithSequenceID("testSequenceID1");
//            System.out.println("RMD Sequence ID ==> " + result.getId());
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testGetSequenceReceivedNumbersWithRMDSequenceID(){

        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        SequenceReceivedNumberDto sequenceReceivedNumberDto1 = new SequenceReceivedNumberDto();
        sequenceReceivedNumberDto1.setNumber(5);
        sequenceReceivedNumberDto1.setRmdSequenceID(1);

        SequenceReceivedNumberDto sequenceReceivedNumberDto2 = new SequenceReceivedNumberDto();
        sequenceReceivedNumberDto2.setNumber(5);
        sequenceReceivedNumberDto2.setRmdSequenceID(1);

        SequenceReceivedNumberDto sequenceReceivedNumberDto3 = new SequenceReceivedNumberDto();
        sequenceReceivedNumberDto3.setNumber(5);
        sequenceReceivedNumberDto3.setRmdSequenceID(2);

        try {
            persistenceManager.save(sequenceReceivedNumberDto1);
            persistenceManager.save(sequenceReceivedNumberDto2);
            persistenceManager.save(sequenceReceivedNumberDto3);
            List sequenceNumbers = persistenceManager.getSequenceReceivedNumbersWithRMDSequenceID(1);
            displaySequenceReceivedNumbers(sequenceNumbers);
        } catch (PersistenceException e) {
            fail();
        }

    }

    public void testGetInvokerBufferWithRMDSequenceID(){
        PersistenceManager persistenceManager = new HibernatePersistenceManager(getConfiguration());

        InvokerBufferDto invokerBufferDto1 = new InvokerBufferDto();
        invokerBufferDto1.setState(2);
        invokerBufferDto1.setLastMessage(23);
        invokerBufferDto1.setLastMessageToApplication(2);
        invokerBufferDto1.setRmdSequenceID(2);

        InvokerBufferDto invokerBufferDto2 = new InvokerBufferDto();
        invokerBufferDto2.setState(2);
        invokerBufferDto2.setLastMessage(23);
        invokerBufferDto2.setLastMessageToApplication(2);
        invokerBufferDto2.setRmdSequenceID(2);

        RMDSequenceDto rmdSequenceDto1 = new RMDSequenceDto();
        rmdSequenceDto1.setSequenceID("testSequenceID1");
        rmdSequenceDto1.setState(2);
        rmdSequenceDto1.setAcksTo("testAcksTo");
        rmdSequenceDto1.setLastMessageNumber(3);

        Axis2InfoDto axis2InfoDto = new Axis2InfoDto();
        axis2InfoDto.setServerSide(true);
        axis2InfoDto.setServiceName("testservice");
        axis2InfoDto.setCurrentHanlderIndex(2);
        axis2InfoDto.setCurrentPhaseIndex(5);


        try {
            persistenceManager.save(rmdSequenceDto1);
            invokerBufferDto1.setRmdSequenceID(rmdSequenceDto1.getId());
            persistenceManager.save(invokerBufferDto1, rmdSequenceDto1,axis2InfoDto);
            persistenceManager.save(invokerBufferDto2, rmdSequenceDto1,axis2InfoDto);
            InvokerBufferDto invokerBuffer =
                    persistenceManager.getInvokerBufferWithRMDSequenceID(rmdSequenceDto1.getId());
            System.out.println("Invoker Buffer id ==> " + invokerBuffer.getId());
        } catch (PersistenceException e) {
            fail();
        }
    }

    public void testDisplayDataBase() {
        // dispay internal key table
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List internalKeys = session.createQuery("from InternalKeyDto").list();
        List rmsSequences = session.createQuery("from RMSSequenceDto").list();
        List rmsMessages = session.createQuery("from RMSMessageDto").list();
        List rmdSequences = session.createQuery("from RMDSequenceDto").list();
        List invokerBuffers = session.createQuery("from InvokerBufferDto").list();
        List rmdMessages = session.createQuery("from RMDMessageDto").list();
        List sequenceReceivedNumbers = session.createQuery("from SequenceReceivedNumberDto").list();
        List bufferReceivedNumbers = session.createQuery("from BufferReceivedNumberDto").list();
        List axis2Infos = session.createQuery("from Axis2InfoDto").list();
        List engagedModules = session.createQuery("from EngagedModuleDto").list();
        List properties = session.createQuery("from PropertyDto").list();

        displayInternalKeys(internalKeys);
        displayRMSSequences(rmsSequences);
        displayRMSMessages(rmsMessages);
        displayRMDSequences(rmdSequences);
        displayInvokerBuffers(invokerBuffers);
        displayRMDMessages(rmdMessages);
        displaySequenceReceivedNumbers(sequenceReceivedNumbers);
        displayBufferReceivedNumbers(bufferReceivedNumbers);
        displayAxisInfo(axis2Infos);
        displayEngagedModule(engagedModules);
        displayProperty(properties);
        session.getTransaction().commit();
    }

    public void displayInternalKeys(List list) {
        InternalKeyDto internalKeyDto = null;
        System.out.println("\nDisplay internal key details ...");
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            internalKeyDto = (InternalKeyDto) iter.next();
            System.out.println("ID - " + internalKeyDto.getId() +
                    ", key - " + internalKeyDto.getKey() +
                    ", toAddress - " + internalKeyDto.getToAddress());
        }
    }

    public void displayRMSSequences(List list) {
        System.out.println("\nDisplay RMSSequce details ...");
        RMSSequenceDto rmsSequenceDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            rmsSequenceDto = (RMSSequenceDto) iter.next();
            System.out.println("ID - " + rmsSequenceDto.getId() +
                    ", sequenceID - " + rmsSequenceDto.getSequenceID() +
                    ", state - " + rmsSequenceDto.getState() +
                    ", messageNumber - " + rmsSequenceDto.getMessageNumber() +
                    ", lastMessageNumber - " + rmsSequenceDto.getLastMessageNumber() +
                    ", endPointAddress - " + rmsSequenceDto.getEndPointAddress() +
                    ", ackToEpr - " + rmsSequenceDto.getAckToEpr() +
                    ", sequenceOffer - " + rmsSequenceDto.getSequenceOffer() +
                    ", internalKeyID - " + rmsSequenceDto.getInternalKeyID() +
                    ", startTime - " + rmsSequenceDto.getStartTime() +
                    ", axis2InfoID - " + rmsSequenceDto.getAxis2InfoID() +
                    ", mep - " + rmsSequenceDto.getMep() +
                    ", endTime - " + rmsSequenceDto.getEndTime());
        }
    }

    public void displayRMSMessages(List list) {
        System.out.println("\nDisplay RMSMessage details ...");
        RMSMessageDto rmsMessageDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            rmsMessageDto = (RMSMessageDto) iter.next();
            System.out.println("ID - " + rmsMessageDto.getId() +
                    ", messageNumber - " + rmsMessageDto.getMessageNumber() +
                    ", isLastMessage - " + rmsMessageDto.isLastMessage() +
                    ", soapEnvelope - " + rmsMessageDto.getSoapEnvelpe() +
                    ", rmssequenceID - " + rmsMessageDto.getRmsSequenceID() +
                    ", isSend - " + rmsMessageDto.isSend() +
                    ", axisMessageID - " + rmsMessageDto.getAxisMessageID() +
                    ", action - " + rmsMessageDto.getAction() +
                    ", operationName - " + rmsMessageDto.getOperationName() +
                    ", callBackClassName - " + rmsMessageDto.getCallBackClassName());
        }
    }

    public void displayRMDSequences(List list){
        System.out.println("\nDisplay RMDSequce details ...");
        RMDSequenceDto rmdSequenceDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            rmdSequenceDto = (RMDSequenceDto) iter.next();
            System.out.println("ID - " + rmdSequenceDto.getId() +
                    ", sequenceID - " + rmdSequenceDto.getSequenceID() +
                    ", state - " + rmdSequenceDto.getState() +
                    ", acksTo - " + rmdSequenceDto.getAcksTo() +
                    ", lastMessageNumber - " + rmdSequenceDto.getLastMessageNumber() +
                    ", startTime - " + rmdSequenceDto.getStartTime() +
                    ", axis2InfoID - " + rmdSequenceDto.getAxis2InfoID() +
                    ", endTime - " + rmdSequenceDto.getEndTime());
        }
    }

    public void displayInvokerBuffers(List list) {
        System.out.println("\nDisplay Invoker Buffer Details ...");
        InvokerBufferDto invokerBufferDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            invokerBufferDto = (InvokerBufferDto) iter.next();
            System.out.println("ID - " + invokerBufferDto.getId() +
                    ", state - " + invokerBufferDto.getState() +
                    ", lastMessage - " + invokerBufferDto.getLastMessage() +
                    ", lastMessageToApplication - " + invokerBufferDto.getLastMessageToApplication() +
                    ", rmdSequenceID - " + invokerBufferDto.getRmdSequenceID());
        }
    }

    public void displayRMDMessages(List list) {
        System.out.println("\nDisplay RMD Message Details ...");
        RMDMessageDto rmdMessageDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            rmdMessageDto = (RMDMessageDto) iter.next();
            System.out.println("ID - " + rmdMessageDto.getId() +
                    ", messageNumber - " + rmdMessageDto.getMessageNumber() +
                    ", soapEnvelope - " + rmdMessageDto.getSoapEnvelope() +
                    ", send - " + rmdMessageDto.isSend() +
                    ", operationName - " + rmdMessageDto.getOperationName() +
                    ", messageID - " + rmdMessageDto.getMessageID() +
                    ", to - " + rmdMessageDto.getTo() +
                    ", replyto - " + rmdMessageDto.getReplyTo() +
                    ", internalBufferID - " + rmdMessageDto.getInternalBufferID());
        }
    }

    public void displaySequenceReceivedNumbers(List list) {
        System.out.println("\nDisplay sequence received numbers Details ...");
        SequenceReceivedNumberDto sequenceReceivedNumberDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            sequenceReceivedNumberDto = (SequenceReceivedNumberDto) iter.next();
            System.out.println("ID - " + sequenceReceivedNumberDto.getId() +
                    ", number - " + sequenceReceivedNumberDto.getNumber() +
                    ", rmdSequenceID - " + sequenceReceivedNumberDto.getRmdSequenceID() +
                    ", relatesToMessageID - " + sequenceReceivedNumberDto.getRelatesToMessageID());
        }
    }

    public void displayBufferReceivedNumbers(List list) {
        System.out.println("\nDisplay buffer received numbers Details ...");
        BufferReceivedNumberDto bufferReceivedNumberDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            bufferReceivedNumberDto = (BufferReceivedNumberDto) iter.next();
            System.out.println("ID - " + bufferReceivedNumberDto.getId() +
                    ", number - " + bufferReceivedNumberDto.getNumber() +
                    ", invokerBufferID - " + bufferReceivedNumberDto.getInternalBufferID());
        }
    }

     public void displayAxisInfo(List list) {
        System.out.println("\nDisplay Axis Info Details ...");
        Axis2InfoDto axis2InfoDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            axis2InfoDto = (Axis2InfoDto) iter.next();
            System.out.println("ID - " + axis2InfoDto.getId() +
                    ", serviceName - " + axis2InfoDto.getServiceName() +
                    ", serverSide - " + axis2InfoDto.isServerSide() +
                    ", currentPhaseIndex - " + axis2InfoDto.getCurrentPhaseIndex() +
                    ", currentHanlderIndex - " + axis2InfoDto.getCurrentHanlderIndex());
        }
    }

    public void displayEngagedModule(List list) {
        System.out.println("\nDisplay Engaged Module Details ...");
        EngagedModuleDto engagedModuleDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            engagedModuleDto = (EngagedModuleDto) iter.next();
            System.out.println("ID - " + engagedModuleDto.getId() +
                    ", moduleName - " + engagedModuleDto.getModuleName() +
                    ", axisInfoID - " + engagedModuleDto.getAxis2InfoID());
        }
    }

    public void displayProperty(List list) {
        System.out.println("\nDisplay Property Details ...");
        PropertyDto propertyDto = null;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            propertyDto = (PropertyDto) iter.next();
            System.out.println("ID - " + propertyDto.getId() +
                    ", name - " + propertyDto.getName() +
                    ", value - " + propertyDto.getValue() +
                    ", axisInfoID - " + propertyDto.getAxis2InfoID());
        }
    }
}
