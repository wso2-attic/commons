/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;


import com.thoughtworks.selenium.Selenium;

import java.util.Properties;
import java.util.StringTokenizer;
import java.io.*;

import junit.framework.TestCase;


public class StartUpTest extends TestCase {

    Selenium browser;
    Properties property = new Properties();
    String carbonHome;

    public StartUpTest (Selenium _browser) throws IOException {
        browser = _browser;

        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        property.load(freader);

        freader.close();
        carbonHome = property.getProperty("carbon.home");

    }


    public void ErorrsInStartUp(String logName) throws Exception{
        String line = null;
        File file=new File(carbonHome + File.separator + "logs" + File.separator + logName);

        if(file.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(carbonHome + File.separator + "logs" + File.separator + logName));
                try {

                    while ((line = br.readLine()) != null) {
                        StringTokenizer tz=new StringTokenizer(line);
                        while(tz.hasMoreTokens()){
                            String next=tz.nextToken();
                            System.out.println(next);

                            if((next).matches("ERROR")){
                                System.out.println("Errors in startup");
                                assertTrue("Errors in startup",!(next).matches("ERROR"));
                            }

                            if((next).matches("FATAL")){
                                System.out.println("FATAL Errors in startup");
                                assertTrue("FATAL Errors in startup",!(next).matches("FATAL"));
                            }

                        }
                    }

                } catch (IOException ex) {
                    System.out.println(ex);
                }

            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        }else{
            System.out.println("Log file does not exist");
            assertTrue("Log file does not exist",false);
        }

    }

}
