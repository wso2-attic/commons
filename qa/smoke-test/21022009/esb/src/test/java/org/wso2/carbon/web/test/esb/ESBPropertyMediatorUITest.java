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

public class ESBPropertyMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBPropertyMediatorUITest(Selenium _browser){
        selenium = _browser;
    }

    /*
	 * This method will verify the properties of the Property mediator
	 */
    public void testAddPropertyMediator(String level, String action, String actionAsVal) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);

        ESBCommon esbCommon = new ESBCommon(selenium);
        assertEquals("Property Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        assertTrue(selenium.isTextPresent("exact:Name *"));
        assertTrue(selenium.isTextPresent("Action"));
        assertTrue(selenium.isTextPresent("Set"));
        assertTrue(selenium.isTextPresent("Remove"));
        assertTrue(selenium.isTextPresent("Scope"));

        if (action.equals("set")){
            selenium.click("set");
            assertEquals("on", selenium.getValue("set"));
            assertTrue(selenium.isTextPresent("Set Action as"));
            assertTrue(selenium.isTextPresent("Value"));
            assertTrue(selenium.isTextPresent("Expression"));

            if (actionAsVal.equals("value")){
                selenium.click("value");
                assertEquals("on", selenium.getValue("value"));
                assertTrue(selenium.isTextPresent("Value *"));
            }else if (actionAsVal.equals("expression")){
                selenium.click("expression");
                assertEquals("on", selenium.getValue("expression"));
                assertTrue(selenium.isTextPresent("Expression *"));
                assertEquals("Namespaces", selenium.getText("mediator.property.nmsp_button"));
            } else if (action.equals("remove")){
                selenium.click("remove");
            }

            assertEquals("Synapse Transport Axis2", selenium.getText("mediator.property.scope"));
            assertTrue(selenium.isTextPresent("Synapse Transport Axis2"));
            assertEquals("Update", selenium.getValue("//input[@value='Update']"));

//            esbCommon.testMediatorHelp("Property");
        }
    }
}
