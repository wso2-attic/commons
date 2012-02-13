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
package org.wso2.mercury.receivers;

import org.apache.axis2.receivers.AbstractMessageReceiver;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;

/**
 * this is a dummy message receiver used in RM controll operations
 *
 */

public class RMDummyMessageReceiver extends AbstractMessageReceiver {
    protected void invokeBusinessLogic(MessageContext messageCtx) throws AxisFault {

    }
}
