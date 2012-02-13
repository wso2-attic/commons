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

public class ESBXqueryMediatorTest  extends TestCase {
   Selenium selenium;
   int targetNsLevel = 0;
   int variableNsLevel = 0;
   int variableCount = 0;

    public ESBXqueryMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
     This  method will add an Xquery mediator and it's mandatory properties
     */
    public void addXqueryKey(String level, String resource) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(4000);
		selenium.click("link=Registry Keys");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.selectResource("Entry",resource);
    }

    /*
    This method adds targets to the mediator
     */
    public void addTarget(String target) throws Exception{
        selenium.type("mediator.xquery.target", target);        
    }

    /*
    This method will add variable to the Xquery mediator
     */
    public void addVariables(String variableType, String variableName, String value) throws Exception{
		selenium.click("link=Add Variable");
        Thread.sleep(2000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        selenium.select("variableType"+variableCount, "label="+variableType);
		selenium.type("variableName"+variableCount, variableName);
        selenium.select("variableTypeSelection"+variableCount, "label=Value");
        if (value != null){
            selenium.type("variableValue"+variableCount, value);
        }
        variableCount++;
    }

    /*
    Adds expression type variables
     */
    public void addExpressionVariable(String variableType, String variableName, String value, String resource) throws Exception{
        int registryKeyNo=variableCount;
		selenium.click("link=Add Variable");
        Thread.sleep(2000);
        ESBCommon esbCommon = new ESBCommon(selenium);

        selenium.select("variableType"+variableCount, "label="+variableType);
		selenium.type("variableName"+variableCount, variableName);
        selenium.select("variableTypeSelection"+variableCount, "label=Expression");

        if (value !=null){
            selenium.type("variableValue"+variableCount, value);
        }
        
        if (resource !=null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('registryKey"+registryKeyNo+"')\"]");
            esbCommon.selectResource("Entry",resource);
        }
        variableCount++;
    }

    /*
    This method will add Target namespaces
     */
    public void addTargetNamespace(String targetNSPrefix, String targetNSUri) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("link=NameSpaces");
        Thread.sleep(2000);
        esbCommon.nsLevel=targetNsLevel;
        esbCommon.addNamespace(targetNSPrefix, targetNSUri);
        targetNsLevel=targetNsLevel+1;    
    }


    /*
    This method will add Variable namespaces
     */
    public void addVariableNamespace(String variableNSPrefix, String variableNSUri) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("//a[@onclick=\"showNameSpaceEditor('variableValue"+variableNsLevel+"')\"]");
        Thread.sleep(2000);
        esbCommon.nsLevel=variableNsLevel;
        esbCommon.addNamespace(variableNSPrefix, variableNSUri);
        variableNsLevel=variableNsLevel+1;
    }




    public void setTargetNsLevel() throws Exception{
        targetNsLevel=0;
    }

    public void setVariableNsLevel() throws Exception{
        variableNsLevel=0;
    }
}
