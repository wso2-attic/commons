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

    public void addSequenceExecuteSuccessCase(OMElement omElement) {
        log.debug("AddSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.addSequence(omElement);
        } catch (RemoteException e) {
            //  e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("AddSequenceCommand SuccessCase passed");
    }

    public void addSequenceExecuteFailureCase(OMElement omElement) {
        log.debug("AddSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.addSequence(omElement);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("AddSequenceCommand FailureCase passed");
    }


    /*Test for the success and failure cases  for  DeleteSequences*/


    public void deleteSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("DeleteSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.deleteSequence(sequenceName);
        } catch (RemoteException e) {
            //  e.printStackTrace();
        } catch (SequenceEditorException e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }
        log.info("DeleteSequenceCommand SuccessCase passed");
    }


    public void deleteSequenceExecuteFailureCase(String sequenceName) {
        log.debug("DeleteSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.deleteSequence(sequenceName);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            //  e.printStackTrace();
        } catch (SequenceEditorException e) {
            fail("Unexpected exception thrown");
            e.printStackTrace();
        }

        log.info("DeleteSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  AddDynamicSequences
*/

    public void addDynamicSequenceExecuteSuccessCase(String sequenceName, OMElement omElement) {
        log.debug("AddDynamicSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.addDynamicSequence(sequenceName, omElement);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("AddDynamicSequenceCommand SuccessCase passed");
    }


    public void addDynamicSequenceExecuteFailureCase(String sequenceName, OMElement omElement) {
        log.debug("AddDynamicSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.addDynamicSequence(sequenceName, omElement);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("AddDynamicSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  DeleteDynamicSequences
*/

    public void deleteDynamicSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("DeleteDynamicSequenceExecuteCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.deleteDynamicSequence(sequenceName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DeleteDynamicSequenceCommand SuccessCase passed");
    }

    public void deleteDynamicSequenceExecuteFailureCase(String sequenceName) {
        log.debug("DeleteDynamicSequenceExecuteCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.deleteDynamicSequence(sequenceName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DeleteDynamicSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  EnableStatisticsSequences
*/

    public void enableStatisticsSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("EnableStatisticsSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.enableStatistics(sequenceName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("EnableStatisticsSequenceCommand SuccessCase passed");
    }

    public void enableStatisticsSequenceExecuteFailureCase(String sequenceName) {
        log.debug("EnableStatisticsSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.enableStatistics(sequenceName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("EnableStatisticsSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  DisableStatisticsSequences
*/

    public void disableStatisticsSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("DisableStatisticsSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.disableStatistics(sequenceName);


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DisableStatisticsSequenceCommand SuccessCase passed");
    }

    public void disableStatisticsSequenceExecuteFailureCase(String sequenceName) {
        log.debug("DisableStatisticsSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.enableStatistics(sequenceName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DisableStatisticsSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  EnableTracingSequence
*/

    public void enableTracingSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("EnableTracingSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.enableTracing(sequenceName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("EnableTracingSequenceCommand SuccessCase passed");
    }

    public void enableTracingSequenceExecuteFailureCase(String sequenceName) {
        log.debug("EnableTracingSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.enableTracing(sequenceName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("EnableTracingSequenceCommand FailureCase passed");
    }

/*
*        Test for the success and failure cases  for  DisableTracingSequence
*/

    public void disableTracingSequenceExecuteSuccessCase(String sequenceName) {
        log.debug("DisableTracingSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.disableTracing(sequenceName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DisableTracingSequenceCommand SuccessCase passed");
    }

    public void disableTracingSequenceExecuteFailureCase(String sequenceName) {
        log.debug("DisableTracingSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.disableTracing(sequenceName);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("DisableTracingSequenceCommand FailureCase passed");
    }


/*
*        Test for the success and failure cases  for  saveSequence
*/

    public void saveSequenceExecuteSuccessCase(OMElement omElement) {
        log.debug("SaveSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.saveSequence(omElement);

        } catch (RemoteException e) {
            //  e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("SaveSequenceCommand SuccessCase passed");
    }

    public void saveSequenceExecuteFailureCase(OMElement omElement) {
        log.debug("SaveSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.saveSequence(omElement);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("SaveSequenceCommand FailureCase passed");
    }


    /* Test for the success and failure cases  for  getDynamicSequenceCount*/


    public void getDynamicSequenceCountExecuteSuccessCase(String sequenceName) {
        log.debug("DisableStatisticsCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.getDynamicSequenceCount();

        } catch (RemoteException e) {
            //  e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("GetDynamicSequenceCountCommand SuccessCase passed");
    }

    public void getDynamicSequenceCountExecuteFailureCase(String sequenceName) {
        log.debug("DisableStatisticsCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.getDynamicSequenceCount();
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("GetDynamicSequenceCountCommand FailureCase passed");
    }

    /* Test for the success and failure cases  for  getSequence*/


    public void getSequenceExecuteSuccessCase(String name) {
        log.debug("getSequenceCommand executeSuccessCase");
        try {
            sequenceAdminServiceStub.getSequence(name);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("getSequenceCommand SuccessCase passed");
    }

    public void getSequenceExecuteFailureCase(String name) {
        log.debug("getSequenceCommand executeFailureCase");

        try {
            sequenceAdminServiceStub.getSequence(name);
            fail("Expected exception did not occur");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SequenceEditorException e) {
            e.printStackTrace();
            fail("Unexpected exception thrown");
        }
        log.info("getSequenceCommand FailureCase passed");
    }

}