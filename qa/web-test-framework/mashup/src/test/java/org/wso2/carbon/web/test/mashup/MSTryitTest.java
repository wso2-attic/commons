package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.Tryit;

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


public class MSTryitTest extends CommonSetup{

    public MSTryitTest(String text) {
           super(text);
    }

    //Invoke External Tryit..............
    public void testInvokeExternalTryit()throws Exception{
       selenium.open(MSCommon.loadProperties().getProperty("context.root")+"/carbon/admin/login.jsp"); 
       Tryit instTryit = new Tryit(selenium);
       instTryit.invokeTryit("external","allCommons", "echoString","param1","Hello",4, "Hello");
    }

    //Invoke External Tryit from Url.................
    public void testInvokeExternalTryitfromURL()throws Exception{
       Tryit instTryit = new Tryit(selenium);
       String URL1="http://" + MSCommon.loadProperties().getProperty("host.ip")+ ":" +MSCommon.loadProperties().getProperty("http.port")+MSCommon.loadProperties().getProperty("context.root")+ "/services/allCommons?wsdl";
       instTryit.invokeTryItByURL("allCommons", "echoString","param1","Hello",4, "Hello",URL1);

       String URL2="http://" + MSCommon.loadProperties().getProperty("host.ip")+ ":" +MSCommon.loadProperties().getProperty("http.port")+MSCommon.loadProperties().getProperty("context.root")+ "/services/allCommons?wsdl2";
       instTryit.invokeTryItByURL("allCommons", "echoString","param1","Hello",4, "Hello",URL2);
    }

    //Login to admin console..........
    public void testLoginTo()throws Exception{
       SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
       instSeleniumTestBase.loginToUI("admin","admin");
    }

    //Invoke Internal Tryit from Url...........
    public void testInvokeInternalTryitfromURL()throws Exception{
       Tryit instTryit = new Tryit(selenium);
       String URL1="http://" + MSCommon.loadProperties().getProperty("host.ip")+ ":" +MSCommon.loadProperties().getProperty("http.port")+MSCommon.loadProperties().getProperty("context.root")+ "/services/allCommons?wsdl";
       instTryit.invokeTryItByURL("allCommons", "echoString","param1","Hello",4, "Hello",URL1);

       String URL2="http://" + MSCommon.loadProperties().getProperty("host.ip")+ ":" +MSCommon.loadProperties().getProperty("http.port")+MSCommon.loadProperties().getProperty("context.root")+ "/services/allCommons?wsdl2";
       instTryit.invokeTryItByURL("allCommons", "echoString","param1","Hello",4, "Hello",URL2);
    }

    //Log out from admin console...............
    public void testLoginOut()throws Exception{
       SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
       instSeleniumTestBase.logOutUI();
    }


}
