package org.wso2.carbon.web.test.esb;

public class ESBSample400Test extends CommonSetup{
    public  ESBSample400Test(String text){
        super(text);
    }

//    <proxy name="SplitAggregateProxy">
//        <target>
//            <inSequence>
//                <iterate expression="//m0:getQuote/m0:request" preservePayload="true"
//                         attachPath="//m0:getQuote"
//                         xmlns:m0="http://services.samples">
//                    <target>
//                        <sequence>
//                            <send>
//                                <endpoint>
//                                    <address
//                                        uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//                                </endpoint>
//                            </send>
//                        </sequence>
//                    </target>
//                </iterate>
//            </inSequence>
//            <outSequence>
//                <aggregate>
//                    <onComplete expression="//m0:getQuoteResponse"
//                                xmlns:m0="http://services.samples">
//                        <send/>
//                    </onComplete>
//                </aggregate>
//            </outSequence>
//        </target>
//    </proxy>


    public void testCreateProxyService() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.logoutLogin();

        createProxyService("sample400_proxy");
    }
    public void createProxyService(String proxyName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBAddProxyServiceTest esbAddProxyServiceTest = new ESBAddProxyServiceTest(selenium);
        ESBIterateMediatorTest esbIterateMediatorTest=new ESBIterateMediatorTest(selenium);
        ESBAggregateMediatorTest esbAggregateMediatorTest=new ESBAggregateMediatorTest(selenium);

        addSequence();

        selenium.click("link=Proxy Service");
		selenium.waitForPageToLoad("30000");
        esbAddProxyServiceTest.addProxyName(proxyName, "Custom Proxy");
        //set Proxy Service Transport settings
        esbAddProxyServiceTest.setTransport("http");
        esbAddProxyServiceTest.setTransport("https");
        esbAddProxyServiceTest.clickNext();

        //Create InSequence
        esbAddProxyServiceTest.selectInSequence("inSeqOpAnon", null);
        //Adding the Iterate mediator
        esbCommon.addRootLevelChildren("Add Child","Advanced","Iterate");
        esbIterateMediatorTest.addMediator("False","True","//m0:getQuote/m0:request","//m0:getQuote");
        esbIterateMediatorTest.addNameSpacesToAttachPath("m0","http://services.samples");
        esbCommon.mediatorUpdate();

        //Adding the target mediator infomation
        selenium.click("link=Target");
        //Because of the bug CARBON-5457 sequence can not be define as anonymous.So it define as pick from registry.
        Thread.sleep(2000);
		selenium.click("mediator.target.seq.radio.reg");
        Thread.sleep(2000);
        selenium.click("mediator.target.seq.reg.link");
        esbCommon.selectResource("Sequence","target_sequence");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //create out sequence
        selenium.click("nextBtn");        
        esbAddProxyServiceTest.selectOutSequence("outSeqOpAnon", null);
        esbCommon.addRootLevelChildren("Add Child","Advanced","Aggregate");
        Thread.sleep(2000);
        esbAggregateMediatorTest.addAggregateExpression("//m0:getQuoteResponse");
        esbAggregateMediatorTest.addAggregateExpressionNameSpaces("m0","http://services.samples");
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Child","0","Core","Send");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        Thread.sleep(2000);
        selenium.click("saveBtn");
        selenium.waitForPageToLoad("50000");
    }


    public void addSequence() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSendMediatorTest esbSendMediatorTest=new ESBSendMediatorTest(selenium);
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);

        esbCommon.viewSequences();
        if(!selenium.isTextPresent("target_sequence")){
            esbCommon.addSequence("target_sequence");
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
            esbSendMediatorTest.addAnonSendMediator("0");
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,"http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.saveAddressEndpoint();

            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            
        }
    }

}
