package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;
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

public class ESBFaultMediatorTest extends TestCase {
    Selenium selenium;

    public ESBFaultMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Fault mediator
	 */
    public void testSetFaultMediatorSoapVersion(String level, String soapVersion, String faultCode) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        if (soapVersion.equals("soap_version")){
            selenium.click("soap_version");
            selenium.select("fault_code1", "label="+faultCode);
        } else {
            selenium.click("//input[@id='soap_version' and @name='soap_version' and @value='2'");            
            selenium.select("fault_code2", "label="+faultCode);
        }
    }

    //Selecting the Fault String type (Value or Expression)
    public void testSetFaultCode(String faultString, String faultValue, String namespacePrefix, String namespaceUri) throws Exception{
        if (faultString.equals("fault_string")){
		    selenium.click("fault_string");
    		selenium.type("name_space", faultValue);
        } else {
		    selenium.click("//input[@name='fault_string' and @value='expression']");
    		selenium.type("name_space", faultValue);
            selenium.click("nmsp_button");
            Thread.sleep(2000);
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddNamespace(namespacePrefix,namespaceUri);
        }
    }

    //This method will set the rest of the Fault mediator properties
    public void testSetFaultGeneralInfo(String soapVersion, String faultActor, String node, String detail) throws Exception{
        if (soapVersion.equals("soap11")){
            selenium.type("fault_actor", faultActor);
            selenium.type("detail", detail);
        } else {
            selenium.type("fault_actor", faultActor);
            selenium.type("node", node);
            selenium.type("detail", detail);
        }
    }
}
