/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.api;

import org.junit.Assert;
import org.junit.Test;
import org.wso2.siddhi.api.eventstream.EventStream;
import org.wso2.siddhi.api.exception.SiddhiPraserException;
import org.wso2.siddhi.compiler.SiddhiCompiler;

import java.util.List;

public class InputStreamTestCase {

    @Test
    public void stream1TestCase() throws SiddhiPraserException {
        List<EventStream> eventStreamList = null;
        eventStreamList = SiddhiCompiler.parse("cseEventStream:=symbol[string] , price[int];");
        Assert.assertTrue(eventStreamList.size() == 1);
    }

    @Test
    public void stream2TestCase() throws SiddhiPraserException {
        List<EventStream> eventStreamList = null;
        eventStreamList = SiddhiCompiler.parse("cseEventStream:=symbol[string], price[int];" +
                                               "infoStock:=action[string], timeStamp[long];");
        Assert.assertTrue(eventStreamList.size() == 2);
    }
}
