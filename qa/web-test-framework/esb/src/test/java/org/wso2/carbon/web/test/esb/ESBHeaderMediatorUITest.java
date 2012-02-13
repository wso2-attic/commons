package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

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

public class ESBHeaderMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBHeaderMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

  /*
   This method is used to add Header mediators
   */
    public void testVerifyHeaderMediator(String level) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("Header Mediator"));
		assertTrue(selenium.isTextPresent("Name  *"));
		assertTrue(selenium.isElementPresent("mediator.header.name.namespace_button"));
		assertTrue(selenium.isTextPresent("Action :"));
		assertTrue(selenium.isTextPresent("Set"));
		assertTrue(selenium.isTextPresent("Remove"));
		assertTrue(selenium.isTextPresent("Value"));
		assertTrue(selenium.isTextPresent("Expression *"));
		assertTrue(selenium.isElementPresent("//input[@value='Update']"));
  }
}
