package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;


public class ESBDropMediatorMainTest extends CommonSetup{

    public ESBDropMediatorMainTest(String text) {
        super(text);
    }

    public void testAddDropMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //verify adding a Drop mediator to the 'Root' level
        esbCommon.addSequence("test_drop_mediator");
        esbCommon.addRootLevelChildren("Add Child","Core","Drop");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Drop"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(!selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //verify Click on the 'Delete' icon of the 'Drop mediator'
        esbCommon.delMediator("0");
        assertEquals("Root Add Child", selenium.getText("treePane"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Drop"));
    }

    //Create a complete sequence with a Drop mediator and save the sequence.
    public void testCreatesequnce() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        ESBSample1Test esbSample1Test=new ESBSample1Test("");
        esbSample1Test.createSequence("sequence_drop");

        //verify 'Mediation Sequences' page
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("sequence_drop"));
    }

    public void testSequenceInfo() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        if(selenium.isTextPresent("sequence_drop")){
            esbCommon.clickEditSeq("sequence_drop");
            assertEquals("RootAddChildFilterThenSendDropElseSend", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));

            //verify Manage Synapse
            esbCommon.checkSequenceInfoInSynapse("sequence_drop");
        }
    }


    public void testEditSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        esbCommon.viewSequences();
        if(selenium.isTextPresent("sequence_drop")){
            //Do changes to the sequence, save the sequence and view in the edit mode
            esbCommon.clickEditSeq("sequence_drop");
            //Adding a log mediator to the sequnce
            esbCommon.addMediators("Add Sibling","1","Core","Log");
            esbLogMediatorTest.addLogMediator("2","Custom");
            esbLogMediatorTest.addLogPropety("Text","Value","Message before Drop");
            esbCommon.mediatorUpdate();

            //Adding a Drop mediator to the sequnce
            esbCommon.addMediators("Add Sibling","2", "Core", "Drop");

            Thread.sleep(2000);
            //Adding a Log mediator to the sequnce
            esbCommon.addMediators("Add Sibling","2","Core","Log");
            esbLogMediatorTest.addLogMediator("4","Custom");
            esbLogMediatorTest.setPropNo();
            esbLogMediatorTest.addLogPropety("Text","Value","Message after Drop");
            esbCommon.mediatorUpdate();

            esbCommon.sequenceSave();

            esbCommon.clickEditSeq("sequence_drop");
            assertEquals("RootAddChildFilterThenSendDropElseSendLogDropLog", selenium.getText("sequenceTree").replaceAll("\n","").replaceAll(" ",""));
        }
    }

    public void testDropMediatorFunctionality() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.setupMainSeq();
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sequence_drop");
        //Invoke the client
        ESBSampleClient esbSampleClient = new ESBSampleClient();
        int log_before=esbCommon.checkLogFile("Text = Message after Drop");
        esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot(), esbCommon.getServiceAddUrl("SimpleStockQuoteService"),"IBM");
        int log_after=esbCommon.checkLogFile("Text = Message after Drop");
        if((log_after-log_before)==0)
            System.out.println("Response msg successfully droped using Drop mediator..");
        else
            throw new MyCheckedException("Drop mediator functionality does not working..");
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
