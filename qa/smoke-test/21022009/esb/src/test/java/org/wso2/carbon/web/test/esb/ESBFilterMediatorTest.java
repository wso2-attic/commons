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

    
public class ESBFilterMediatorTest   extends TestCase {
    Selenium selenium;

    public ESBFilterMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Filter mediator
	 */
    public void testAddFilterMediator(String level, String xPath, String src, String regex, String namespacePrefix, String namespaceURI, String selectionType) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        
        Thread.sleep(2000);

        if (selectionType.equals("xpath")){
            selenium.click("xpath");
            selenium.type("filter_xpath", xPath);

            if (xPath.startsWith("//")){
                selenium.click("mediator.callout.target.xpath_nmsp_button");
                ESBCommon esbCommon = new ESBCommon(selenium);
                esbCommon.testAddNamespace(namespacePrefix, namespaceURI);
            }

        } else if (selectionType.equals("xpathRex")){
		    selenium.click("xpathRex");
            selenium.type("filter_src", src);
            selenium.type("filter_regex", regex);

            if (xPath.startsWith("//")){
                selenium.click("mediator.callout.target.xpath_nmsp_button");
                ESBCommon esbCommon = new ESBCommon(selenium);
                esbCommon.testAddNamespace(namespacePrefix, namespaceURI);
            }
        }

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testMediatorUpdate();
    }

    /*
	 * This method will add a Child mediators for the Then node
	 */
    public void testAddThenChildMediators(String mediatorCategory, String mediatorName) throws Exception{
		selenium.click("link=Then");
        Thread.sleep(2000);
		selenium.click("//div[@id='mediator-0.0']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
        Thread.sleep(2000);
    }

    /*
	 * This method will add a Child mediators for the Else node
	 */
    public void testAddElseChildMediators(String mediatorCategory, String mediatorName) throws Exception{
        selenium.click("link=Else");
        Thread.sleep(2000);
        selenium.click("//div[@id='mediator-0.1']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
        Thread.sleep(2000);        
    }
}
