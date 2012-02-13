package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;

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

public class ESBFaultMediatorMainTest extends CommonSetup {

    public ESBFaultMediatorMainTest(String text) {
        super(text);
    }


    public void testAddFaultMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_fault_mediator");
        //Add a Fault mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Transform","Fault");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Fault"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Fault mediator
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertEquals("on", selenium.getValue("soap_version"));
        assertEquals("off", selenium.getValue("//input[@id='soap_version' and @name='soap_version' and @value='2']"));
        selenium.getSelectedValue("fault_code1").equals("versionMismatch");
        assertEquals("on", selenium.getValue("fault_string"));
        assertEquals("off", selenium.getValue("//input[@name='fault_string' and @value='expression']"));
        assertTrue(selenium.isElementPresent("name_space"));
        assertTrue(selenium.isElementPresent("fault_actor"));
        assertTrue(selenium.isElementPresent("detail"));

        //Click on update mediator without specifying Fault String
        selenium.click("//input[@value='Update']");
        assertEquals("Fault String is empty.Required fields can not left blank", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //Click on 'Add Sibling' of the Fault mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Fault");

        //Select the radio option 'SOAP1.2'
        selenium.click("//input[@id='soap_version' and @name='soap_version' and @value='2']");
        selenium.getSelectedValue("fault_code1").equals("versionMismatch");
        assertTrue(selenium.isTextPresent("exact:Reason*"));
        assertTrue(selenium.isTextPresent("Role"));
        assertTrue(selenium.isTextPresent("Node"));
        assertTrue(selenium.isTextPresent("Detail"));

        //Click on update mediator without specifying Reason field
        selenium.click("//input[@value='Update']");
        assertEquals("Fault String is empty.Required fields can not left blank", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");

        //Click on the 'Delete' icon of the 'fault mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Fault"));
    }

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'Server'
//         - the 'Fault String' set to 'Expression'
//        and invoke through a client (Specify the Fault Actor and Detail as well)
    /*
    Sample5
     */

    public void testCreateSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample5Test esbSample5Test=new ESBSample5Test("");
        esbSample5Test.addMyFaultHandler();
        esbSample5Test.addSequence("Fault_seq1");
    }

    public void testEditSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'Client'
//         - the 'Fault String' set to 'Expression'
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","Client");
            esbFaultMediatorTest.setFaultCodeExpression("get-property('ERROR_MESSAGE')");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor","detail");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence2() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'mustUnderstand'
//         - the 'Fault String' set to 'Expression'
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");
        System.out.println("\n\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","mustUnderstand");
            esbFaultMediatorTest.setFaultCodeExpression("get-property('ERROR_MESSAGE')");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor","detail");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence3() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'versionMismatch'
//         - the 'Fault String' set to 'Expression'
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","versionMismatch");
            esbFaultMediatorTest.setFaultCodeExpression("get-property('ERROR_MESSAGE')");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor","detail");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence4() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'Server'
//         - the 'Fault String' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","Server");
            esbFaultMediatorTest.setFaultCodeString("This is the custom error message");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor","detail");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    //**************************************************************************************************
        /*
    This method is used to check a customer issue...
    replace "detail" with a more complex structure  like:
    <ns1:IdeaCustomException xmlns:ns1="http://www.example.org/SimpleService/">
               <errorCode>GENEX001</errorCode>
               <uniqueIdentifier>foobar123456fsck</uniqueIdentifier>
               <timestamp>2009-05-08T10:54:21.827-06:00</timestamp>
            </ns1:IdeaCustomException>
     */

    public void testEditSequence18() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","Server");
            esbFaultMediatorTest.setFaultCodeString("IdeaCustomException");
            esbFaultMediatorTest.setSoap11FaultGeneralInfo("actor", "<ns1:IdeaCustomException xmlns:ns1=\"http://www.example.org/SimpleService/\">\n" +
                    "               <errorCode>GENEX001</errorCode>\n" +
                    "               <uniqueIdentifier>foobar123456fsck</uniqueIdentifier>\n" +
                    "               <timestamp>2009-05-08T10:54:21.827-06:00</timestamp>\n" +
                    "            </ns1:IdeaCustomException> ");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }
    //**************************************************************************************************

    public void testEditSequence5() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'Client'
