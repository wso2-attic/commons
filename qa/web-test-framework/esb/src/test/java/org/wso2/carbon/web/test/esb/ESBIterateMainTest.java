package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

/**
 * Created by IntelliJ IDEA.
 * User: dinusha
 * Date: Jul 28, 2009
 * Time: 12:11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ESBIterateMainTest extends CommonSetup{
      public ESBIterateMainTest(String text) {
        super(text);
    }

    public void addMediator() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addSequence("sequence_iterate");

        esbCommon.addRootLevelChildren("Add Child","Advanced","Iterate");

        ESBIterateMediatorTest esbIterateMediatorTest=new  ESBIterateMediatorTest(selenium);
        esbIterateMediatorTest.addMediator("True","True","//m0:add","//m0:add");

        ESBIterateMediatorUITest esbIterateMediatorUITest=new  ESBIterateMediatorUITest(selenium);
        esbIterateMediatorUITest.verifyIterateMediator();

        
        esbIterateMediatorTest.testAddNameSpacesToIterateExpression("url","rrrr");
        esbIterateMediatorTest.testAddNameSpacesToIterateExpression("dddddddd","ddddddd");

        esbIterateMediatorTest.addNameSpacesToAttachPath("g","h");
        esbIterateMediatorTest.addNameSpacesToAttachPath("test","test");
        esbCommon.mediatorUpdate();
        
        esbIterateMediatorTest.addTargetMediator("urn:add", "http://localhost:9000/services/SimpleStockQuoteService", "mediator.target.seq.radio.none", "epOpAnon", "main", "epr1");
        esbIterateMediatorUITest.testVerifyTargetConfiguration();

        esbCommon.sequenceSave();

        ESBManageSynapseConfigurationTest esbManageSynapseConfigurationTest = new ESBManageSynapseConfigurationTest(selenium);
        esbManageSynapseConfigurationTest.saveSynapseConfig();

        seleniumTestBase.logOutUI();
    }
}
