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
    int xsltNsLevel=0;
    int sourceNsLevel=0;
    int propertyValNo=0;
    int propertyExpNo=0;
    int propertyNo=0;
    int featureNo=0;

    public ESBXSLTMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an XSLT mediator and it's mandatory properties
     */
    public void addXSLTMediator(String level, String resource) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);

		selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.xslt.key')\"]");
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.selectResource("Entry",resource);
    }

    /*
    Adding the xslt source
     */
    public void addXsltSource(String source) throws Exception{
        selenium.type("mediator.xslt.source", source);
    }

    /*
    This method will add properties to the XSLT mediator
     */
    public void addValProperties(String propertyName, String propertVal) throws Exception{
        selenium.click("link=Add Property");
        selenium.type("propertyName"+propertyNo, propertyName);
        selenium.select("propertyTypeSelection"+propertyNo, "label=Value");
        selenium.type("propertyValue"+propertyNo, propertVal);
        propertyNo++;
    }

    /*
    Adding value type properties
     */
    public void addExpressionProp(String propertyName, String propertVal) throws Exception{
        selenium.click("link=Add Property");
        selenium.type("propertyName"+propertyNo, propertyName);
        selenium.select("propertyTypeSelection"+propertyNo, "label=Expression");
        selenium.type("propertyValue"+propertyNo, propertVal);
        propertyNo++;
    }

    /*
    This method will add features to the XSLT mediator
     */
    public void addFeatures(String featureName, String featureVal) throws Exception{
        selenium.click("link=Add Feature");
        selenium.type("featureName"+featureNo, featureName);
		selenium.select("featureValue"+featureNo, "label="+featureVal);
        featureNo++;
    }

    /*
    This method will add namespaces to the Property mediator
     */
    public void addSourceNamespace(String prefix, String uri) throws Exception{
        selenium.click("link=NameSpaces");
        Thread.sleep(3000);

        boolean pref = selenium.isTextPresent(prefix);

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("addNSButton");
            selenium.type("prefix"+sourceNsLevel, prefix);
            selenium.type("uri"+sourceNsLevel, uri);
            sourceNsLevel++;
            selenium.click("saveNSButton");
        }
    }

    public void setXsltNsLevel() throws Exception{
        xsltNsLevel=0;
    }

    /*
    Adds namespaces for the source
     */
    public void addPropNamespace(String prefix, String uri) throws Exception{
        propertyNo=propertyNo-1;
        selenium.click("//a[@onclick=\"showNameSpaceEditor('propertyValue"+propertyNo+"')\"]");
        Thread.sleep(3000);

        boolean pref = selenium.isTextPresent(prefix);

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("addNSButton");
            selenium.type("prefix"+xsltNsLevel, prefix);
            selenium.type("uri"+xsltNsLevel, uri);
            xsltNsLevel++;
            selenium.click("saveNSButton");
        }
    }

    public void setSourceNsLevel() throws Exception{
        sourceNsLevel=0;
    }
}
