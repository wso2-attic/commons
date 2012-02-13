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

public class ESBScriptMediatorTest extends TestCase {
    Selenium selenium;

    public ESBScriptMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
    This method created inline script mediators
     */
    public void addInlineScripts(String level, String scriptLangauge, String source) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		selenium.select("script_type", "label=Inline");
        selenium.select("mediator.script.language", "label="+ scriptLangauge);
        selenium.type("mediator.script.source_script", source);
    }

    /*
    This method will create registry type script mediators
     */
    public void addRegistryScripts(String level, String scriptLangauge, String function, String resourceName) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);        
        ESBCommon esbCommon = new ESBCommon(selenium);
		selenium.select("script_type", "label=Registry Key");
        selenium.select("mediator.script.language", "label="+ scriptLangauge);
        selenium.type("mediator.script.function", function);
        selenium.click("link=Registry Key");
        Thread.sleep(5000);
        esbCommon.selectResource("Entry", resourceName);
    }

    /*
    This method adds include keys
     */
    public void addIncludeKeys(String includeKey) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=Add include key");
        Thread.sleep(2000);
        selenium.click("link=Registry Keys");
        Thread.sleep(5000);
        esbCommon.selectResource("Entry", includeKey);
    }
}
