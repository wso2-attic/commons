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
    int headerNsLevel = 0;
    int actionNsLevel = 0;

    public ESBHeaderMediatorTest(Selenium _browser){
		selenium = _browser;
    }

  /*
   This method is used to add Header mediators
   */
    public void addHeaderMediator(String level, String headerName) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.type("mediator.header.name", headerName);
    }

    /*
     This method is used to add Header mediators
     */
    public void setHeaderAction(String actionOpt, String headerVal) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
            selenium.click("set");
            if (actionOpt.equals("value")){
                selenium.click("value");
                selenium.type("mediator.header.val_ex", headerVal);
            } else {
                selenium.click("expression");
                selenium.type("mediator.header.val_ex", headerVal);
            }
      }

    /*
    This method will remove the header
     */
    public void removeHeader() throws Exception{
        selenium.click("remove");
    }

    /*
    This method will add namespaces to the name of the header mediator
     */
    public void addHeaderNameNamespace(String headerNSPrefix, String headerNSUri) throws Exception{
        boolean pref = selenium.isTextPresent(headerNSPrefix);
        Thread.sleep(1000);
        selenium.click("mediator.header.name.namespace_button");

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(2000);
            selenium.type("prefix"+headerNsLevel, headerNSPrefix);
            selenium.type("uri"+headerNsLevel, headerNSUri);
            headerNsLevel++;
            selenium.click("saveNSButton");
        }
    }

    /*
    This method will add namespaces to the expression header mediator
     */
    public void addHeaderExpNamespace(String actionNSPrefix, String actionNSUri) throws Exception{
        boolean pref = selenium.isTextPresent(actionNSPrefix);
        selenium.click("mediator.header.expression.namespace_button");
        Thread.sleep(2000);

        if (pref) {
		    selenium.click("link=X");
        } else {
            selenium.click("addNSButton");
            selenium.type("prefix"+actionNsLevel, actionNSPrefix);
            selenium.type("uri"+actionNsLevel, actionNSUri);
            actionNsLevel++;
            selenium.click("saveNSButton");
        }
    }

    /*
    This method will set the headerNamespace value to 0
     */
    public void setHeaderNSVal() throws Exception{
        headerNsLevel = 0;
    }

    /*
    This method will set the actionNsLevel value to 0
     */
    public void setActionNSVal() throws Exception{
        actionNsLevel = 0;
    }
}
