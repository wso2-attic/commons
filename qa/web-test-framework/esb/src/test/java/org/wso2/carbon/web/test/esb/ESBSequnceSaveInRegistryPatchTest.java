package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.client.ESBSampleClient;


public class ESBSequnceSaveInRegistryPatchTest extends CommonSetup{

    public ESBSequnceSaveInRegistryPatchTest(String text) {
        super(text);
    }

    public void createSequence(String seqName,String regPath) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        esbCommon.addSequence(seqName);
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbCommon.addMediators("Add Child","0","Core","Log");
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");

        //save as sequence
        esbCommon.sequenceSaveAs(regPath);
    }

    //This method is used to saveas the sequence and verify in registry and sequence list
    public void testSaveAsSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        createSequence("testSeq1","/esbSequnces/testSeq1");

        //verify in sequence list
        esbCommon.viewSequences();
        assertTrue(selenium.isTextPresent("/esbSequnces/testSeq1"));

        //verify in Registry
        boolean addEprName = esbCommon.searchResources("/esbSequnces","testSeq1");
        if (!addEprName){
            throw new MyCheckedException("Endpoint  \"testSeq1\" not available in registry path..!!");
        }
    }

    //Edit the sequnce through the sequence list & verify that the changes has applied in both registry and sequnce list
    public void testEditSequnceFromSequnceList() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        esbCommon.editSeqMediator("/esbSequnces/testSeq1","Out",null);
        esbCommon.addMediators("Add Child","1","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //verify changes done to sequence
        esbCommon.clickEditDynamicSequence("/esbSequnces/testSeq1");

        //saveas button not available in edit mode
        assertTrue(selenium.isElementPresent("//input[@value='Cancel']"));
        assertTrue(selenium.isElementPresent("applyButton"));
        assertTrue(selenium.isElementPresent("saveButton"));
        assertTrue(!selenium.isElementPresent("saveAsButton"));

        //verify the sequence source
        esbCommon.clickSequenceSource("0");
        assertEquals("<syn:nullSequence xmlns:syn=\"http://ws.apache.org/ns/synapse\"> <syn:in> <syn:log /> </syn:in> <syn:out> <syn:send /> </syn:out> </syn:nullSequence>", selenium.getText("sequence_source"));
        esbCommon.clickDesignView();

        //verify in registry
        if(esbCommon.searchResources("/esbSequnces","testSeq1")){
            selenium.click("link=testSeq1");
            Thread.sleep(5000);
            selenium.click("link=Edit as text");
            assertEquals("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"dynamicSeq\"><syn:in><syn:log /></syn:in><syn:out><syn:send /></syn:out></syn:sequence>", selenium.getText("editTextContentID"));
            selenium.click("cancelContentButtonID");
        }
    }

    //Edit sequnce through the registry
    public void testEditSequnceFromRegistry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.searchResources("/esbSequnces","testSeq1");
        Thread.sleep(2000);
        selenium.click("link=testSeq1");
        Thread.sleep(6000);
        selenium.click("link=Edit as text");
        selenium.type("editTextContentID", "<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"dynamicSeq\"><syn:in><syn:log /></syn:in><syn:out></syn:out></syn:sequence>");
        selenium.click("saveContentButtonID");

        //verify chances in sequnce tree
        esbCommon.viewSequences();
        esbCommon.clickEditDynamicSequence("/esbSequnces/testSeq1");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("In"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Log"));
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Out"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Send"));
    }

    //detele sequnce through list
    public void testDeleteSequnceFromList() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.viewSequences();
        esbCommon.deleteSequence("/esbSequnces/testSeq1");

        //verify detele in list
        esbCommon.viewSequences();
        assertTrue(!selenium.isTextPresent("/esbSequnces/testSeq1"));

        //verify detele in registry
        assertTrue(!esbCommon.searchResources("/esbSequnces","testSeq1"));
    }

    //delete sequence through registry
    public void testDeteleSequnceFromRegistry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        createSequence("testSeq2","/esbSequnces/testSeq2");

        ESBGenerarlSequenceScenariosTest esbGenerarlSequenceScenariosTest=new ESBGenerarlSequenceScenariosTest("");
        esbGenerarlSequenceScenariosTest.clickActionDelete("/esbSequnces/testSeq2","testSeq2");

        //verify delete in registry
        assertTrue(!esbCommon.searchResources("/esbSequnces","testSeq2"));

        //verify detele in list
        esbCommon.viewSequences();
        assertTrue(!selenium.isTextPresent("/esbSequnces/testSeq2"));
    }

    public void testCreateInSequence() throws Exception{
         createInSequence("In_seq","/esbSequnces/In_seq");
    }

    public void testCreateOutSequence() throws Exception{
         createOutSequnce("Out_seq","/esbSequnces/Out_seq");
    }

    public void testCreateProxy() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbAddProxyServiceTest.addProxyName("test_proxy_", "Custom Proxy");
        //esbAddProxyServiceTest.transportSettings("http", "https", null, null, null);
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbAddProxyServiceTest.selectInSequnceFromEmbeddedRegistry("/esbSequnces/In_seq");
        esbAddProxyServiceTest.clickNext();

        //*****setting up of the Out Sequence of the Proxy Service*****//
        esbAddProxyServiceTest.selectOutSequnceFromEmbeddedRegistry("/esbSequnces/Out_seq");

        //****saving the proxy Service*****//
        esbAddProxyServiceTest.saveProxyService();
        Thread.sleep(5000);

    }

    public void testInvokeClient() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

        boolean stockQuoteResponse = esbSampleClient.stockQuoteClient("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/"+esbCommon.getContextRoot()+"/", esbCommon.getServiceAddUrl("SimpleStockQuoteService"), "IBM");
        if (stockQuoteResponse){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();

    }

    //create proxy and invoke
    public void createInSequence(String seqName,String regPath) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //****setting up of the In Sequence of the Proxy Service*****//
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        esbCommon.addSequence(seqName);
        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");

        //Adding a send mediator
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.0");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Drop mediator
        esbCommon.addMediators("Add Sibling","0.0","Core","Drop");
        //save as sequence
        esbCommon.sequenceSaveAs(regPath);
    }

    public void createOutSequnce(String seqName,String regPath) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //*****setting up of the Out Sequence of the Proxy Service*****//
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        esbCommon.addSequence(seqName);
        //Adding the send mediator
        esbCommon.addRootLevelChildren("Add Child","Core","Send");
        esbCommon.mediatorUpdate();
        //save as sequence
        esbCommon.sequenceSaveAs(regPath);
    }
}
