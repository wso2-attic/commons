package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

/*
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

public class ESBXqueryMediatorTest  extends TestCase {

    Selenium selenium;

    public ESBXqueryMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an Xquery mediator and it's mandatory properties
     */
    public void testAddXqueryMediator(String level, String resource, String target, String namepsacePrefix, String namespaceUri) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);
		selenium.click("link=Registry Keys");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testSelectResource("Entry",resource);

        if (target!=null){
            selenium.type("mediator.xquery.target", "//m0:add");
            selenium.click("link=NameSpaces");
            esbCommon.testAddNamespace(namepsacePrefix,namespaceUri);
        }
    }

    /*
    This method will add variable to the Xquery mediator
     */
    public void testAddVariables(String variableType, String variableName, String valueType, String value, String resource, String namespacePrefix, String namespaceUri) throws Exception{
		selenium.click("link=Add Variable");
        Thread.sleep(2000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        selenium.select("variableType0", "label="+variableType);
		selenium.type("variableName0", variableName);

        if (valueType.equals("Value")){
            selenium.select("variableTypeSelection0", "label=Value");
            selenium.type("variableValue0", value);
        } else{
            selenium.select("variableTypeSelection0", "label=Expression");
            selenium.type("variableValue0", value);
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('registryKey0')\"]");
            esbCommon.testSelectResource("Entry",resource);

            selenium.click("//a[@onclick=\"showNameSpaceEditor('variableValue0')\"]");
            esbCommon.testAddNamespace(namespacePrefix, namespaceUri);
        }
    }
}
