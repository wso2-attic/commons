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

public class ESBDBReportMediatorTest extends TestCase {

    Selenium selenium;

    public ESBDBReportMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an DBreport mediator and it's mandatory properties for Pool connections
     */
    public void testAddDBreportMediatorPoolInfo(String level, String driver, String url, String user, String password) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);

		selenium.click("radio_pool");
        selenium.type("driver", driver);
		selenium.type("url", url);
		selenium.type("user", user);
		selenium.type("password", password);
    }

    /*
     This  method will add an DBreport mediator and it's mandatory properties for Pool connections
     */
    public void testAddDBreportMediatorDSInfo(String level, String dsType, String initCtx, String dataSource, String url, String user, String password) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("radio_datasource");
		if (dsType.equals("sourceTypeInline")){
            selenium.click("sourceTypeInline");
            selenium.type("init_ctx", initCtx);
            selenium.type("data_source", dataSource);
            selenium.type("url", url);
            selenium.type("user", user);
            selenium.type("password", password);
        } else {
		    selenium.click("radio_datasource");            
		    selenium.click("link=Load Data Sources");
        }
    }

    /*
     This  method will add Properties of the DBreport mediator
     */
    public void testAddDBreportMediatorProperties(String property, String propertyVal) throws Exception {
		selenium.click("link=Add Property");
        Thread.sleep(2000);
        selenium.select("property1", "label="+property);
		selenium.select("property_value1", "label="+propertyVal);
    }

    /*
     This  method will add SQL Statment information of the DBreport mediator
     */
    public void testAddDBreportMediatorSQLStatement(String sqlValue, String paramType, String propType, String value, String namepsacePrefix, String namespaceUri) throws Exception {
        selenium.click("link=Add Statement");
        Thread.sleep(2000);
        selenium.type("sql_val1", sqlValue);
        selenium.click("link=Add Parameter");
        selenium.select("javaType1.1", "label="+paramType);

        if (propType.equals("Value")){
            selenium.select("parameterType1.1", "label=Value");
            selenium.type("parameter_value1.1", value);
        } else {
            selenium.select("parameterType1.1", "label=Expression");
            selenium.type("parameter_value1.1", value);
            selenium.click("paramNS1.1");
            ESBCommon esbCommon = new ESBCommon(selenium);
            esbCommon.testAddNamespace(namepsacePrefix,namespaceUri);
        }
    }    
}
