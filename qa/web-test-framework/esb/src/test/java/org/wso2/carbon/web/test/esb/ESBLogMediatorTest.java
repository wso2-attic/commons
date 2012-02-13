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


public class ESBLogMediatorTest extends TestCase {
    Selenium selenium;
    int nsLevel_log=0;
    int propNo = 0;

    public ESBLogMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Log mediator
	 */
    public void addLogMediator(String level, String logLevel) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(10000);
		selenium.select("mediator.log.log_level", "label="+logLevel);
    }

    /*
    This method will add properties to the Log mediator
     */
    public void addLogPropety(String propertyName, String propertyTypeSel, String propertyValue) throws Exception{
		selenium.click("link=Add Property");
        Thread.sleep(3000);
        selenium.type("propertyName"+propNo, propertyName);
		selenium.select("propertyTypeSelection"+propNo, "label="+propertyTypeSel);
        Thread.sleep(1000);
        selenium.type("propertyValue"+propNo, propertyValue);
        propNo++;
    }

    public void setPropNo() throws Exception{
        propNo = 0;
    }

    public int getPropertyNo() throws Exception{
       return  propNo;
    }

    /*
    Adding namespaces to the Log mediator
     */
    public void addLogExpressionNameSpaces(String namespacePrefixAgg, String namespaceURIAgg) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        int property_no=getPropertyNo();
        if(property_no==0)
            selenium.click("link=NameSpaces");
        else
            selenium.click("//a[@onclick=\"showNameSpaceEditor('propertyValue"+(property_no-1)+"')\"]");
        
        Thread.sleep(2000);
        esbCommon.nsLevel=nsLevel_log;
        esbCommon.addNamespace(namespacePrefixAgg, namespaceURIAgg);
        nsLevel_log=nsLevel_log+1;
    }

    /*
    Setting nsLevel_log to 0
     */
    public void setNsLevel_log() throws Exception{
        nsLevel_log=0;
    }
}