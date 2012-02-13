package org.wso2.mb.sqs.sample;

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
public abstract class MessageSender implements Runnable {
    private String epr;

    public MessageSender(String epr) {
        this.epr = epr;
    }

    public void run() {
        SQSClient sqsClient = new SQSClient();
        sqsClient.sendMessage(epr, getMessage(), 10);
    }

    public abstract String getMessage();
}

class PriceUpdateMessageSender extends MessageSender {

    public PriceUpdateMessageSender(String epr) {
        super(epr);
    }

    @Override
    public String getMessage() {
        String message = "<UpdatePrice>\n" +
                         "    <items>\n" +
                         "        <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <oldPrice>10</oldPrice>\n" +
                         "            <newPrice>15</newPrice>\n" +
                         "        </item>\n" +
                         "        <item>\n" +
                         "            <name>item2</name>\n" +
                         "            <oldPrice>50</oldPrice>\n" +
                         "            <newPrice>45</newPrice>\n" +
                         "        </item>\n" +
                         "    </items>\n" +
                         "</UpdatePrice>";
        return message;
    }

}

class StockUpdateMessageSender extends MessageSender {

    public StockUpdateMessageSender(String epr) {
        super(epr);
    }

    @Override
    public String getMessage() {
        String message = "<StockUpdate>\n" +
                         "     <items>\n" +
                         "        <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "         <item>\n" +
                         "            <name>ABC</name>\n" +
                         "            <brand>xyz</brand>\n" +
                         "            <quantity>10</quantity>\n" +
                         "        </item>\n" +
                         "    </items>\n" +
                         "</StockUpdate>";
        return message;
    }
}
