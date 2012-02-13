package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.Tryit;/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class BPSTryitTest extends CommonSetup {
    public BPSTryitTest(String text) {
        super(text);
    }

    public void testExternalTryit() throws Exception {
        Tryit tryit = new Tryit(browser);
        tryit.invokeTryit("external","Axis2Service", "echoString", "s", "wso2", 4, "wso2");
        tryit.invokeTryit("external","HelloService", "greet", "name", "wso2", 1, "Hello World, wso2 !!!");
        BPSLogin.loginToConsole("admin", "admin");
        tryit.invokeTryit("internal","Axis2Service", "echoString", "s", "wso2", 4, "wso2");
        

    }
}
