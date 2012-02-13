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
	 * This method will add a Property mediator
	 */
    public void testAddPropertyMediator(String level, String propertyName, String action, String actionAsVal, String propertyValue, String scope, String nsPrefix, String nsUri) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        //Setting the Property name
        selenium.type("mediator.property.name", propertyName);

        //Checking whether the selected radio button option is 'set' or 'remove'
        if (action.equals("set")){
            selenium.click(action);
            selenium.click(actionAsVal);

            //Checking whether the 'Set Action as' property is 'value' or 'expression'
            if (actionAsVal.equals("expression")){

                //If the 'Set Action as' property is 'expression' add a namespace
                if (nsPrefix != null && nsUri !=null){
                    selenium.click("mediator.property.nmsp_button");
                    esbCommon.testAddNamespace(nsPrefix, nsUri);
                }
            }

            //Setting the property value
            selenium.type("mediator.property.val_ex", propertyValue);
        } else{

            //If action is 'remove' simply specify the scope only and update the mediator
            selenium.click(action);
            selenium.select("mediator.property.scope", "label="+scope);
        }
        
      //Update the mediator
      esbCommon.testMediatorUpdate();
    }
}
