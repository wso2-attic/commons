/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


package org.wso2.automation.tools.jmeter.test;

import junit.framework.Assert;
import tools.jmeter.core.JmeterCore;
import tools.jmeter.core.JmeterProperties;
import tools.jmeter.core.JmeterResults;
import org.wso2.carbon.system.test.core.TestTemplate;

import java.util.LinkedList;
import java.util.List;


public class jmeterTest extends TestTemplate {
    @Override
    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
        testClassName = jmeterTest.class.getName();
    }

    @Override
    public void runSuccessCase() {
        boolean status = true;

        List<String> errorList = new LinkedList<String>();
        JmeterProperties jmeterProperties = new JmeterProperties();
        jmeterProperties.setProperty("/home/dharshana/jmeterSample", "/home/dharshana/jmeterSample", "/home/dharshana/stratos/target/jmeter", "/home/dharshana/jmeterSample/jmeter.log");
        JmeterCore jmeterCore = new JmeterCore();
        List<JmeterResults> results = jmeterCore.runTest(jmeterProperties);
        for (int length = 0; length <= results.size() - 1; length++) {
            JmeterResults resultSet = results.get(length);
            status = resultSet.getExecuteState();
            for (int mapItem = 0; mapItem <= resultSet.getAssertReport().size() - 1; mapItem++) {
                Object key = resultSet.getAssertReport().keySet().toArray()[mapItem];
                String tagValue = resultSet.getAssertReport().get(key);
                if (tagValue.contains("Test failed")) {
                    status = false;
                    errorList.add(mapItem, resultSet.getAssertReport().toString());
                }
            }
        }
        if (status) {
            System.out.println(errorList.toString());
            Assert.fail(errorList.toString());
        }
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
