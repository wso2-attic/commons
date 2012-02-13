package org.wso2.carbon.sequence.test;

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

import junit.framework.Assert;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.sequence.test.commands.InitializeSequenceAdminCommand;
import org.wso2.carbon.sequence.test.commands.SequencesAdminCommand;
import org.wso2.carbon.sequences.ui.types.SequenceAdminServiceStub;

import java.io.File;

public class SequenceAddRemoveTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SequenceAddRemoveTest.class);

    @Override
    public void init() {
        log.info("Sequence admin service tests initialized");
    }

    @Override
    public void runSuccessCase() {

        try {

            SequenceAdminServiceStub sequenceAdminServiceStub = new InitializeSequenceAdminCommand().executeAdminStub(sessionCookie);
            int count_before = new SequencesAdminCommand(sequenceAdminServiceStub).getSequencesCountSuccessCase();

            String xmlPath = frameworkPath + File.separator + "components" + File.separator + "sequences"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "synapse.xml";

            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            // add new sequence
            new SequencesAdminCommand(sequenceAdminServiceStub).addSequenceExecuteSuccessCase(omElement);
            new SequencesAdminCommand(sequenceAdminServiceStub).getSequenceExecuteSuccessCase("testFwSeq");
            new SequencesAdminCommand(sequenceAdminServiceStub).enableStatisticsSequenceExecuteSuccessCase("testFwSeq");
            new SequencesAdminCommand(sequenceAdminServiceStub).enableTracingSequenceExecuteSuccessCase("testFwSeq");

            System.out.println(sequenceAdminServiceStub.getSequence("testFwSeq"));
            int count_after = new SequencesAdminCommand(sequenceAdminServiceStub).getSequencesCountSuccessCase();

            if (count_after - count_before == 1) {
                System.out.println("Sequence Added Successfully");
                log.info("Sequence Added Successfully");
            } else {
                Assert.fail("Sequence getContent Test Failed");
                log.error("Sequence getContent Test Failed");

            }

            new SequencesAdminCommand(sequenceAdminServiceStub).deleteSequenceExecuteSuccessCase("testFwSeq");
            count_after = new SequencesAdminCommand(sequenceAdminServiceStub).getSequencesCountSuccessCase();

            if (count_after == count_before) {
                System.out.println("Sequence Removed Successfully");
                log.info("Sequence Removed Successfully");

            } else {
                Assert.fail("Sequence Removed Test Failed");
                log.error("Sequence Removed Test Failed");

            }

        }

        catch (Exception e) {
            log.error("Sequence admin service test failed due to the exception : " + e.getMessage());
            Assert.fail("Sequence admin service test failed due to the exception : " + e.getMessage());
        }

    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
