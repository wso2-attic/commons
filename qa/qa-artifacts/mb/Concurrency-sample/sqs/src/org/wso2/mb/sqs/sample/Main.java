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
public class Main {
    public static void main(String[] args) {
        SQSClient sqsClient = new SQSClient();
        String epr = sqsClient.createQueue("TestQueueB");
        sqsClient.sendMessage(epr, new PriceUpdateMessageSender(epr).getMessage(), 1);
        for (int i = 0; i < 100; i++) {
            if (i % 5 != 0) {
                Thread priceUpdater = new Thread(new PriceUpdateMessageSender(epr));
                priceUpdater.start();
            } else {
                Thread stockUpdater = new Thread(new StockUpdateMessageSender(epr));
                stockUpdater.start();
            }

            if (i < 5) {
                Thread stockUpdateMessageReceiver = new Thread(new StockUpdateMessageReceiver(epr));
                stockUpdateMessageReceiver.start();

                Thread updatePriceMessageReceiver = new Thread(new UpdatePriceMessageReceiver(epr));
                updatePriceMessageReceiver.start();
            }
        }
    }
}
