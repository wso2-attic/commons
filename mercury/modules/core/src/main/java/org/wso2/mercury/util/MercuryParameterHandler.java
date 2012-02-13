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
package org.wso2.mercury.util;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisDescription;

/**
 * this class is used to set and get parameters to AxisService
 */

public class MercuryParameterHandler {

    private AxisDescription axisDescription;

    public MercuryParameterHandler(AxisDescription axisDescription) {
        this.axisDescription = axisDescription;
    }

    public long getRMSSequenceTimeout() {
        long rmsSequeenceTimeout = 60000;
        if (axisDescription.getParameter(MercuryConstants.RMS_SEQUENCE_TIMEOUT) != null) {
            rmsSequeenceTimeout =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMS_SEQUENCE_TIMEOUT).getValue());
        }
        return rmsSequeenceTimeout;
    }

    public void setRMSSequenceTimeout(long timeout) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMS_SEQUENCE_TIMEOUT, String.valueOf(timeout));
    }

    public long getRMSSequenceRetransmitTime() {
        long rmsSequeenceRetransmitTime = 15000;
        if (axisDescription.getParameter(MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME) != null) {
            rmsSequeenceRetransmitTime =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME).getValue());
        }
        return rmsSequeenceRetransmitTime;
    }

    public void setRMSSequenceRetransmitTime(long retransmitTime) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME, String.valueOf(retransmitTime));
    }

    public long getRMSSequenceWorkerSleepTime() {
        long rmsSequeenceWorkerSleepTime = 100;
        if (axisDescription.getParameter(MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME) != null) {
            rmsSequeenceWorkerSleepTime =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME).getValue());
        }
        return rmsSequeenceWorkerSleepTime;
    }

    public void setRMSSequenceWorkerSleepTime(long sleepTime) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME, String.valueOf(sleepTime));
    }

    public long getRMSMaximumRetransmitCount() {
        long rmsMaximumRetransmitCount = 100;
        if (axisDescription.getParameter(MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT) != null) {
            rmsMaximumRetransmitCount =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT).getValue());
        }
        return rmsMaximumRetransmitCount;
    }

    public void setRMSMaximumRetransmitCount(long count) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT, String.valueOf(count));
    }

    public boolean getRMSExponentialBackoff() {
        boolean rmsExponentialBackoff = false;
        if (axisDescription.getParameter(MercuryConstants.RMS_EXPONENTIAL_BACKOFF) != null) {
            rmsExponentialBackoff = ((Boolean)axisDescription.getParameter(
                            MercuryConstants.RMS_EXPONENTIAL_BACKOFF).getValue()).booleanValue();
        }
        return rmsExponentialBackoff;
    }
    public void setRMSExponentialBackoff(boolean value) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMS_EXPONENTIAL_BACKOFF, new Boolean(value));
    }

    
    public long getRMDSequenceTimeout() {
        long rmdSequeenceTimeout = 60000;
        if (axisDescription.getParameter(MercuryConstants.RMD_SEQUENCE_TIMEOUT) != null) {
            rmdSequeenceTimeout =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMD_SEQUENCE_TIMEOUT).getValue());
        }
        return rmdSequeenceTimeout;
    }

    public void setRMDSequenceTimeout(long timeout) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMD_SEQUENCE_TIMEOUT, String.valueOf(timeout));
    }

    public long getRMDSequenceRetransmitTime() {
        long rmsSequeenceRetransmitTime = 15000;
        if (axisDescription.getParameter(MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME) != null) {
            rmsSequeenceRetransmitTime =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME).getValue());
        }
        return rmsSequeenceRetransmitTime;
    }

    public void setRMDSequenceRetransmitTime(long retransmitTime) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME, String.valueOf(retransmitTime));
    }

    public long getRMDSequenceWorkerSleepTime() {
        long rmdSequeenceWorkerSleepTime = 100;
        if (axisDescription.getParameter(MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME) != null) {
            rmdSequeenceWorkerSleepTime =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME).getValue());
        }
        return rmdSequeenceWorkerSleepTime;
    }

    public void setRMDSequenceWorkerSleepTime(long sleepTime) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME, String.valueOf(sleepTime));
    }

    public long getInvokerTimeout() {
        long invokerTimeout = 60000;
        if (axisDescription.getParameter(MercuryConstants.INVOKER_TIMEOUT) != null) {
            invokerTimeout =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.INVOKER_TIMEOUT).getValue());
        }
        return invokerTimeout;
    }

    public void setInvokerTimeout(long timeout) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.INVOKER_TIMEOUT, String.valueOf(timeout));
    }

    public long getInvokerSleepTime() {
        long invokerSleepTime = 100;
        if (axisDescription.getParameter(MercuryConstants.INVOKER_WORKER_SLEEP_TIME) != null) {
            invokerSleepTime =
                    Long.parseLong((String) axisDescription.getParameter(
                            MercuryConstants.INVOKER_WORKER_SLEEP_TIME).getValue());
        }
        return invokerSleepTime;
    }

    public void setInvokerSleepTime(long sleepTime) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.INVOKER_WORKER_SLEEP_TIME, String.valueOf(sleepTime));
    }

    public boolean getEnforceRM() {
        boolean enforceRM = false;
        if (axisDescription.getParameter(MercuryConstants.ENFORCE_RM) != null) {
            enforceRM = Boolean.parseBoolean((String) axisDescription.getParameter(
                    MercuryConstants.ENFORCE_RM).getValue());
        }
        return enforceRM;
    }

    public void setEnforceRM(boolean enforceRM) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.ENFORCE_RM, String.valueOf(enforceRM));
    }

    public boolean getNotifyThreads() {
        boolean notifyThreads = true;
        if (axisDescription.getParameter(MercuryConstants.NOTIFY_THREADS) != null) {
            notifyThreads = Boolean.parseBoolean((String) axisDescription.getParameter(
                    MercuryConstants.NOTIFY_THREADS).getValue());
        }
        return notifyThreads;
    }

    public void setNotifyThreads(boolean notifyThreads) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.NOTIFY_THREADS, String.valueOf(notifyThreads));
    }

    public boolean getBuildMessageWithoutWaiting() {
        boolean buildMessageWithoutWaiting = false;
        if (axisDescription.getParameter(MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING) != null) {
            buildMessageWithoutWaiting = Boolean.parseBoolean((String) axisDescription.getParameter(
                    MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING).getValue());
        }
        return buildMessageWithoutWaiting;
    }

    public void setBuildMessageWithoutWaiting(boolean buildMessageWithoutWaiting) throws AxisFault {
        this.axisDescription.addParameter(MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING,
                String.valueOf(buildMessageWithoutWaiting));
    }
}
