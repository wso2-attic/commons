/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.sequences;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.sequences.ui.types.SequenceEditorException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 17, 2010
 * Time: 11:03:49 AM
 * Testing addition and removal of a sample sequence
 */
public class SequenceTest extends SequenceTestCase {

    @Test
    public void addingRemovingSequence() throws XMLStreamException, RemoteException, SequenceEditorException {
        String seq1 = "<sequence xmlns=\"http://ws.apache.org/ns/synapse\" name=\"test\">" +
                "   <in>" +
                "      <log />" +
                "      <send />" +
                "   </in>" +
                "   <out>" +
                "      <log />" +
                "      <send />" +
                "   </out>" +
                "</sequence>";
        OMElement documentElement = AXIOMUtil.stringToOM(seq1);

        sequenceAdminServiceStub.addSequence(documentElement);
        Assert.assertEquals(sequenceAdminServiceStub.getSequencesCount(), 3);
        OMElement element = (sequenceAdminServiceStub.getSequence("test")).getFirstElement();

        Assert.assertEquals(element.getLocalName(), "sequence");
        Assert.assertEquals(element.getAttributeValue(new QName("name")), "test");

        Iterator itrEle = element.getChildElements();
        if (itrEle.hasNext()) {
            OMElement ele = (OMElement) itrEle.next();
            Assert.assertEquals(ele.getLocalName(), "in");
        }
        if (itrEle.hasNext()) {
            OMElement ele = (OMElement) itrEle.next();
            Assert.assertEquals(ele.getLocalName(), "out");
        }
        sequenceAdminServiceStub.deleteSequence("test");
        Assert.assertEquals(sequenceAdminServiceStub.getSequencesCount(), 2);
    }


}