//         - the 'Fault String' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","Client");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence6() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'mustUnderstand'
//         - the 'Fault String' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","mustUnderstand");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence7() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.1'
//         - the 'Fault Code' set to 'versionMismatch'
//         - the 'Fault String' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Fault Actor and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap11Fault("0","versionMismatch");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("SUN");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }


    public void testEditSequence8() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'versionMismatch'
//         - the 'Reason' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","versionMismatch");
            esbFaultMediatorTest.setFaultCodeString("This is the custom error message");
            esbFaultMediatorTest.setSoap12FaultGeneralInfo("actor","detail","node");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence9() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'mustUnderstand'
//         - the 'Reason' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","mustUnderstand");
            esbFaultMediatorTest.setFaultCodeString("This is the custom error message");
            esbFaultMediatorTest.setSoap12FaultGeneralInfo("actor","detail","node");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence10() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'dataEncodingUnknown'
//         - the 'Reason' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","dataEncodingUnknown");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence11() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'Sender'
//         - the 'Reason' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","Sender");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence12() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'Receiver'
//         - the 'Reason' set to 'Value' and specify a custom message (E.g.:-This is the custom error message)
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","Receiver");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence13() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'versionMismatch'
//         - the 'Reason' set to 'Expression' and specify a custom message
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","versionMismatch");
            esbFaultMediatorTest.setFaultCodeExpression("get-property('ERROR_MESSAGE')");
            esbFaultMediatorTest.setSoap12FaultGeneralInfo("actor","detail","node");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");

    }

    public void testEditSequence14() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'mustUnderstand'
//         - the 'Reason' set to 'Expression' and specify a custom message
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","mustUnderstand");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence15() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'dataEncodingUnknown'
//         - the 'Reason' set to 'Expression' and specify a custom message
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","dataEncodingUnknown");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence16() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'Sender'
//         - the 'Reason' set to 'Expression' and specify a custom message
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","Sender");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testEditSequence17() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        Create a complete sequence with a Fault mediator with
//         - the 'Version' set to 'SOAP1.2'
//         - the 'Code' set to 'Receiver'
//         - the 'Reason' set to 'Expression' and specify a custom message
//        and invoke through a client (Specify the Role, Node and Detail as well)
        ESBFaultMediatorTest esbFaultMediatorTest=new ESBFaultMediatorTest(selenium);
        ESBSample5Test esbSample5Test=new ESBSample5Test("");

        System.out.println("\n");
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler") && selenium.isTextPresent("Fault_seq1")){
            esbCommon.editSeqMediator("myFaultHandler","Fault",null);
            esbFaultMediatorTest.setSoap12Fault("0","Receiver");

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            esbCommon.setSequenceToSequence("main","Fault_seq1");
            //Invoke Client
            esbSample5Test.invokeClient("MSFT");
        }
        else
            throw new MyCheckedException("Sequences not found...!");

    }


    public void testSequenceInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Save the sequence and view options in the edit mode
        esbCommon.viewSequences();
        if(selenium.isTextPresent("myFaultHandler")){
            esbCommon.clickEditSeq("myFaultHandler");
            //assertEquals("Root Add Child \n \n \n \n \nFault\n\n \nSend", selenium.getText("treePane"));

            //Add a mediator, specify all values and switch to the source view
            esbCommon.clickMediatorSource("0");
            assertTrue(selenium.isTextPresent("<syn:makefault xmlns:syn=\"http://ws.apache.org/ns/synapse\" version=\"soap12\"> <syn:code xmlns:soap12Env=\"http://www.w3.org/2003/05/soap-envelope\" value=\"soap12Env:Receiver\" /> <syn:reason expression=\"get-property('ERROR_MESSAGE')\" /> <syn:node>detail</syn:node> <syn:role>actor</syn:role> <syn:detail>node</syn:detail> </syn:makefault>"));

//            esbCommon.clickSequenceSource("0");
//            String seq_source = selenium.getText("sequence_source");
//            String temp1=seq_source.substring(seq_source.indexOf("name"));
//            String temp2="<syn:sequence "+temp1;
//            temp2=temp2.replaceAll(" />","/>");
//            esbCommon.clickSynapse();
//            boolean status=esbCommon.verifyManageSynapseConfig(temp2);
//            if(!status)
//                throw new MyCheckedException("Sequence information incorrect..!");
        }
        else
            throw new MyCheckedException("Sequences not found...!");
    }

    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
