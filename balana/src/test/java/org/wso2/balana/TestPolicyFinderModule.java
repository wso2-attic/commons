
/*
 * @(#)TestPolicyFinderModule.java
 *
 * Copyright 2004-2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package org.wso2.balana;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.balana.combine.xacml2.DenyOverridesPolicyAlg;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;

import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.PolicyFinderResult;

import java.io.File;
import java.io.FileInputStream;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * A <code>PolicyFinderModule</code> used to handle all policies in the
 * conformance tests. It supports retrevial for requests and for references,
 * but is tightly coupled with the structure of the conformance tests, and
 * is definately not thread-safe.
 *
 * @author Seth Proctor
 */
public class TestPolicyFinderModule extends PolicyFinderModule
{

    // the finder that owns this module
    private PolicyFinder finder = null;

    // the policies we're currently using for request-based retrieval
    private Set<AbstractPolicy> policies = null;

    // a map of URIs to policies for the reference-based policies we're
    // currently providing, and the current namespace prefix
    private Map policyRefs = null;

    private String policyRefPrefix;

    // a map of URIs to policies for the reference-based policy sets we're
    // currently providing, and the current namespace prefix
    private Map policySetRefs = null;
    
    private String policySetRefPrefix;

    private FactoryConfig factoryConfig;

    private  Set<String> policyFiles;

    private String rootDirectory;

    private String versionDirectory;

    private PolicyCombiningAlgorithm combiningAlg;

    /**
     * the logger we'll use for all messages
     */
	private static Log log = LogFactory.getLog(TestPolicyFinderModule.class);    

    /**
     * Constructor
     *
     * @param policyFiles  set of policy paths
     */
    public TestPolicyFinderModule(String rootDirectory, String versionDirectory, Set<String> policyFiles) {
        this.policyFiles = policyFiles;
        this.rootDirectory = rootDirectory;
        this.versionDirectory = versionDirectory;
        this.policies = new HashSet<AbstractPolicy>();
    }

    /**
     * Initializes this module with the given finder.
     *
     * @param finder the <code>PolicyFinder</code> that owns this module
     */
    public void init(PolicyFinder finder) {
        
        this.finder = finder;
        ConfigurationStore store = finder.getConfig();

        this.factoryConfig =  new FactoryConfig(store.getDefaultAttributeFactoryProxy(),
            store.getDefaultCombiningFactoryProxy(), store.getDefaultFunctionFactoryProxy());
        try {
            loadPolicies();
            combiningAlg = new DenyOverridesPolicyAlg();
        } catch (Exception e) {
            log.error("Fail to initialize the policy finder module as policies can not be loaded.");
        }
    }

    /**
     * Always returns true, since request-based retrieval is supported.
     *
     * @return true
     */
    public boolean isRequestSupported() {
        return true;
    }

    /**
     * Always returns true, since reference-based retrieval is supported.
     *
     * @return true
     */
    public boolean isIdReferenceSupported() {
        return true;
    }

    /**
     * Re-sets the policies known to this module to the single policy
     * provided in the given file.
     *
     * @param policyFile a file containing a policy or policy set
     *
     * @throws Exception if the policy cannot be loaded
     */
    public void setPolicies(String policyFile) throws Exception {
        
        policies.clear();
        AbstractPolicy policy = loadPolicy(policyFile, finder);
        if (policy == null){
            throw new Exception("failed to load policy");
        }
        policies.add(policy);
    }

    /**
     * Re-sets the policies known to this module to those contained in the
     * given files.
     *
     *
     * @throws Exception if the any of the policies cannot be loaded
     */
    public void loadPolicies() throws Exception {

        policies.clear();
        if(policyFiles != null){
            for (String policyFile : policyFiles) {
                File file = new File(".");
                String filePath =  file.getCanonicalPath() + File.separator +   TestConstants.RESOURCE_PATH +
                            File.separator + rootDirectory + File.separator + versionDirectory +
                            File.separator + TestConstants.POLICY_DIRECTORY + File.separator + policyFile;
                AbstractPolicy policy = loadPolicy(filePath, finder);
                if (policy == null){
                    throw new Exception("failed to load policy");
                }
                policies.add(policy);
            }
        } else {
            
        }
    }

    /**
     * Re-sets the policy reference mapping used for policies.
     *
     * @param policyRefs the reference mapping
     * @param prefix the prefix for these references
     */
    public void setPolicyRefs(Map policyRefs, String prefix) {
        this.policyRefs = policyRefs;
        policyRefPrefix = prefix;
    }

