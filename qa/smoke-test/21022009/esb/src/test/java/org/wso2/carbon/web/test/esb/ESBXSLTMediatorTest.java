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

public class ESBXSLTMediatorTest extends TestCase {

    Selenium selenium;

    public ESBXSLTMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an XSLT mediator and it's mandatory properties
     */
    public void testAddXSLTMediator(String level, String resource, String source, String namepsacePrefix, String namespaceUri) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);

		selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.xslt.key')\"]");
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testSelectResource("Entry",resource);

        if (source!=null){
            selenium.type("mediator.xslt.source", "//m0:add");
            selenium.click("link=NameSpaces");
            esbCommon.testAddNamespace(namepsacePrefix,namespaceUri);
        }
    }

    /*
    This method will add properties to the XSLT mediator
     */
    public void testAddProperties(String propertyName, String propertyType, String propertVal, String namespacePrefix, String namespaceUri) throws Exception{
        selenium.click("link=Add Property");
        if (propertyName!=null){
            selenium.type("propertyName0", propertyName);

            if (propertyType.equals("Value")){
                selenium.select("propertyTypeSelection0", "label=Value");
                selenium.type("propertyValue0", propertVal);
            } else {
                selenium.select("propertyTypeSelection0", "label=Expression");
                selenium.type("propertyValue0", propertVal);
                selenium.click("//a[@onclick=\"showNameSpaceEditor('propertyValue0')\"]");
                ESBCommon esbCommon = new ESBCommon(selenium);
                esbCommon.testAddNamespace(namespacePrefix,namespaceUri);
            }
        }
    }

    /*
    This method will add features to the XSLT mediator
     */
    public void testAddFeatures(String featureName, String featureVal) throws Exception{
        selenium.click("link=Add Feature");
        selenium.type("featureName0", featureName);
		selenium.select("featureValue0", "label="+featureVal);
    }
}
