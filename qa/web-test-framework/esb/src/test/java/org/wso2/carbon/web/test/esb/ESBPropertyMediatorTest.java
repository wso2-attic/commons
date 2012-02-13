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

public class ESBPropertyMediatorTest  extends TestCase {
    Selenium selenium;

    public ESBPropertyMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will add basic information of the Property mediator
     */
    public void addBasicPropInfo(String level, String propertyName, String action) throws Exception{
        Thread.sleep(3000);        
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        //Setting the Property name
        selenium.type("mediator.property.name", propertyName);

        if (action.equalsIgnoreCase("set")){
            selenium.click("set");
        } else {
            selenium.click("remove");
        }
    }

    /*
	 * This method will add a Property mediator
	 */
    public void addPropertyMediator(String actionAsVal, String propertyValue, String scope) throws Exception {
            //Checking whether the 'Set Action as' property is 'value' or 'expression'
        if(actionAsVal!=null){
            if (actionAsVal.equalsIgnoreCase("expression")){
                selenium.click("expression");
            } else {
                selenium.click("value");
            }
        }
            //Setting the property value
        if(propertyValue!=null)
            selenium.type("mediator.property.val_ex", propertyValue);
        if(scope!=null){
            selenium.select("mediator.property.scope", "label="+scope);
        }
    }

    /*
    This method will add namespaces to the Property mediator
     */
    int propNsLevel=0;
    public void addPropNamespace(String prefix, String uri) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("mediator.property.nmsp_button");
        Thread.sleep(3000);

        boolean pref = selenium.isTextPresent(prefix);

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("addNSButton");
            selenium.type("prefix"+propNsLevel, prefix);
            selenium.type("uri"+propNsLevel, uri);
            propNsLevel++;
            selenium.click("saveNSButton");
        }
    }
}
