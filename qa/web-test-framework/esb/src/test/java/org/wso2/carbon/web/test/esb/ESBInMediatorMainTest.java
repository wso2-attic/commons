package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;


public class ESBInMediatorMainTest extends CommonSetup{

    public ESBInMediatorMainTest(String text) {
        super(text);
    }

    public void testAddInMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        esbCommon.addSequence("test_In_mediator");
        //Add a In mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("In"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the In mediator
        selenium.click("//a[@id='mediator-0']");
        Thread.sleep(2000);
        assertEquals("In Mediator", selenium.getText("//form[@id='mediator-editor-form']/table/tbody/tr[1]/td/h2"));
        assertEquals("In mediator will execute its child mediators over all incoming messages.", selenium.getText("//form[@id='mediator-editor-form']/table/tbody/tr[2]/td"));
        assertEquals("There is no In mediator specific configuration.", selenium.getText("//form[@id='mediator-editor-form']/table/tbody/tr[3]/td"));
        assertEquals("Please add child mediators to this mediator and configure those mediators.", selenium.getText("//form[@id='mediator-editor-form']/table/tbody/tr[4]/td"));

        //verify Click on the 'Help' link of the mediator
        //selenium.click("//a[@id='mediator-0']");
        //esbCommon.mediatorHelp("In");

        //verify Click on the 'Delete' icon of the 'In mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
    }

    public void testAddChildMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_In_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        //Add a Child mediator to the In mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        assertEquals("In", selenium.getText("//a[@id='mediator-0']"));
        assertEquals("Send", selenium.getText("//a[@id='mediator-0.0']"));
        //verify the mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:in xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:send /> </syn:in>", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        //verify the sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_In_mediator\"> <syn:in> <syn:send /> </syn:in> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }

    public void testAddSibling() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_In_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        //Add a Sibling to the In mediator
        esbCommon.addMediators("Add Sibling","0","Filter","Out");
        assertEquals("In", selenium.getText("//a[@id='mediator-0']"));
        assertEquals("Out", selenium.getText("//a[@id='mediator-1']"));
        //verify the mediator source
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:in xmlns:syn=\"http://ws.apache.org/ns/synapse\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");
        //verify the sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"test_In_mediator\"> <syn:in /> <syn:out /> </syn:sequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();
    }

    public void testDeleteInMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_In_mediator");
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbCommon.addMediators("Add Child","0","Core","Log");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_In_mediator");
        assertEquals("RootAddChildInLog", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));
        //Click on the 'Delete' icon of the 'In mediator'
        esbCommon.delMediator("0");
        //Save the sequence
        esbCommon.sequenceSave();
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("test_In_mediator"));

        //verify In mediator has successfully deleted from the sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_In_mediator");
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("In"));

        //delete the sequnce from sequnce list
        esbCommon.deleteSequence("test_In_mediator");
    }

    public void testUpdateSynapse() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        
        //Go to the Manage Synapse Configuration page and click on 'Update'
        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest=new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.updateSynapseConfig();

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}
