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
package org.wso2.carbon.test.framework;

import org.junit.Test;
import org.wso2.carbon.test.framework.util.server.SampleAxis2Server;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 11, 2010
 * Time: 11:55:46 AM
 *
 * This is to test, staring and stopping of Sample Axis2 Servers
 *
 */
public class SampleAxis2ServerTest extends initTestCase{
    @Test
    public void build()   {

        SampleAxis2Server.build("SimpleStockQuoteService");

    }

    @Test
    public void serverStart() throws Exception {

        SampleAxis2Server.Start("9001","9005","MyServer1");
        SampleAxis2Server.Start();
    }

    @Test
    public void serverStop() throws Exception {
        SampleAxis2Server.Stop("MyServer1");
         SampleAxis2Server.Stop();

    }
}
