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

public class ESBHeaderMediatorTest extends TestCase {
    Selenium selenium;

    public ESBHeaderMediatorTest(Selenium _browser){
		selenium = _browser;
    }

  /*
   This method is used to add Header mediators
   */
    public void testAddHeaderMediator(String level, String headerName, String headerNSPrefix, String headerNSUri) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.type("mediator.header.name", headerName);

      if (headerNSPrefix!=null){
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddNamespace(headerNSPrefix, headerNSUri);
      }
    }

    /*
     This method is used to add Header mediators
     */
    public void testSetHeaderAction(String action, String actionOpt, String headerVal, String actionNSPrefix, String actionNSUri) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (action.equals("set")){
            selenium.click("set");

            if (actionOpt.equals("value")){
                selenium.click("value");
                selenium.type("mediator.header.val_ex", headerVal);
            } else {
                selenium.click("expression");
                selenium.type("mediator.header.val_ex", headerVal);
                selenium.click("mediator.header.expression.namespace_button");
                esbCommon.testAddNamespace(actionNSPrefix,actionNSUri);
            }
        } else {
            selenium.click("remove");
        }
        esbCommon.testMediatorUpdate();
      }
}
