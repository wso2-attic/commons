package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ESBOutMediatorMainTest extends CommonSetup{
    public ESBOutMediatorMainTest(String text){
        super(text);
    }

    public void testAddOutMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_out_mediator");
        //verify adding a Out mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Out"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the Out mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Mediatorswitch to source view", selenium.getText("//tr[@id='mediator-designview-header']/td"));
		assertEquals("Out Mediator", selenium.getText("//form[@id='mediator-editor-form']/table/tbody/tr[1]/td/h2"));
        assertEquals("Out Mediator Out mediator will execute its child mediators over all outgoing messages. There is no Out mediator specific configuration. Please add child mediators to this mediator and configure those mediators.", selenium.getText("//form[@id='mediator-editor-form']/table"));

//        //verify Click on the 'Help' link of the mediator
//        Thread.sleep(2000);
//        selenium.click("//a[@id='mediator-0']");
//        esbCommon.mediatorHelp("Out");

    }

    public void testAddChildMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_out_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");

        //Add a Child mediator to the Out mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        assertEquals("Out", selenium.getText("//a[@id='mediator-0']"));
		assertEquals("Send", selenium.getText("//a[@id='mediator-0.0']"));
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertTrue(selenium.isTextPresent("<syn:out xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:send /> </syn:out>"));
        selenium.click("link=switch to design view");
        //verify sequence source
        esbCommon.clickSequenceSource("0");
        assertTrue(selenium.isTextPresent("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_out_mediator\"> <syn:out> <syn:send /> </syn:out> </syn:sequence>"));
        esbCommon.clickDesignView();
        //verify design view
        assertEquals("RootAddChildOutSend", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));
    }

    public void testAddSibling() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_out_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");

        //verify adding a Sibling to the Out mediator
        esbCommon.addMediators("Add Sibling","0", "Core", "Drop");
        assertEquals("Out", selenium.getText("//a[@id='mediator-0']"));
		assertEquals("Drop", selenium.getText("//a[@id='mediator-1']"));
        //verify sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_out_mediator\"> <syn:out /> <syn:drop /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
        //verify mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:out xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
    }

    public void testDeleteOutMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_out_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
        esbCommon.addMediators("Add Sibling","0", "Core", "Drop");
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_out_mediator");
        assertEquals("RootAddChildOutDrop", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));

        //Click on the 'Delete' icon of the 'Out mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        assertTrue(esbCommon.searchSequenceInList("test_out_mediator"));

        //verify Out mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_out_mediator");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Out"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_out_mediator");
    }

    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        Thread.sleep(2000);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();

    }
}
