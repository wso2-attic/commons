/*
 *  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.balana.finder.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.TestConstants;
import org.wso2.balana.TestUtil;
import org.wso2.balana.combine.xacml2.FirstApplicablePolicyAlg;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;

/**
 * Unit test for the In-Memory Policy Finder Module implementation.
 * 
 */
public class TestInMemoryPolicyFinderModule extends TestCase {

    private final static String ROOT_DIRECTORY = "basic";
    private final static String VERSION_DIRECTORY = "3";

    private static Log log = LogFactory.getLog(TestInMemoryPolicyFinderModule.class);

    public void testBasics1() throws Exception {
        log.info("TestInMemoryPolicyFinderModule testBasics1 is Started");
        // Copied from Basic Test 0001...doesn't really matter what we test...
        String request = TestUtil.createRequest(ROOT_DIRECTORY, VERSION_DIRECTORY, "request_0001_01.xml");
        if (request != null) {
            log.info("Request that is sent to the PDP :  " + request);
            ResponseCtx response = TestUtil.evaluate(getPDPNewInstance(1), request);
            if (response != null) {
                log.info("Response that is received from the PDP :  " + response.encode());
                ResponseCtx expectedResponseCtx = TestUtil.createResponse(ROOT_DIRECTORY, VERSION_DIRECTORY, "response_0001_01.xml");
                if (expectedResponseCtx != null) {
                    assertTrue(TestUtil.isMatching(response, expectedResponseCtx));
                } else {
                    assertTrue("Response read from file is Null", false);
                }
            } else {
                assertFalse("Response received PDP is Null", false);
            }
        } else {
            assertTrue("Request read from file is Null", false);
        }

        log.info("TestInMemoryPolicyFinderModule testBasics1 is finished");
    }


    public void testBasics2() throws Exception {
        log.info("TestInMemoryPolicyFinderModule testBasics2 is Started");
        // Copied from Basic Test 0001...doesn't really matter what we test...
        String request = TestUtil.createRequest(ROOT_DIRECTORY, VERSION_DIRECTORY, "request_0001_01.xml");
        if (request != null) {
            log.info("Request that is sent to the PDP :  " + request);
            ResponseCtx response = TestUtil.evaluate(getPDPNewInstance(2), request);
            if (response != null) {
                log.info("Response that is received from the PDP :  " + response.encode());
                ResponseCtx expectedResponseCtx = TestUtil.createResponse(ROOT_DIRECTORY, VERSION_DIRECTORY, "response_0001_01.xml");
                if (expectedResponseCtx != null) {
                    assertTrue(TestUtil.isMatching(response, expectedResponseCtx));
                } else {
                    assertTrue("Response read from file is Null", false);
                }
            } else {
                assertFalse("Response received PDP is Null", false);
            }
        } else {
            assertTrue("Request read from file is Null", false);
        }

        log.info("TestInMemoryPolicyFinderModule.testBasics2 is finished");
    }
    

    private static PDP getPDPNewInstance(int numberOfPolicies) throws Exception {

        PolicyFinder finder = new PolicyFinder();
        List<Document> testDocuments = loadTestDocuments(numberOfPolicies);
        InMemoryPolicyFinderModule testPolicyFinderModule = new InMemoryPolicyFinderModule(testDocuments, new FirstApplicablePolicyAlg());
        Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        policyModules.add(testPolicyFinderModule);
        finder.setModules(policyModules);

        Balana balana = Balana.getInstance();
        PDPConfig pdpConfig = balana.getPdpConfig();
        pdpConfig = new PDPConfig(pdpConfig.getAttributeFinder(), finder, pdpConfig.getResourceFinder(), true);
        return new PDP(pdpConfig);
    }

    private static List<Document> loadTestDocuments(int lastPolicyId) throws Exception {

        List<Document> ret = new ArrayList<Document>();
        for(int i = 1; i < lastPolicyId +1 ; i++){
            ret.add(loadTestDocument(i));
        }
        return ret;
    }
    
    private static Document loadTestDocument(int policyId) throws Exception {

        // assume policy id must be less than 10

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder db = factory.newDocumentBuilder();
        String policyPath = (new File(".")).getCanonicalPath() + File.separator + TestConstants.RESOURCE_PATH +
                File.separator + ROOT_DIRECTORY + File.separator + VERSION_DIRECTORY + File.separator
                + TestConstants.POLICY_DIRECTORY + File.separator + "TestPolicy_000" + policyId + ".xml";
        File policy = new File(policyPath);
        assertTrue(policy.exists());
        return  db.parse(policy);
    }

}
