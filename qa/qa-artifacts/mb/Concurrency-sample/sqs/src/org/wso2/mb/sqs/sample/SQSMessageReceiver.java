package org.wso2.mb.sqs.sample;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class SQSMessageReceiver implements Runnable {
    protected String epr;
    protected SQSClient sqsClient;
    protected boolean allMessagesProcessed = false;

    public SQSMessageReceiver(String epr) {
        this.epr = epr;
        sqsClient = new SQSClient();
    }

    public synchronized void run() {
        while (!allMessagesProcessed) {
            List<MessageQueueStub.Message_type0> messages = sqsClient.receiveMessages2(epr, 1, 1500);
            processMessage(messages);
            /*  try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }*/
        }
    }

    public abstract void processMessage(List<MessageQueueStub.Message_type0> messages);
}

class StockUpdateMessageReceiver extends SQSMessageReceiver {

    public StockUpdateMessageReceiver(String epr) {
        super(epr);
    }

    @Override
    public void processMessage(List<MessageQueueStub.Message_type0> messages) {
        if (messages != null && messages.size() > 0) {
            for (MessageQueueStub.Message_type0 message : messages) {
                String messageBody = message.getBody();
                if (messageBody != null) {
                    StAXOMBuilder stAXOMBuilder = null;
                    try {
                        System.out.println("messageBody = " + messageBody);
                        stAXOMBuilder = new StAXOMBuilder(new ByteArrayInputStream(messageBody.trim().getBytes()));
                        OMElement omElement = stAXOMBuilder.getDocumentElement();
                        if (omElement.getLocalName().equals("UpdatePrice")) {
                            return;
                        } else if (omElement.getLocalName().equals("StockUpdate")) {
                            String[] receiptHandlers = {message.getReceiptHandle()};
                            sqsClient.changeVisibility(epr, receiptHandlers, 100000);
                            /*  try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }*/
//                            System.out.println(omElement);
                            sqsClient.deleteMessages(epr, receiptHandlers);
                        }
                    } catch (XMLStreamException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        } else {
            allMessagesProcessed = true;
            notify();
        }
    }
}

class UpdatePriceMessageReceiver extends SQSMessageReceiver {

    public UpdatePriceMessageReceiver(String epr) {
        super(epr);
    }

    @Override
    public void processMessage(List<MessageQueueStub.Message_type0> messages) {
        if (messages != null && messages.size() > 0) {
            for (MessageQueueStub.Message_type0 message : messages) {
                String messageBody = message.getBody();
                if (messageBody != null) {
                    StAXOMBuilder stAXOMBuilder = null;
                    try {
                        stAXOMBuilder = new StAXOMBuilder(new ByteArrayInputStream(messageBody.getBytes()));
                        OMElement omElement = stAXOMBuilder.getDocumentElement();
                        if (omElement.getLocalName().equals("UpdatePrice")) {
                            String[] receiptHandlers = {message.getReceiptHandle()};
                            sqsClient.changeVisibility(epr, receiptHandlers, 100000);
//                            System.out.println(omElement);
                            /* try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }*/
                            sqsClient.deleteMessages(epr, receiptHandlers);
                        } else if (omElement.getLocalName().equals("StockUpdate")) {
                            return;
                        }
                    } catch (XMLStreamException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        } else {
            allMessagesProcessed = true;
            notify();
        }
    }

}
