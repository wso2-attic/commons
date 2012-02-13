package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.client.ESBSampleClient;


public class ESBSample420Test extends CommonSetup{
    public ESBSample420Test(String text) {
        super(text);
    }

//    <in>
//            <cache timeout="20" scope="per-host" collector="false"
//                   hashGenerator="org.wso2.caching.digest.DOMHASHGenerator">
//                <implementation type="memory" maxSize="100"/>
//            </cache>
//            <send>
//                <endpoint>
//                    <address uri="http://localhost:9000/services/SimpleStockQuoteService"/>
//                </endpoint>
//            </send>
//        </in>
//        <out>
//            <cache collector="true"/>
//            <send/>
//        </out>

    public void addSequence(String seqName) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSequenceTreePopulatorTest esbSequenceTreePopulatorTest = new ESBSequenceTreePopulatorTest(selenium);
        ESBCacheMediatorTest esbCacheMediatorTest=new ESBCacheMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);

        esbCommon.addSequence(seqName);

        //Adding the In mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
        esbSequenceTreePopulatorTest.clickMediator("0");

        //Adding the Cache mediator
        esbCommon.addMediators("Add Child","0","Advanced","Cache");
        esbCacheMediatorTest.addCacheMediatorMainInfo("0.0","A","Per-Host","Finder","20",null);
        esbCacheMediatorTest.addCacheImplementationInfo("memory","100");
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","0.0","Core","Send");
        esbSendMediatorTest.addAnonSendMediator("0.1");
        esbSendMediatorTest.addMandInfoSendMediator(esbCommon.getServiceAddUrl("SimpleStockQuoteService"));
        esbSendMediatorTest.saveEndpoint();
        esbCommon.mediatorUpdate();

        //Adding the Out mediator
        esbCommon.addMediators("Add Sibling","0","Filter","Out");

        //Adding the cache mediator
        esbCommon.addMediators("Add Child","1","Advanced","Cache");
        esbCacheMediatorTest.addCacheMediatorMainInfo("1.0","A","Per-Host","Collector",null,null);
        esbCommon.mediatorUpdate();

        //Adding the Send mediator
        esbCommon.addMediators("Add Sibling","1.0","Core","Send");
        esbCommon.mediatorUpdate();

        esbCommon.sequenceSave();
    }

    public String invokeClient(String symbol) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSampleClient esbSampleClient = new ESBSampleClient();

        String stockQuoteResponse = esbSampleClient.stockQuoteClientforProxy("http://"+esbCommon.getHostName()+":"+esbCommon.getNioHttpPort()+"/", null,symbol).toString();
        boolean output=false;
        output = stockQuoteResponse.contains(symbol);
        if (output){
            System.out.println("The response received!!!!");
        }else{
            throw new MyCheckedException("Client Failed!!!!");
        }
        Thread.sleep(5000);
       esbCommon.closeFiles();

       return stockQuoteResponse;
   }
    
    public void testAll() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

         boolean login = selenium.isTextPresent("Sign-out");

         if (login){
             seleniumTestBase.logOutUI();
         }

        seleniumTestBase.loginToUI("admin","admin");

        addSequence("sample_420");
        //Setting the created sequence to the main sequence
        esbCommon.setSequenceToSequence("main","sample_420");
        seleniumTestBase.logOutUI();

        //Create a complete sequence with the incoming message and the outgoing message both having the same Cache ID and invoke using a client
        String res1=invokeClient("IBM");
        String res2=invokeClient("IBM");
        
        if(res1.equals(res2))
             System.out.println("The response has retrieved from the cache..");
        else
             throw new MyCheckedException("The response not retrieved from the cache the second time it is invoked..!!!");
        
    }
}
