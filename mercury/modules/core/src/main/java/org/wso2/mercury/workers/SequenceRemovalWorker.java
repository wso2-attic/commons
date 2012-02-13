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
package org.wso2.mercury.workers;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.state.RMDContext;
import org.wso2.mercury.state.RMSContext;
import org.wso2.mercury.util.MercuryConstants;


public class SequenceRemovalWorker implements Runnable {

    private static Log log = LogFactory.getLog(SequenceRemovalWorker.class);

    private ConfigurationContext configContext;
    private boolean isShutDown = false;

    private long sequenceRemovalWorkerSleepTime;

    public SequenceRemovalWorker(ConfigurationContext configContext) {
        this.configContext = configContext;
    }


    public void run() {
        removeSequences();
    }

    private synchronized void removeSequences() {
        while (!isShutDown){

            RMSContext rmsContext = (RMSContext) this.configContext.getProperty(MercuryConstants.RMS_CONTEXT);
            RMDContext rmdContext = (RMDContext) this.configContext.getProperty(MercuryConstants.RMD_CONTEXT);
            rmsContext.removeExpiredSequences();
            rmdContext.removeExpiredSequences();
            try {
                // wait until sleep time expires
                // or gets a shut down notification.
                this.wait(sequenceRemovalWorkerSleepTime);
            } catch (InterruptedException e) {}
        }
        log.info("Shutting down the Sequence removal worker");

    }


    public synchronized void shutDownWorker(){
        this.isShutDown = true;
        this.notify();
    }

    public boolean isShutDown() {
        return isShutDown;
    }

    public void setShutDown(boolean shutDown) {
        isShutDown = shutDown;
    }

    public long getSequenceRemovalWorkerSleepTime() {
        return sequenceRemovalWorkerSleepTime;
    }

    public void setSequenceRemovalWorkerSleepTime(long sequenceRemovalWorkerSleepTime) {
        this.sequenceRemovalWorkerSleepTime = sequenceRemovalWorkerSleepTime;
    }
}
