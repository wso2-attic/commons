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
    int nsLevel=0;

    public ESBFilterMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method will add Xpath expressions for the Filter mediator
     */
    public void addXpath(String level, String xPath) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);
        selenium.click("xpath");
        selenium.type("filter_xpath", xPath);

    }

    /*
    This method will add Xpath expressions for the Filter mediator
     */
    public void addSourceRegex(String level, String src, String regex) throws Exception{
        Thread.sleep(2000);
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("xpathRex");
        selenium.type("filter_src", src);
        selenium.type("filter_regex", regex);
    }

    /*
	 * This method will add a Child mediators for the Then node
	 */
    public void addThenChildMediators(String level, String mediatorCategory, String mediatorName) throws Exception{
		selenium.click("link=Then");
        Thread.sleep(2000);
		selenium.click("//div[@id='mediator-"+level+"']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
        Thread.sleep(4000);
    }

    /*
	 * This method will add a Child mediators for the Else node
	 */
    public void addElseChildMediators(String level, String mediatorCategory, String mediatorName) throws Exception{
        selenium.click("link=Else");
        Thread.sleep(2000);
        selenium.click("//div[@id='mediator-"+level+"']/div/div[1]/a");

        Thread.sleep(1000);
        selenium.click("link="+mediatorCategory);
        Thread.sleep(1000);
        selenium.click("link="+mediatorName);
        Thread.sleep(4000);        
    }

    /*
    This method will add namespaces to the Filter mediator
     */
    public void addFilterNamespace(String prefix, String uri) throws Exception{
        selenium.click("mediator.callout.target.xpath_nmsp_button");
        Thread.sleep(2000);

        boolean pref = selenium.isTextPresent(prefix);

        if (pref) {
		    selenium.click("link=X");
        } else {
            Thread.sleep(1000);
            selenium.click("addNSButton");
            selenium.type("prefix"+nsLevel, prefix);
            selenium.type("uri"+nsLevel, uri);
            nsLevel++;
            selenium.click("saveNSButton");
        }
    }

    public void setNsLevel() throws Exception{
        nsLevel=0;
    }
}
