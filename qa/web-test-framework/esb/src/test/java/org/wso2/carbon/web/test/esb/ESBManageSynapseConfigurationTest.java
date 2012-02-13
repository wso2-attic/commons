package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.ServiceManagement;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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

public class ESBManageSynapseConfigurationTest extends TestCase {
    Selenium selenium;

    public ESBManageSynapseConfigurationTest(Selenium _browser){
        selenium = _browser;
    }

    //Clicking on the Reset button
    public void clickReset() throws Exception{
        selenium.click("resetConfig");
    }

    //Clicking on the Update button
    public void clickUpdate() throws Exception{
        selenium.click("updateConfig");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
    }

    //Clicking on the Save button
    public void clickSave() throws Exception{
        selenium.click("saveConfig");
        selenium.waitForPageToLoad("30000");
        selenium.click("//button[@type='button']");
    }
    
    //Method to verify the Management Console
    public void verifyManagementConsole(String manageType,String name){
      selenium.click("link="+manageType);
      selenium.waitForPageToLoad("30000");
      assertTrue(selenium.isTextPresent(name));
    }

    //This method will read synapse.xml files
    public String readSynapseFile() throws Exception{
     ESBCommon esbCommon = new ESBCommon(selenium);
       Properties properties = new Properties();
       String conCatLines="exact:";
       try{
           // Open the file
           FileInputStream fstream = new FileInputStream(esbCommon.getCarbonHome()+"/conf/synapse.xml");
           // Get the object of DataInputStream
           DataInputStream in = new DataInputStream(fstream);
           BufferedReader br = new BufferedReader(new InputStreamReader(in));
           String strLine;

          //Read File Line By Line
           while ((strLine = br.readLine()) != null){
               strLine=strLine.trim();
               conCatLines=conCatLines+strLine+" ";
            }
          //Close the input stream
            in.close();
          //return conCatLines;
        }
       catch (Exception e){//Catch exception if any
        System.err.println("Error: " + e.getMessage());
        }
       return conCatLines;
    }

    /*
    This test will reset the Synapse configuration
     */
    public void resetSynapseConfig() throws Exception{
		selenium.click("link=Synapse");
        Thread.sleep(10000);
		selenium.click("resetConfig");
    }

    /*
    This test will update the Synapse configuration
     */
    public void updateSynapseConfig() throws Exception{
		selenium.click("link=Synapse");
        Thread.sleep(2000);
		selenium.click("updateConfig");
        Thread.sleep(10000);
		selenium.click("//button[@type='button']");
    }

    /*
    This test will save the Synapse configuration
     */
    public void saveSynapseConfig() throws Exception{
		selenium.click("link=Synapse");
        Thread.sleep(10000);
		selenium.click("updateConfig");
		selenium.waitForPageToLoad("30000");
		assertEquals("OK", selenium.getText("//button[@type='button']"));
		selenium.click("//button[@type='button']");
    }

    public void addContent(String xml) throws Exception{
	    selenium.type("rawConfig", xml);
    }
}