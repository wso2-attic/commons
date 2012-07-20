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

package org.wso2.balana.basic;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.balana.*;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 *  This XACML 3.0 basic test.
 */
public class BasicTestV3 extends TestCase {

    /**
     * Configuration store
     */
    private static ConfigurationStore store;

    /**
     * directory name that states the test type
     */
    private final static String ROOT_DIRECTORY  = "basic";

    /**
     * directory name that states XACML version
     */
    private final static String VERSION_DIRECTORY  = "3";

    /**
     * the logger we'll use for all messages
     */
	private static Log log = LogFactory.getLog(BasicTestV3.class);

    @Override
    public void setUp() throws Exception {

        String configFile = (new File(".")).getCanonicalPath() + File.separator + TestConstants.CONFIG_FILE;
        store = new ConfigurationStore(new File(configFile));
    }

    public void testBasicTest0001() throws Exception {

        String reqResNo;
        Set<String> policies = new HashSet<String>();
        policies.add("TestPolicy_0001.xml");
        PDP pdp = getPDPNewInstance(policies);
        log.info("Basic Test 0001 is started");

        for(int i = 1; i < 2 ; i++){
            
            if(i < 10){
                reqResNo = "0" + i;
            } else {
                reqResNo = Integer.toString(i);
            }

            String request = TestUtil.createRequest(ROOT_DIRECTORY, VERSION_DIRECTORY,
                                                        "request_0001_" + reqResNo + ".xml");
            if(request != null){
                log.info("Request that is sent to the PDP :  " + request);
                String response = pdp.evaluate(request);
                if(response != null){
                    log.info("Response that is received from the PDP :  " + response);
                    ResponseCtx expectedResponseCtx = TestUtil.createResponse(ROOT_DIRECTORY,
                                    VERSION_DIRECTORY, "response_0001_" + reqResNo + ".xml");
                    if(expectedResponseCtx != null){
                        assertTrue(TestUtil.isMatching(response, expectedResponseCtx.getEncoded()));
                    } else {
                        assertTrue("Response read from file is Null",false);    
                    }
                } else {
                    assertFalse("Response received PDP is Null",false);
                }
            } else {
                assertTrue("Request read from file is Null", false);
            }

            log.info("Basic Test 0001 is finished");
        }
    }


    /**
     * Returns a new PDP instance with new XACML policies
     *
     * @param policies  Set of XACML policy file names
     * @return a  PDP instance
     */
    private static PDP getPDPNewInstance(Set<String> policies){

        PolicyFinder finder= new PolicyFinder(store);

        TestPolicyFinderModule testPolicyFinderModule = new TestPolicyFinderModule(ROOT_DIRECTORY,
                                                                   VERSION_DIRECTORY, policies);
        Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        policyModules.add(testPolicyFinderModule);
        finder.setModules(policyModules);

        PDPConfig pdpConfig;
        try {
            pdpConfig = store.getDefaultPDPConfig();
        } catch (UnknownIdentifierException e) {
            // just ignore and load default config
            log.warn("Load default PDP configurations");
            pdpConfig = new PDPConfig(null, null, null, false);
        }
        return new PDP(new PDPConfig(pdpConfig.getAttributeFinder(), finder,
                                                        pdpConfig.getResourceFinder(), false));

    }

}
