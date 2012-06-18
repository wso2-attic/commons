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
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class BasicTest0001 extends TestCase {


    private PDP pdp;

    /**
     * the logger we'll use for all messages
     */
	private static Log log = LogFactory.getLog(BasicTest0001.class);

    @Override
    public void setUp() throws Exception {

        //init PDP with 0001 policy . This is basic policy to test the balana engine
        Set<String> policyPath = new HashSet<String>();
        policyPath.add("TestPolicy_0001.xml");
        PolicyFinder finder = new PolicyFinder();
        TestPolicyFinderModule testPolicyFinderModule = new TestPolicyFinderModule(policyPath);
        Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        policyModules.add(testPolicyFinderModule);
        finder.setModules(policyModules);

        PDPConfig pdpConfig = new PDPConfig(null, finder, null, false);
        pdp = new PDP(pdpConfig);
    }

    public void testBasicTest0001() throws Exception {

        log.info("Basic Balana Test 0001 is started");

        AbstractRequestCtx requestCtx = TestUtil.createRequest("request_0001_01.xml");
        if(requestCtx != null){
            ResponseCtx responseCtx = pdp.evaluate(requestCtx);
            if(responseCtx != null){

                 assertTrue(TestUtil.isCorrect(responseCtx, "response_0001_01.xml"));
                assertTrue(true);
            }
        }

        log.info("Basic Balana Test 0001 is finished");
    }
}
