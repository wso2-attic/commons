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

public class ESBValidateMediatorTest extends TestCase {
    Selenium selenium;

    public ESBValidateMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Validate mediator along with the schema key
	 */
    public void testAddValidateMediatorSchemaKey(String level, String resourceName) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
		Thread.sleep(2000);

		selenium.click("link=Registry Keys");
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testSelectResource("Entry", resourceName);
    }

    /*
    This method will be used to add the source of the Validate mediator
     */
    public void testAddSource(String source, String namespacePrefix, String namespaceURI) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (source != null){
            selenium.type("mediator.validate.source", source);
            selenium.click("link=Namespace");
            esbCommon.testAddNamespace(namespacePrefix, namespaceURI);
        }
    }

    /*
    This method will be used to add features to the Validate mediator
     */
    public void testAddFeatures(String featureName) throws Exception{
        if (featureName != null){
            selenium.click("link=Add feature");
            selenium.type("featureName0", featureName);
        }
    }
}
