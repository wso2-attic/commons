package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBCacheMediatorMainTest  extends CommonSetup{

    public ESBCacheMediatorMainTest(String text) {
        super(text);
    }

    public void testAddCacheMediator() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        esbCommon.addSequence("test_cache_mediator");
        //Add a Cache mediator to the 'Root' level
        esbCommon.addRootLevelChildren("Add Child","Advanced","Cache");
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Cache"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Child"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Add Sibling"));
        assertTrue(selenium.getText("//*[@id='mediator-0']").contains("Delete"));

        //Click on the Cache mediator
        selenium.click("//a[@id='mediator-0']");
        assertEquals("Cache Mediator", selenium.getText("//form[@id='mediator-editor-form']/div/table/tbody/tr[1]/td/h2"));
        selenium.getSelectedValue("cacheScope").equals("Per-Host");
        selenium.getSelectedValue("cacheType").equals("Finder");
        assertEquals("org.wso2.caching.digest.DOMHASHGenerator", selenium.getValue("hashGen"));
        selenium.getSelectedValue("impType").equals("In-Memory");
        assertEquals("1000", selenium.getValue("maxSize"));
        assertEquals("on", selenium.getValue("sequenceOptionAnon"));
        assertEquals("off", selenium.getValue("sequenceOptionReference"));

        //Click on 'Add Sibling' of the Fault mediator
        esbCommon.verifyClickAddSibling();

        //Click on the 'Help' link of the mediator
        //esbCommon.mediatorHelp("Cache");

        //Select the 'Cache Type' as 'Collector'
        selenium.select("cacheType", "label=Collector");
        esbCommon.mediatorUpdate();
        esbCommon.clickMediatorSource("0");
        assertEquals("<syn:cache xmlns:syn=\"http://ws.apache.org/ns/synapse\" id=\"\" scope=\"per-host\" collector=\"true\" />", selenium.getText("mediatorSrc"));
        selenium.click("link=switch to design view");

        Thread.sleep(2000);
        selenium.select("cacheType", "label=Finder");
        esbCommon.mediatorUpdate();
        selenium.click("//a[@id='mediator-0']");
        //Check the combo box 'Cache Scope'
        assertEquals("Per-Host Per-Mediator", selenium.getText("cacheScope"));

        //Check the combo box 'Cache Type'
        assertEquals("Finder Collector", selenium.getText("cacheType"));

        //Click on the 'Delete' icon of the 'Cache mediator'
        esbCommon.delMediator("0");
        assertTrue(selenium.getText("//*[@id=\"treePane\"]").contains("Root"));
        assertTrue(!selenium.getText("//*[@id=\"treePane\"]").contains("Cache"));
    }

    public void testCacheMediatorInfomation() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();
        ESBCacheMediatorTest esbCacheMediatorTest = new ESBCacheMediatorTest(selenium);

        //Select relevant information and save the mediator and then save the sequence
        esbCommon.addSequence("test_cache");
        /*
        Parameters that can be passed are,
        String cacheId, String cacheScope, String cacheType, String cacheTimeout, String maxMsgSize, String maxSize, String onCacheHit, String resourceName,
         */
        esbCommon.addRootLevelChildren("Add Child","Advanced","Cache");
        esbCacheMediatorTest.addCacheMediatorMainInfo("0","A","Per-Host","Finder","10",null);
        esbCacheMediatorTest.addCacheImplementationInfo("In-Memory","2");
        esbCacheMediatorTest.addOnCacheHitInfo("sequenceOptionReference", "main");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();

        //verify in edit mode
        esbCommon.viewSequences();
        esbCommon.clickEditSeq("test_cache");
        //assertEquals("Root Add Child \n \n \n \n \nCache", selenium.getText("treePane"));

        //verify Manage Synapse
        selenium.click("link=Cache");
        esbCommon.clickSequenceSource("0");
        String seq_source = selenium.getText("sequence_source");
        String temp1=seq_source.substring(seq_source.indexOf("name"));
        String temp2="<syn:sequence "+temp1;
        temp2=temp2.replaceAll(" />","/>");
        esbCommon.clickSynapse();
        boolean status=esbCommon.verifyManageSynapseConfig(temp2);
        if(!status)
            throw new MyCheckedException("Sequnce information incorrect in manage synapse...!");
    }

   public void testCreateSequence() throws Exception{
       ESBCommon esbCommon=new ESBCommon(selenium);
       esbCommon.logoutLogin();

       ESBSample420Test esbSample420Test=new ESBSample420Test("");
       esbCommon.setupMainSeq();
       esbSample420Test.addSequence("cache_seq1");
       esbCommon.setSequenceToSequence("main","cache_seq1");
   }

    //Create a complete sequence with different Cache Ids for the incoming message and the outgoing message  and invoke using a client
    public void testCacheIdFunctionality() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Create a complete sequence with different Cache Ids for the incoming message and the outgoing message  and invoke using a client
        ESBSample420Test esbSample420Test=new ESBSample420Test("");
        ESBCacheMediatorTest esbCacheMediatorTest=new ESBCacheMediatorTest(selenium);
        String response1,response2;

        esbCommon.viewSequences();
        if(selenium.isTextPresent("cache_seq1")){
            esbCommon.editSeqMediator("cache_seq1","Out","Cache");
            esbCacheMediatorTest.addCacheMediatorMainInfo("1.0","B","Per-Host","Collector",null,null);
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();

            response1=esbSample420Test.invokeClient("IBM");
            response2=esbSample420Test.invokeClient("IBM");

            if(response1.equals(response2))
               throw new MyCheckedException("\nError in cache ID..!!!\nThe response  retrieved from the cache..!!!\nSequence used different Cache Ids for the incoming message and the outgoing message...!!!\nResponse should not cached...!!!");
        }
        else
            System.out.println("sequence \"cache_seq1\" not found...!");
    }

    //Create a complete sequence with the timeout set to 20000 milliseconds and invoke using a client
    public void testCacheTimeoutFunctionality() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        //Create a complete sequence with the timeout set to 20000 milliseconds and invoke using a client
        ESBSample420Test esbSample420Test=new ESBSample420Test("");
        ESBCacheMediatorTest esbCacheMediatorTest=new ESBCacheMediatorTest(selenium);
        String response1,response2;

        esbCommon.viewSequences();
        if(selenium.isTextPresent("cache_seq1")){
            esbCommon.editSeqMediator("cache_seq1","In","Cache");
            esbCacheMediatorTest.addCacheMediatorMainInfo("0.0","B","Per-Host","Finder","20",null);
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            response1=esbSample420Test.invokeClient("IBM");
            Thread.sleep(30000);
            response2=esbSample420Test.invokeClient("IBM");
            if(response1.equals(response2))
                throw new MyCheckedException("\nError in cache Timeout..!!!\nThe response  retrieved from the cache..!!!\nThe response should NOT be received from the cache after 20 seconds...!!!");

            //Once 20000 milliseconds  time is passed the response should be received from the cache again for 20 seconds
            System.out.println("\n");
            response1=esbSample420Test.invokeClient("IBM");
            response2=esbSample420Test.invokeClient("IBM");
            if(response1.equals(response2))
                System.out.println("The response retrived from the cache...!");
            else
                throw new MyCheckedException("\nError in cache Timeout..!!!\nOnce Cache Timeout  is passed the response should be received from the cache again for the duration of Cache Timeout...!.");
        }
        else
            System.out.println("sequence \"cache_seq1\" not found...!");

    }

    //Create a complete sequence with the maxSize set to 2 and invoke using a client. (Send three types of stock quotes)
    public void testCacheMaxSizeFuctionality() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

        ESBSample420Test esbSample420Test=new ESBSample420Test("");
        ESBCacheMediatorTest esbCacheMediatorTest=new ESBCacheMediatorTest(selenium);

        esbCommon.viewSequences();
        if(selenium.isTextPresent("cache_seq1")){
            esbCommon.editSeqMediator("cache_seq1","In","Cache");
            esbCacheMediatorTest.addCacheMediatorMainInfo("0.0",null,"Per-Host","Finder","100",null);
            esbCacheMediatorTest.addCacheImplementationInfo("memory","2");
            esbCommon.mediatorUpdate();
            esbCommon.sequenceSave();
            String response1,response2,response3,response4,response5,response6;

            System.out.println("\n");
            response1=esbSample420Test.invokeClient("IBM");
            response2=esbSample420Test.invokeClient("MSFT");
            response3=esbSample420Test.invokeClient("SUN");
            response4=esbSample420Test.invokeClient("IBM");
            response5=esbSample420Test.invokeClient("MSFT");
            response6=esbSample420Test.invokeClient("SUN");

            if(response1.equals(response4) && response2.equals(response5) && !response3.equals(response6))
                  System.out.println("Responses received correctly..!!!");
            else if(response3.equals(response6))
                   throw new MyCheckedException("\nError in cache maxSize...!!!");
        }
        else
            System.out.println("sequence \"cache_seq1\" not found...!");
    }

    public void verifyMaxMsgSizeFunctionality() throws Exception{
        //Create a complete sequence with the maxMessageSize set to 200 bytes  and invoke using a client
        ESBSample420Test esbSample420Test=new ESBSample420Test("");
        ESBCacheMediatorTest esbCacheMediatorTest=new ESBCacheMediatorTest(selenium);
        ESBCommon esbCommon=new ESBCommon(selenium);

        esbCommon.viewSequences();
        esbCommon.editSeqMediator("cache_seq1","In","Cache");
        esbCacheMediatorTest.addCacheMediatorMainInfo("0.0",null,"Per-Host","Finder","100","200");
        esbCommon.mediatorUpdate();
        esbCommon.sequenceSave();
        String response1,response2,response3,response4,response5,response6;
    }

    //Add a mediator, specify all values and switch to the source view and Switch back to the design view
    public void testCacheMediatorSyntax() throws Exception{
        ESBCommon esbCommon=new ESBCommon(selenium);
        esbCommon.logoutLogin();

//        <cache [id="string"] [hashGenerator="class"] [timeout="seconds"] [scope=(per-host | per-mediator)]
//             collector=(true | false) [maxMessageSize="in-bytes"]>
//           <onCacheHit [sequence="key"]>
//             (mediator)+
//           </onCacheHit>?
//           <implementation type=(memory | disk) maxSize="int"/>
//         </cache>

        esbCommon.viewSequences();
        if(selenium.isTextPresent("cache_seq1")){
            esbCommon.editSeqMediator("cache_seq1","In","Cache");

            String cache_id = selenium.getValue("cacheId");
            String cache_scope=selenium.getSelectedValue("cacheScope");
            String cache_type=selenium.getSelectedValue("cacheType");
            String hash_generator = selenium.getValue("hashGen");
            String cache_timeout = selenium.getValue("cacheTimeout");
            String maxMsgSize = selenium.getValue("maxMsgSize");
            String imp_type = selenium.getSelectedValue("impType");
            String maxSize = selenium.getValue("maxSize");
            String onCacheHit_annon = selenium.getValue("sequenceOptionAnon");
            String onCacheHitSeqRef = selenium.getValue("sequenceOptionReference");

            esbCommon.clickMediatorSource("0.0");
            //System.out.println(selenium.getText("mediatorSrc"));
            if(cache_type.equalsIgnoreCase("Finder")){
                //System.out.println("<syn:cache xmlns:syn=\"http://ws.apache.org/ns/synapse\" id=\""+cache_id+"\" scope=\""+cache_scope+"\" collector=\"false\" hashGenerator=\""+hash_generator+"\" timeout=\""+cache_timeout+"\"> <syn:implementation type=\""+imp_type+"\" maxSize=\""+maxSize+"\" /> </syn:cache>");
                assertTrue(selenium.isTextPresent("<syn:cache xmlns:syn=\"http://ws.apache.org/ns/synapse\" id=\""+cache_id+"\" scope=\""+cache_scope+"\" collector=\"false\" hashGenerator=\""+hash_generator+"\" timeout=\""+cache_timeout+"\"> <syn:implementation type=\"memory\" maxSize=\""+maxSize+"\" /> </syn:cache>"));
            }
            else if(cache_type.equalsIgnoreCase("Collector"))
                assertTrue(selenium.isTextPresent("<syn:cache xmlns:syn=\"http://ws.apache.org/ns/synapse\" id=\""+cache_id+"\" scope=\""+cache_scope+"\" collector=\"true\" hashGenerator=\""+hash_generator+"\" timeout=\""+cache_timeout+"\"> <syn:implementation type=\""+imp_type+"\" maxSize=\""+maxSize+"\" /> </syn:cache>"));

            selenium.click("link=switch to design view");
            //The information of the mediator should be displayed without the data being lost/unchanged
            assertEquals(cache_id, selenium.getValue("cacheId"));
            assertEquals(cache_scope,selenium.getSelectedValue("cacheScope"));
            assertEquals(cache_type,selenium.getSelectedValue("cacheType"));
            assertEquals(hash_generator,selenium.getValue("hashGen"));
            assertEquals(cache_timeout,selenium.getValue("cacheTimeout"));
            assertEquals(maxMsgSize, selenium.getValue("maxMsgSize"));
            assertEquals(imp_type,selenium.getSelectedValue("impType"));
            assertEquals(maxSize,selenium.getValue("maxSize"));
            assertEquals(onCacheHit_annon,selenium.getValue("sequenceOptionAnon"));
            assertEquals(onCacheHitSeqRef,selenium.getValue("sequenceOptionReference"));
        }
        else
            System.out.println("sequence \"cache_seq1\" not found...!");
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
