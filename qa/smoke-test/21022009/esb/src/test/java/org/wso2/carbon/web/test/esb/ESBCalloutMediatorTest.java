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

public class ESBCalloutMediatorTest   extends TestCase {
    Selenium selenium;

    public ESBCalloutMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add the main Callout mediator information
	 */
    public void testAddCalloutMediator(String level, String svcURL, String action, String repo, String axis2XML) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);

        if (svcURL!=null) {
            selenium.type("mediator.callout.svcURL", svcURL);
        }

        if (action !=null){
		    selenium.type("mediator.callout.action", action);
        }

        if (repo !=null){
    		selenium.type("mediator.callout.repo", repo);
        }

        if (axis2XML !=null){
    		selenium.type("mediator.callout.axis2XML", axis2XML);
        }
    }


   /*
   This method will add the 'Source' information of the Callout mediator
    */
   public void testAddSourceInfo(String sourceXpath, String sourceSpecifyAs, String xpathNamespacePrefix, String xpathNamespaceURI, String sourceResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (sourceSpecifyAs.equals("sourceGroupXPath")){
            selenium.click("sourceGroupXPath");
            selenium.type("source_xpath", sourceXpath);
            selenium.click("mediator.callout.source.xpath_nmsp_button");

            esbCommon.testAddNamespace(xpathNamespacePrefix,xpathNamespaceURI);
        } else {
            selenium.click("//input[@name='sourcegroup' and @value='Key']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.callout.source.key_val')\"]");

            esbCommon.testSelectResource("Entry",sourceResource);
        }
   }

   /*
   This method will add the 'Target' information of the Callout mediator
    */
   public void testAddTargetInfo(String targetSpecifyAs, String targetNamespacePrefix, String targetNamespaceURI, String targetResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (targetSpecifyAs.equals("targetGroupXPath")){
            selenium.click("targetGroupXPath");
            selenium.type("mediator.callout.target.xpath_val", "//mo:div");
            selenium.click("mediator.callout.target.xpath_nmsp_button");

            esbCommon.testAddNamespace(targetNamespacePrefix, targetNamespaceURI);
        } else {
            selenium.click("//input[@name='targetgroup' and @value='Key']");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.callout.target.key_val')\"]");

            esbCommon.testSelectResource("Entry",targetResource);
        }
   }

}
