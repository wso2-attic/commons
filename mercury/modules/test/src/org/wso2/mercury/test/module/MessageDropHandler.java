/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
package org.wso2.mercury.test.module;

import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;


public class MessageDropHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext msgContext)
            throws AxisFault {

        Object countinue =
                msgContext.getConfigurationContext().getProperty(MessageDropModule.COUNTINUE_CLIENT_IN_MESSAGES);
        if ((countinue != null) && countinue.equals("true")){
            if (!msgContext.isServerSide() && (msgContext.getFLOW() == MessageContext.IN_FLOW)){
                return InvocationResponse.CONTINUE;
            }
        }
        Double reliablity =
                (Double) msgContext.getConfigurationContext().getProperty(MessageDropModule.RELIABILITY);
        double number = Math.random();
        if (number < reliablity.doubleValue()) {
            return InvocationResponse.CONTINUE;
        } else {
            System.out.println("Dropping the message");
            return InvocationResponse.ABORT;
        }
    }

}
