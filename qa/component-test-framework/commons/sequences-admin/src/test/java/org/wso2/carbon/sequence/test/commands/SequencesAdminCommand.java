package org.wso2.carbon.sequence.test.commands;

/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

import junit.framework.TestCase;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.sequences.ui.types.SequenceAdminServiceStub;
import org.wso2.carbon.sequences.ui.types.SequenceEditorException;
import org.wso2.carbon.sequences.ui.types.common.to.SequenceInfo;

import java.rmi.RemoteException;


public class SequencesAdminCommand extends TestCase {
    SequenceAdminServiceStub sequenceAdminServiceStub;
    private static final Log log = LogFactory.getLog(SequencesAdminCommand.class);

    public SequencesAdminCommand(SequenceAdminServiceStub sequenceAdminServiceStub) {
        this.sequenceAdminServiceStub = sequenceAdminServiceStub;
        log.debug("sequenceAdminStub added");
    }

/*
*        Test for the success and failure cases  for  AddSequences
*/

    public void addSequenceExecuteSuccessCase(OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("AddSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.addSequence(omElement);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Sequence added");
    }

    public void addSequenceExecuteFailureCase(OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("AddSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.addSequence(omElement);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("AddSequenceCommand FailureCase passed");
    }


    /*Test for the success and failure cases  for  DeleteSequences*/


    public void deleteSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DeleteSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.deleteSequence(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DeleteSequenceCommand SuccessCase passed");
    }


    public void deleteSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DeleteSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.deleteSequence(sequenceName);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DeleteSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  AddDynamicSequences
*/

    public void addDynamicSequenceExecuteSuccessCase(String key, OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("AddDynamicSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.addDynamicSequence(key, omElement);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("AddDynamicSequenceCommand SuccessCase passed");
    }


    public void addDynamicSequenceExecuteFailureCase(String key, OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("AddDynamicSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.addDynamicSequence(key, omElement);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("AddDynamicSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  DeleteDynamicSequences
*/

    public void deleteDynamicSequenceExecuteSuccessCase(String key) throws RemoteException, SequenceEditorException {
        log.debug("DeleteDynamicSequenceExecuteCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.deleteDynamicSequence(key);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DeleteDynamicSequenceCommand SuccessCase passed");
    }

    public void deleteDynamicSequenceExecuteFailureCase(String key) throws RemoteException, SequenceEditorException {
        log.debug("DeleteDynamicSequenceExecuteCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.deleteDynamicSequence(key);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DeleteDynamicSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  EnableStatisticsSequences
*/

    public String enableStatisticsSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("EnableStatisticsSequenceCommand executeSuccessCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.enableStatistics(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Enable Statistics of " + sequenceName + " sequence");
        return status;
    }

    public String enableStatisticsSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("EnableStatisticsSequenceCommand executeFailureCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.enableStatistics(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("EnableStatisticsSequenceCommand FailureCase passed");
        return status;
    }

/*
*        Test for the success and failure cases  for  DisableStatisticsSequences
*/

    public String disableStatisticsSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DisableStatisticsSequenceCommand executeSuccessCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.disableStatistics(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DisableStatisticsSequenceCommand SuccessCase passed");
        return status;
    }

    public String disableStatisticsSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DisableStatisticsSequenceCommand executeFailureCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.enableStatistics(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DisableStatisticsSequenceCommand FailureCase passed");
        return status;
    }

/*
*        Test for the success and failure cases  for  EnableTracingSequence
*/

    public String enableTracingSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("EnableTracingSequenceCommand executeSuccessCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.enableTracing(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Enable Tracing of " + sequenceName + " sequence");
        return status;
    }

    public String enableTracingSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("EnableTracingSequenceCommand executeFailureCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.enableTracing(sequenceName);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("EnableTracingSequenceCommand FailureCase passed");
        return status;
    }

/*
*        Test for the success and failure cases  for  DisableTracingSequence
*/

    public String disableTracingSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DisableTracingSequenceCommand executeSuccessCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.disableTracing(sequenceName);
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DisableTracingSequenceCommand SuccessCase passed");
        return status;
    }

    public String disableTracingSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("DisableTracingSequenceCommand executeFailureCase");
        String status = null;
        try {
            status = sequenceAdminServiceStub.disableTracing(sequenceName);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("DisableTracingSequenceCommand FailureCase passed");
        return status;
    }


/*
*        Test for the success and failure cases  for  saveSequence
*/

    public void saveSequenceExecuteSuccessCase(OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("SaveSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.saveSequence(omElement);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("SaveSequenceCommand SuccessCase passed");
    }

    public void saveSequenceExecuteFailureCase(OMElement omElement) throws RemoteException, SequenceEditorException {
        log.debug("SaveSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.saveSequence(omElement);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("SaveSequenceCommand FailureCase passed");
    }


    /* Test for the success and failure cases  for  getDynamicSequenceCount*/


    public int getDynamicSequenceCountExecuteSuccessCase() throws RemoteException, SequenceEditorException {
        log.debug("DisableStatisticsCommand executeSuccessCase");
        int dynamicSeqCount = 0;
        try {
            dynamicSeqCount = sequenceAdminServiceStub.getDynamicSequenceCount();

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("GetDynamicSequenceCountCommand SuccessCase passed");
        return dynamicSeqCount;
    }

    public int getDynamicSequenceCountExecuteFailureCase() throws RemoteException, SequenceEditorException {
        log.debug("DisableStatisticsCommand executeFailureCase");
        int dynamicSeqCount = 0;
        try {
            dynamicSeqCount = sequenceAdminServiceStub.getDynamicSequenceCount();
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("GetDynamicSequenceCountCommand FailureCase passed");
        return dynamicSeqCount;
    }

    /* Test for the success and failure cases  for  getSequence*/


    public OMElement getSequenceExecuteSuccessCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("getSequenceCommand executeSuccessCase");
        OMElement oMElement = null;
        try {
            oMElement = sequenceAdminServiceStub.getSequence(sequenceName);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Getting " + sequenceName + " sequence succeeded  ");
        return oMElement;
    }

    public OMElement getSequenceExecuteFailureCase(String sequenceName) throws RemoteException, SequenceEditorException {
        log.debug("getSequenceCommand executeFailureCase");
        OMElement oMElement = null;
        try {
            oMElement = sequenceAdminServiceStub.getSequence(sequenceName);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("getSequenceCommand FailureCase passed");
        return oMElement;
    }


    public int getSequencesCountSuccessCase() throws RemoteException, SequenceEditorException {
        log.debug("getSequenceCommand executeSuccessCase");
        int seqCount = 0;
        try {
            seqCount = sequenceAdminServiceStub.getSequencesCount();

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Getting " + seqCount + " number of sequences.");
        return seqCount;
    }

    public int getSequencesCountFailureCase() throws RemoteException, SequenceEditorException {
        log.debug("getSequenceCommand executeSuccessCase");
        int seqCount = 0;
        try {
            seqCount = sequenceAdminServiceStub.getSequencesCount();

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        log.info("Getting " + seqCount + " number of sequences.");
        return seqCount;
    }

    public SequenceInfo[] getDynamicSequencesSuccessCase(int pageNo, int seqPerPage) throws RemoteException, SequenceEditorException {
        SequenceInfo[] count = null;
        try {
            count = sequenceAdminServiceStub.getDynamicSequences(pageNo, seqPerPage);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        return count;
    }

    public SequenceInfo[] getDynamicSequencesFailureCase(int pageNo, int seqPerPage) throws RemoteException, SequenceEditorException {
        SequenceInfo[] count = null;
        try {
            count = sequenceAdminServiceStub.getDynamicSequences(pageNo, seqPerPage);

        } catch (RemoteException e) {
            fail("RemoteException thrown " + e);
        } catch (SequenceEditorException e) {
            fail("SequenceEditorException thrown " + e);
        } catch (Exception e) {
            fail("Unexpected Exception thrown " + e);
        }
        return count;
    }

}