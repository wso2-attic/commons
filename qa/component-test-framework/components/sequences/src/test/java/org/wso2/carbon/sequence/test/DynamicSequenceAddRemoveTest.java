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
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.sequence.test.commands.InitializeSequenceAdminCommand;
import org.wso2.carbon.sequence.test.commands.SequencesAdminCommand;
import org.wso2.carbon.sequences.ui.types.SequenceAdminServiceStub;

public class DynamicSequenceAddRemoveTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(DynamicSequenceAddRemoveTest.class);

    @Override
    public void init() {
        log.info("Dynamic Sequence admin service tests initialized");
    }

    @Override
    public void runSuccessCase() {

        try {

            SequenceAdminServiceStub sequenceAdminServiceStub = new InitializeSequenceAdminCommand().executeAdminStub(sessionCookie);


            String sequenceXML = "<sequence xmlns=\"http://ws.apache.org/ns/synapse\">\n" +
                                 "   <property name=\"prop1\" value=\"aaa\" scope=\"default\" type=\"STRING\" />\n" +
                                 "</sequence>";
            OMElement oMElement = AXIOMUtil.stringToOM(sequenceXML);
            System.out.println(oMElement);
            // SequenceInfo[] sequenceList = new SequencesAdminCommand(sequenceAdminServiceStub).getDynamicSequesncesSuccessCase(0, 100);

            // System.out.println(sequenceAdminServiceStub.getDynamicSequence("conf:/testDynamicSeq"));

            int count_before = new SequencesAdminCommand(sequenceAdminServiceStub).getDynamicSequenceCountExecuteSuccessCase();

            // add dynamic sequence
            new SequencesAdminCommand(sequenceAdminServiceStub).addDynamicSequenceExecuteSuccessCase("conf:/testDynamicSeq", oMElement);
            new SequencesAdminCommand(sequenceAdminServiceStub).addDynamicSequenceExecuteSuccessCase("gov:/testDynamicSeq", oMElement);
            int count_after = new SequencesAdminCommand(sequenceAdminServiceStub).getDynamicSequenceCountExecuteSuccessCase();

            if (count_after - count_before == 2) {
                System.out.println("Dynamic Sequences Added Successfully");
                log.info("Dynamic Sequence Added Successfully");
            } else {
                Assert.fail("Dynamic Sequences Adding Failed");
                log.error("Dynamic Sequences Adding");

            }

            new SequencesAdminCommand(sequenceAdminServiceStub).deleteDynamicSequenceExecuteSuccessCase("conf:/testDynamicSeq");
            new SequencesAdminCommand(sequenceAdminServiceStub).deleteDynamicSequenceExecuteSuccessCase("gov:/testDynamicSeq");
            count_after = new SequencesAdminCommand(sequenceAdminServiceStub).getDynamicSequenceCountExecuteSuccessCase();

            if (count_after == count_before) {
                System.out.println("Dynamic Sequences removed successfully");
                log.info("Dynamic Sequences removed successfully");

            } else {
                Assert.fail("Dynamic Sequences cannot be removed");
                log.error("Dynamic Sequences cannot be removed");

            }

        }

        catch (Exception e) {
            log.error("Dynamic Sequence admin service test failed : " + e.getMessage());
            Assert.fail("Dynamic Sequence admin service test failed : " + e.getMessage());
        }

    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
