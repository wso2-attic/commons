package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.awt.event.KeyEvent;

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

public class ESBServiceUploaderTest  extends TestCase {
    Selenium selenium;

    public ESBServiceUploaderTest(Selenium _browser){
        selenium = _browser;
    }

    public void testAxis2_upload()throws Exception
    {
		selenium.click("link=Axis2 Service");
		selenium.waitForPageToLoad("30000");
        SetFileBrowse("aarFilename","/home/evanthika/WSO2/Carbon/ESB/release/2.0.2/wso2esb-2.0.2/samples/axis2Server/repository/services/SimpleStockQuoteService.aar");
		selenium.click("upload");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created Axis2 service"));
		selenium.click("//button[@type='button']");
        Thread.sleep(25000);
		selenium.click("link=List");
		selenium.waitForPageToLoad("120000");
//		assertTrue(selenium.isTextPresent("SimpleStockQuoteService.aar"));
    }

    public void SetFileBrowse(String ID,String FilePath)throws Exception
        {
           selenium.windowFocus();
           System.out.println("id="+ID);
           selenium.focus(ID);

            Thread.sleep(10000);
            char[] cArray = FilePath.toCharArray();
            for(int i =0; i < cArray.length; i++)
            {
                char cElement = cArray[i];
                String cString = String.valueOf(cElement);
                Thread.sleep(500);
               // System.out.println(cString);
                if("A".equals(cString))
                {
                    selenium.keyDownNative(""+ KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_A));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("B".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_B));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("C".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_C));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("D".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_D));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("E".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_E));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("F".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_F));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("G".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_G));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("H".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_H));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("I".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_I));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }

                if("J".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_J));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("K".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_K));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("L".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_L));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("M".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_M));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("N".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_N));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("O".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_O));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("P".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_P));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Q".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_Q));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("R".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_R));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("S".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_S));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("T".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_T));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("U".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_U));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("V".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_V));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("W".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_W));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("X".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_X));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Y".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_Y));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("Z".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+(KeyEvent.VK_Z));
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                 if("/".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_SLASH));
                }
                if("a".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_A));
                }
                if("b".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_B));
                }
                if("c".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_C));
                }
                if("d".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_D));
                }
                if("e".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_E));
                }
                if("f".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_F));
                }
                if("g".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_G));
                }
                if("h".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_H));
                }
                if("i".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_I));
                }
                if("j".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_J));
                }
                if("k".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_K));
                }
                if("l".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_L));
                }
                if("m".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_M));
                }
                if("n".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_N));
                }
                if("o".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_O));
                }
                if("p".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_P));
                 }
                if("q".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_Q));
                }
                if("r".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_R));
                 }
                if("s".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_S));
                }
                if("t".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_T));
                }
                if("u".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_U));
                }
                if("v".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_V));
                }
                if("w".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_W));
                 }
                if("x".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_X));
                }
                if("y".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_Y));
                }
                if("z".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_Z));
                }
                if(".".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_PERIOD));
                }
                if("\\".equals(cString))
                {
                    selenium.keyPressNative(""+(KeyEvent.VK_BACK_SLASH));
                }
                if(":".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+KeyEvent.VK_SEMICOLON);
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }
                if("-".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_MINUS);
                }
                if("_".equals(cString))
                {
                    selenium.keyDownNative(""+KeyEvent.VK_SHIFT);
                    selenium.keyPressNative(""+KeyEvent.VK_UNDERSCORE);
                    selenium.keyUpNative(""+KeyEvent.VK_SHIFT);
                }

                if("1".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_1);
                }
                if("2".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_2);
                }
                if("3".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_3);
                }
                if("4".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_4);
                }
                if("5".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_5);
                }
                if("6".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_6);
                }
                if("7".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_7);
                }
                if("8".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_8);
                }
                if("9".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_9);
                }
                if("0".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_0);
                }
                if(" ".equals(cString))
                {
                    selenium.keyPressNative(""+KeyEvent.VK_SPACE);
                }

            }
        }


}
