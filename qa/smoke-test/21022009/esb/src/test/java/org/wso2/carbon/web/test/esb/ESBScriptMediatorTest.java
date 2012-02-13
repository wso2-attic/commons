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
	 * This method will add a Script mediator
	 */
    public void testAddScriptMediator(String level, String scriptType, String scriptLangauge, String source, String function, String resourceName, String includeKey) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (scriptType.equals("Inline")){
		    selenium.select("script_type", "label="+scriptType);
            Thread.sleep(1000);
            selenium.type("mediator.script.source_script", source);
        } else if (scriptType.equals("Registry Key")){
            selenium.select("script_type", "label="+scriptType);
            Thread.sleep(1000);
            selenium.type("mediator.script.function", function);
		    selenium.click("link=Registry Key");
            Thread.sleep(5000);
            esbCommon.testSelectResource("Entry", resourceName);

            if (includeKey != null){
                selenium.click("link=Add include key");
                Thread.sleep(2000);
                selenium.click("link=Registry Keys");
                Thread.sleep(5000);
                esbCommon.testSelectResource("Entry", includeKey);
            }
        }
		    selenium.select("mediator.script.language", "label="+ scriptLangauge);
            esbCommon.testMediatorUpdate();
    }
}
