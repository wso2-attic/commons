package org.wso2.carbon.web.test.mashup;

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


public class MSCustomUI extends CommonSetup {

    public MSCustomUI(String text) {
            super(text);
    }

    //Access "Custom UI of this Mashup" link.
    public static void accessCustomUIOfServices(String jsservice) throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int k=1;
        boolean next=selenium.isTextPresent("next >");
        while(next==true){
               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               k=k+1;
        }

        int i;
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        for(i=1;i<=k;i++){
            if(selenium.isElementPresent("link="+jsservice)==true){
                i=k+1;
                selenium.click("link="+jsservice);
		        selenium.waitForPageToLoad("30000");
		        selenium.click("link="+jsservice);
                selenium.waitForPageToLoad("30000");
                selenium.click("link=Custom UI of this Mashup");
                String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                selenium.selectWindow(tryitwinid);
                Thread.sleep(1000);
            }
            else{
                int temp=i+1;
                if(temp<=k)
                    selenium.click("link="+temp);
            }
        }
     }
    
//..............................................................................................................................................

    //Test Custom UI functionality of digit2image service.
    public static void customUI_Functionality_Of_digit2image() throws Exception{
            String label[]={"label=small (75x75 square)","label=thumbnail (100 on longest side)","label=medium (240 on longest side)","label=normal (500 on longest side)","label=big (1024 on longest side)","label=original image"};

            int j;
            for(j=0;j<6;j++){
                selenium.select("imageSize",label[j]);
		        selenium.click("//input[@value='Refresh']");
                Thread.sleep(10000);

                int i=1;
                while(i<=9){
                    assertTrue(selenium.isElementPresent("digit"+i));
                    String src = selenium.getAttribute("id=digit"+i+"@src");
                    if(src.equals(null))
                        System.out.println("digit"+i+" image is not loaded......");
                    else
                        System.out.println("digit"+i+" image is loaded......");

                    i=i+1;
                }
            }
    }

//..................................................................................................................................................


    //Test Custom UI functionality of formulaFlicks service.
    public static void customUI_Functionality_Of_formulaFlicks() throws Exception{
        boolean error = selenium.isTextPresent(selenium.getText("error"));

        if(error==false) {
           assertEquals("Formula Flicks", selenium.getText("pageTitle"));
		   assertEquals("Retrieving race details and videos...", selenium.getText("heading"));

           System.out.println("No errors in Custom UI of formulaFlicks service.................Then Test is passed");
        }
        else
           System.out.println("Found the error in Custom UI of formulaFlicks service..............Then Test is failed");
    }

//.....................................................................................................................................................

    //Test Custom UI functionality of sudoku service.
    public static void customUI_Functionality_Of_sudoku() throws Exception{
        assertTrue(selenium.isTextPresent("WSudOku2"));

        String puzzle=null;
        int i_value=0,j_value=0;
        for(int i=1;i<10;i++){
            for(int j=1;j<10;j++){
                String temp=selenium.getValue("c"+i+j);
                if(!temp.equals("")){
                    puzzle=temp;
                    i_value=i;
                    j_value=j;
                    j=10;
                    i=10;
                }
            }
        }
        System.out.println("Puzzle vaue of c"+i_value+j_value+" : "+puzzle);
        selenium.click("newPuzzle");
        String newPuzzle=selenium.getValue("c"+i_value+j_value);
        System.out.println("New puzzle vaue of c"+i_value+j_value +" : " + newPuzzle);
    }

//........................................................................................................................................................

    //Test Custom UI functionality of tomatoTube service.
    public static void customUI_Functionality_Of_tomatoTube() throws Exception{
        assertTrue(selenium.isTextPresent("Welcome to TomatoTube"));
		assertTrue(selenium.isElementPresent("//div[@id='intro']/div[1]/img"));

		selenium.select("mode", "label=Get Top 10 in Theaters");
		selenium.click("//input[@value='Get data']");
        Thread.sleep(15000);

        int i=1;
        boolean theaters = true;
        while(i<=10){
             theaters=selenium.isTextPresent(selenium.getText("//div[@id='content']/div["+i+"]/div/h2"));
             i=i+1;
        }

        if(theaters==true)
            System.out.println("No errors in Custom UI of tomatoTube service.................Then Test is passed");
        else
            System.out.println("Found the error in Custom UI of tomatoTube service..............Then Test is failed");

		selenium.select("mode", "label=Get Top 10 DVD releases");
		selenium.click("//input[@value='Get data']");
        Thread.sleep(15000);

        int j=1;
        theaters = true;
        while(j<=10){
             theaters =selenium.isTextPresent(selenium.getText("//div[@id='content']/div["+j+"]/div/h2"));
             j=j+1;
        }

        if(theaters==true)
            System.out.println("No errors in Custom UI of tomatoTube service.................Then Test is passed");
        else
            System.out.println("Found the error in Custom UI of tomatoTube service..............Then Test is failed");
    }

//............................................................................................................................................................

    //Test Custom UI functionality of TwitterMap service.
    public static void customUI_Functionality_Of_TwitterMap() throws Exception{
      //Have to give localhost instead of host ip........need to check
    }

//.............................................................................................................................................................

    //Test Custom UI functionality of upgradeChecker service.
    public static void customUI_Functionality_Of_upgradeChecker() throws Exception{
            Thread.sleep(5000);
            String src = selenium.getAttribute("//img[@alt='WSO2 Mashup Server']@src");
            if(src.equals("images/mashup_logo.gif"))
                System.out.println("Mashup logo is available......");
            else
                System.out.println("Mashup logo is not available.....");

            assertEquals("Your release is current.", selenium.getText("//div[@id='norecommendation']/h1"));
            assertTrue(selenium.isElementPresent("//img[@alt='WSO2 Mashup Server']"));
            assertEquals("No new releases or nightly builds are available.", selenium.getText("//div[@id='norecommendation']/div"));
    }

//.................................................................................................................................................................

    //Test Custom UI functionality of version service.
    public static void customUI_Functionality_Of_version() throws Exception{
            Thread.sleep(5000);
            String src = selenium.getAttribute("//img[@alt='WSO2 Mashup Server logo']@src");
            
            if(src.equals("images/mashup_logo.gif"))
                System.out.println("Mashup logo is available......");
            else
                System.out.println("Mashup logo is not available.....");
        
            assertEquals("This release is version:", selenium.getText("//div[1]"));
            assertTrue(selenium.isTextPresent(selenium.getText("number")));
            assertEquals("This version was built on:", selenium.getText("//div[3]"));
            assertTrue(selenium.isTextPresent(selenium.getText("date")));
    }

//.....................................................................................................................................................................

    //Test Custom UI functionality of yahooGeoCode service.
    public static void customUI_Functionality_Of_yahooGeoCode() throws Exception{

    }

}

