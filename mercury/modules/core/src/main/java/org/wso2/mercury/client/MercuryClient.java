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
package org.wso2.mercury.client;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.Constants;
import org.apache.axis2.AxisFault;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.util.MercuryParameterHandler;
import org.wso2.mercury.callback.MercuryTerminateCallback;
import org.wso2.mercury.callback.InternalTerminateCallback;

/**
 * provides some mercury specific client functionalities
 */
public class MercuryClient {

    private ServiceClient serviceClient;
    private MercuryParameterHandler mercuryParameterHandler;

    public MercuryClient(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
        this.mercuryParameterHandler = new MercuryParameterHandler(serviceClient.getAxisService());
    }

    public void terminateSequence() throws AxisFault {
        terminateSequence(MercuryConstants.DEFAULT_INTERNAL_KEY);
    }

    public void terminateSequence(boolean isBlocking) throws AxisFault {
        terminateSequence(MercuryConstants.DEFAULT_INTERNAL_KEY, isBlocking);
    }

    public void terminateSequence(String key) throws AxisFault {
        serviceClient.getOptions().setProperty(MercuryClientConstants.TERMINATE_MESSAGE, Constants.VALUE_TRUE);
        serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, key);
        serviceClient.fireAndForget(null);
    }

    public void terminateSequence(String key, boolean isBlocking) throws AxisFault {
        MercuryTerminateCallback terminateCallback = new InternalTerminateCallback();
        serviceClient.getOptions().setProperty(MercuryClientConstants.TERMINATE_CALLBACK, terminateCallback);
        terminateSequence(key);
        if (isBlocking) {
            synchronized (terminateCallback) {
                try {
                    terminateCallback.wait(serviceClient.getOptions().getTimeOutInMilliSeconds());
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void setRMSSequenceTimeout(long timeout) throws AxisFault {
        this.mercuryParameterHandler.setRMSSequenceTimeout(timeout);
    }

    public void setRMSSequenceRetransmitTime(long retransmitTime) throws AxisFault {
        this.mercuryParameterHandler.setRMSSequenceRetransmitTime(retransmitTime);
    }

    public void setRMSSequenceWorkerSleepTime(long sleepTime) throws AxisFault {
        this.mercuryParameterHandler.setRMSSequenceWorkerSleepTime(sleepTime);
    }

    public void setRMSMaximumRetransmitCount(long count) throws AxisFault {
        this.mercuryParameterHandler.setRMSMaximumRetransmitCount(count);
    }

    public void setRMSExponentialBackoff(boolean value) throws AxisFault {
        this.mercuryParameterHandler.setRMSExponentialBackoff(value);
    }

    public void setRMDSequenceTimeout(long timeout) throws AxisFault {
        this.mercuryParameterHandler.setRMDSequenceTimeout(timeout);
    }

    public void setRMDSequenceRetransmitTime(long retransmitTime) throws AxisFault {
        this.mercuryParameterHandler.setRMDSequenceRetransmitTime(retransmitTime);
    }

    public void setRMDSequenceWorkerSleepTime(long sleepTime) throws AxisFault {
        this.mercuryParameterHandler.setRMDSequenceWorkerSleepTime(sleepTime);
    }

    public void setInvokerTimeout(long timeout) throws AxisFault {
        this.mercuryParameterHandler.setInvokerTimeout(timeout);
    }

    public void setInvokerSleepTime(long sleepTime) throws AxisFault {
        this.mercuryParameterHandler.setInvokerSleepTime(sleepTime);
    }

    public void setEnforceRM(boolean enforceRM) throws AxisFault {
        this.mercuryParameterHandler.setEnforceRM(enforceRM);
    }
}
