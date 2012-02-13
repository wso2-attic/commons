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

import org.wso2.mercury.state.RMDSequence;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * this is the worker for the RMDSequence. this worker polles the
 * RMDSequence and do ncessary actions according to the state.
 */
public class RMDSequenceWorker implements Runnable {

    private static Log log = LogFactory.getLog(RMDSequenceWorker.class);

    private long sequenceWorkerSleepTime;

    private RMDSequence rmdSequence;

    public RMDSequenceWorker(RMDSequence rmdSequence) {
        this.rmdSequence = rmdSequence;
    }

    public void run() {
        // run the loop until sequence finish
        // this loop will terminate only if RMDSequence comes to terminated sate or
        // it is inactive for a given time
        while ((rmdSequence.getState() != RMDSequence.STATE_TERMINATED) &&
                ((System.currentTimeMillis() - rmdSequence.getLastAccesedTime()) < rmdSequence.getTimeoutTime())) {
            try {
                rmdSequence.doActions();
            } catch (AxisFault e) {
                log.error("Fault occured when doing actions for the RMD Sequence "
                        + this.rmdSequence.getSequenceID(), e);
            } catch (RMMessageBuildingException e) {
                log.error("Could not build the message ", e);
            }

            try {
                Thread.sleep(sequenceWorkerSleepTime);
            } catch (InterruptedException e) {
            }
        }
        log.info("Stopping the RMD Sequence for " + this.rmdSequence.getSequenceID());

    }

    public long getSequenceWorkerSleepTime() {
        return sequenceWorkerSleepTime;
    }

    public void setSequenceWorkerSleepTime(long sequenceWorkerSleepTime) {
        this.sequenceWorkerSleepTime = sequenceWorkerSleepTime;
    }
}