    /**
     * Re-sets the policy reference mapping used for policy sets.
     *
     * @param policySetRefs the reference mapping
     * @param prefix the prefix for these references
     */
    public void setPolicySetRefs(Map policySetRefs, String prefix) {
        this.policySetRefs = policySetRefs;
        policySetRefPrefix = prefix;
    }

    /**
     * Finds the applicable policy (if there is one) for the given context.
     *
     * @param context the evaluation context
     *
     * @return an applicable policy, if one exists, or an error
     */
    public PolicyFinderResult findPolicy(EvaluationCtx context) {
        
        ArrayList<AbstractPolicy> selectedPolicies = new ArrayList<AbstractPolicy>();
        Iterator it = policies.iterator();

        // iterate through all the policies we currently have loaded
        while (it.hasNext()) {
            AbstractPolicy policy = (AbstractPolicy)(it.next());
            MatchResult match = policy.match(context);
            int result = match.getResult();

            // if target matching was indeterminate, then return the error
            if (result == MatchResult.INDETERMINATE)
                return new PolicyFinderResult(match.getStatus());

            // see if the target matched
            if (result == MatchResult.MATCH) {

                if ((combiningAlg == null) && (selectedPolicies.size() > 0)) {
                    // we found a match before, so this is an error
                    ArrayList<String> code = new ArrayList<String>();
                    code.add(Status.STATUS_PROCESSING_ERROR);
                    Status status = new Status(code, "too many applicable "
                                               + "top-level policies");
                    return new PolicyFinderResult(status);
                }

                // this is the first match we've found, so remember it
                selectedPolicies.add(policy);
            }
        }

        // no errors happened during the search, so now take the right
        // action based on how many policies we found
        switch (selectedPolicies.size()) {
        case 0:
            if(log.isDebugEnabled()){
                log.debug("No matching XACML policy found");
            }
            return null;
        case 1:
             return new PolicyFinderResult((selectedPolicies.get(0)));
        default:
            return new PolicyFinderResult(new PolicySet(null, combiningAlg, null, selectedPolicies));
        }
    }

    /**
     * Resolves the reference to a policy or policy set, if possible.
     *
     * @param idReference the reference to resolve
     * @param type policy or policy set
     * @param constraints ignored since this test uses only pre-2.0 policies
     *
     * @return the referenced policy, if one exists, or an error
     */
    public PolicyFinderResult findPolicy(URI idReference, int type,
                                         VersionConstraints constraints,
                                         PolicyMetaData metaData) {
        String fileName = null;
        
        // based on the type, see if we have any references available, and
        // if we do then get the filename
        if (type == PolicyReference.POLICY_REFERENCE) {
            if (policyRefs == null)
                return new PolicyFinderResult();

            fileName = (String)(policyRefs.get(idReference.toString()));
        } else {
            if (policySetRefs == null)
                return new PolicyFinderResult();

            fileName = (String)(policySetRefs.get(idReference.toString()));
        }

        // if we had no mapping available, return with no referenced policy
        if (fileName == null)
            return new PolicyFinderResult();

        // append the correct prefix to the filename
        if (type == PolicyReference.POLICY_REFERENCE)
            fileName = policyRefPrefix + fileName;
        else
            fileName = policySetRefPrefix + fileName;

        // load the referenced policy
        AbstractPolicy policy = loadPolicy(fileName, finder);
 
        // if there was an error loading the policy, return the error
        if (policy == null) {
            ArrayList<String> code = new ArrayList<String>();
            code.add(Status.STATUS_PROCESSING_ERROR);
            Status status = new Status(code,
                                       "couldn't load referenced policy");
            return new PolicyFinderResult(status);
        }
        
        // return the referenced policy
        return new PolicyFinderResult(policy);
    }

    /**
     * Private helper that tries to load the given file-based policy, and
     * returns null if any error occurs.
     *
     * @param finder policy finder
     * @return  <code>AbstractPolicy</code>
     */
    private AbstractPolicy loadPolicy(String filePath, PolicyFinder finder) {
        try {

            // create the factory
            DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            
            // create a builder based on the factory & try to load the policy
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(filePath));
            
            // handle the policy, if it's a known type
            Element root = doc.getDocumentElement();
            String name = root.getLocalName();
            
            if (name.equals("Policy")) {
                return Policy.getInstance(root , factoryConfig);
            } else if (name.equals("PolicySet")) {
                return PolicySet.getInstance(root, finder, factoryConfig);
            }
        } catch (Exception e) {
            log.error("Fail to log policy : " + filePath , e);
        }
        return null;
    }

}
