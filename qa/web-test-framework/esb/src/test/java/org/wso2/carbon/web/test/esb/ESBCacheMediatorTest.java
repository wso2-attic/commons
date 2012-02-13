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

public class ESBCacheMediatorTest extends TestCase {
    Selenium selenium;

    public ESBCacheMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Cache mediator  main information
	 */
    public void addCacheMediatorMainInfo(String level, String cacheId, String cacheScope, String cacheType, String cacheTimeout, String maxMsgSize) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        if(cacheId!=null)
            selenium.type("cacheId", cacheId);
        if(cacheScope!=null)
		    selenium.select("cacheScope", "label="+cacheScope);
        if(cacheType!=null)
            selenium.select("cacheType", "label="+cacheType);
        if(cacheTimeout!=null)
            selenium.type("cacheTimeout", cacheTimeout);
        if(maxMsgSize!=null)
		    selenium.type("maxMsgSize", maxMsgSize);
    }

    /*
    This method will add Cache implementation details
     */
    public void addCacheImplementationInfo(String impType, String maxSize) throws Exception{
		selenium.type("impType", impType);
		selenium.type("maxSize", maxSize);
    }

    /*
    This method will add onCache Hit details
     */
    public void addOnCacheHitInfo(String onCacheHit, String resourceName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        if (onCacheHit.equals("sequenceOptionAnon")){
            selenium.click("sequenceOptionAnon");
        }else {
            selenium.click("sequenceOptionReference");
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('mediator.sequence')\"]");
            Thread.sleep(2000);
            esbCommon.selectResource("Sequence", resourceName);
        }
    }

}
