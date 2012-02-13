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

public class SequenceAdminServiceTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(SequenceAdminServiceTest.class);

    @Override
    public void init() {
        log.info("sequence admin service tests initialized");

    }

    @Override
    public void runSuccessCase() {
        OMElement result = null;
        try {

            SequenceAdminServiceStub sequenceAdminServiceStub = new InitializeSequenceAdminCommand().executeAdminStub(sessionCookie);
            String xmlPath = frameworkPath + File.separator + "sequences"
                             + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "synapse.xml";

            OMElement omElement = ConfigHelper.createOMElement(xmlPath);

            // add new sequence
            new SequencesAdminCommand(sequenceAdminServiceStub).addSequenceExecuteSuccessCase(omElement);
            new SequencesAdminCommand(sequenceAdminServiceStub).getSequenceExecuteSuccessCase("testSeq");
            new SequencesAdminCommand(sequenceAdminServiceStub).enableStatisticsSequenceExecuteSuccessCase("testSeq");
            new SequencesAdminCommand(sequenceAdminServiceStub).enableTracingSequenceExecuteSuccessCase("testSeq");

            System.out.println(sequenceAdminServiceStub.getSequence("testSeq"));
            boolean isFound = result.getChildren().next().toString().contains("<ns:return xmlns:ns=\"http://org.apache.synapse/xsd\"><sequence xmlns=\"http://ws.apache.org/ns/synapse\" name=\"testSeq\"><property name=\"prop1\" value=\"&quot;testVal&quot;\" scope=\"default\" type=\"STRING\" /><log /></sequence></ns:return>");
            if (!isFound) {
                Assert.fail("Sequence getContent Test Failed");
                log.error("Sequence getContent Test Failed");
            }

        }

        catch (Exception e) {
        }

    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
