/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package org.wso2.carbon.registry.resource.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.CollectionContentBean;

/* Add Collection test cases */

public class RegistryCollectionTest extends TestTemplate {
    /**
     * @goal testing add colleciton feature in registry
     */

    private static final Log log = LogFactory.getLog(RegistryCollectionTest.class);

    @Override
    public void init() {
        log.info("Registry collection test started");
    }

    @Override
    public void runSuccessCase() {
        try {
            CollectionContentBean collectionContentBean = new CollectionContentBean();
            ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation")) {
                        new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/TestAutomation");
                    }
                }
            }
            // adding normal collection
            String collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/", "TestAutomation", "", "");
            // Changing media type
            log.debug("collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "wso2.esb", "application/vnd.wso2.esb", "application/vnd.wso2.esb media type collection");
            log.debug("Media type application/vnd.apache.axis2 collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "apache.synapse", "application/vnd.apache.synapse", "application/vnd.apache.synapse media type collection");
            log.debug("Media type application/vnd.apache.synapse collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "apache.axis2", "application/vnd.apache.axis2", "application/vnd.apache.axis2 media type collection");
            log.debug("Media type application/vnd.apache.axis2 collection added to " + collectionPath);
            collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "wso2.wsas", "application/vnd.wso2.wsas", "application/vnd.wso2.wsas media type collection");
            log.debug("Media type application/vnd.wso2.wsas collection added to " + collectionPath);

            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/TestAutomation");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation/wso2.esb")) {
                        log.debug("/TestAutomation/wso2.esb resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/apache.synapse")) {
                        log.debug("/TestAutomation/apache.synapse resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/apache.axis2")) {
                        log.debug("/TestAutomation/apache.axis2 resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/wso2.wsas")) {
                        log.debug("/TestAutomation/wso2.wsas resource found");
                    }
                    else {
                        log.error("Resource didn't found in : " + childPath[i]);
                        Assert.fail("Resource didn't found in : " + childPath[i]);
                    }

                }
            }
            // Renaming collections
            new ResourceAdminCommand(resourceAdminServiceStub).renameResourceSuccessCase("/TestAutomation", "/TestAutomation/wso2.esb", "new_wso2.esb");
            new ResourceAdminCommand(resourceAdminServiceStub).renameResourceSuccessCase("/TestAutomation", "/TestAutomation/apache.synapse", "new_apache.synapse");
            new ResourceAdminCommand(resourceAdminServiceStub).renameResourceSuccessCase("/TestAutomation", "/TestAutomation/apache.axis2", "new_apache.axis2");
            new ResourceAdminCommand(resourceAdminServiceStub).renameResourceSuccessCase("/TestAutomation", "/TestAutomation/wso2.wsas", "new_wso2.wsas");
            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/TestAutomation");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.esb")) {
                        log.debug("/TestAutomation/new_wso2.esb resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.synapse")) {
                        log.debug("/TestAutomation/new_apache.synapse resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.axis2")) {
                        log.debug("/TestAutomation/new_apache.axis2 resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.wsas")) {
                        log.debug("/TestAutomation/new_wso2.wsas resource found");
                    }
                    else {
                        log.error("Resource didn't rename : " + childPath[i]);
                        Assert.fail("Resource didn't rename : " + childPath[i]);
                    }

                }
            }

            // move collections
            new ResourceAdminCommand(resourceAdminServiceStub).
                    addCollectionSuccessCase("/TestAutomation", "movedCollections", "",
                                             "Collections which contain moved sample collections");
            new ResourceAdminCommand(resourceAdminServiceStub).moveResourceSuccessCase("/TestAutomation", "/TestAutomation/new_wso2.esb", "/TestAutomation/movedCollections", "new_wso2.esb");
            new ResourceAdminCommand(resourceAdminServiceStub).moveResourceSuccessCase("/TestAutomation", "/TestAutomation/new_apache.synapse", "/TestAutomation/movedCollections", "new_apache.synapse");
            new ResourceAdminCommand(resourceAdminServiceStub).moveResourceSuccessCase("/TestAutomation", "/TestAutomation/new_apache.axis2", "/TestAutomation/movedCollections", "new_apache.axis2");
            new ResourceAdminCommand(resourceAdminServiceStub).moveResourceSuccessCase("/TestAutomation", "/TestAutomation/new_wso2.wsas", "/TestAutomation/movedCollections", "new_wso2.wsas");
            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/TestAutomation/movedCollections");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation/movedCollections/new_wso2.esb")) {
                        log.debug("/TestAutomation/movedCollections/new_wso2.esb resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/movedCollections/new_apache.synapse")) {
                        log.debug("/TestAutomation/movedCollections/new_apache.synapse resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/movedCollections/new_apache.axis2")) {
                        log.debug("/TestAutomation/movedCollections/new_apache.axis2 resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/movedCollections/new_wso2.wsas")) {
                        log.debug("/TestAutomation/movedCollections/new_wso2.wsas resource found");
                    }
                    else {
                        log.error("Resource didn't move : " + childPath[i]);
                        Assert.fail("Resource didn't move : " + childPath[i]);
                    }

                }
            }

            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/TestAutomation");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.esb")) {
                        log.error("/TestAutomation/new_wso2.esb resource found in original path after move ");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.synapse")) {
                        log.error("/TestAutomation/new_apache.synapse resource found in original path after move");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.axis2")) {
                        log.error("/TestAutomation/new_apache.axis2 resource found in original path after move");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.wsas")) {
                        log.error("/TestAutomation/new_wso2.wsas resource found in original path after move");
                    }
                }
            }

            //copy collections
            new ResourceAdminCommand(resourceAdminServiceStub).copyResourceSuccessCase("/TestAutomation/movedCollections", "/TestAutomation/movedCollections/new_wso2.esb", "/TestAutomation", "new_wso2.esb");
            new ResourceAdminCommand(resourceAdminServiceStub).copyResourceSuccessCase("/TestAutomation/movedCollections", "/TestAutomation/movedCollections/new_apache.synapse", "/TestAutomation", "new_apache.synapse");
            new ResourceAdminCommand(resourceAdminServiceStub).copyResourceSuccessCase("/TestAutomation/movedCollections", "/TestAutomation/movedCollections/new_apache.axis2", "/TestAutomation", "new_apache.axis2");
            new ResourceAdminCommand(resourceAdminServiceStub).copyResourceSuccessCase("/TestAutomation/movedCollections", "/TestAutomation/movedCollections/new_wso2.wsas", "/TestAutomation", "new_wso2.wsas");

            new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/TestAutomation/movedCollections");

            collectionContentBean = new ResourceAdminCommand(resourceAdminServiceStub).getCollectionContentSuccessCase("/TestAutomation");
            if (collectionContentBean.getChildCount() > 0) {
                String[] childPath = collectionContentBean.getChildPaths();
                for (int i = 0; i <= childPath.length - 1; i++) {
                    if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.esb")) {
                        log.debug("/TestAutomation/new_wso2.esb resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.synapse")) {
                        log.debug("/TestAutomation/new_apache.synapse resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_apache.axis2")) {
                        log.debug("/TestAutomation/new_apache.axis2 resource found");
                    }
                    else if (childPath[i].equalsIgnoreCase("/TestAutomation/new_wso2.wsas")) {
                        log.debug("/TestAutomation/new_wso2.wsas resource found");
                    }
                    else {
                        log.error("Resource didn't found after copoied : " + childPath[i]);
                        Assert.fail("Resource didn't rename after copoied : " + childPath[i]);
                    }
                }
            }
            collectionBoundaryTest(resourceAdminServiceStub);
        }
        catch (Exception e) {
            log.error("Exception thrown while running Registry collection test : " + e.getMessage());
            Assert.fail("Exception thrown while running Registry collection test : " + e.getMessage());
        }

    }

    public void collectionBoundaryTest(ResourceAdminServiceStub resourceAdminServiceStub) {

        // some characters may fail due to the CARBON-8331
        String collectionPath;
        String[] charBuffer = {"~", "!", "@", "#", "$", "%", "^", "*", "+", "=", "{", "}", "|", "\\", "<", ">", "\"", "\'", "(", ")", "[", "]", ";"};
        for (int i = 0; i <= charBuffer.length; i++) {
            try {
                System.out.println(charBuffer[i]);
                collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/TestAutomation", "wso2." + charBuffer[i], "application/vnd.wso2.esb", "application/vnd.wso2.esb media type collection");
                if (!collectionPath.equals(null)) {
                    log.error("Invalid collection added with illigal character " + charBuffer[i]);
                    new ResourceAdminCommand(resourceAdminServiceStub).deleteResourceSuccessCase("/TestAutomation");
                    Assert.fail("Invalid collection added with illigal character " + charBuffer[i]);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                //  if (!e.getMessage().contains("contains one or more illegal characters (~!@#$;%^*()+={}[]|\\<>\"',)" )) {
                if (!e.getMessage().contains("contains one or more illegal characters (~!@#$;%^*()+={}[]|\\<>)\"',")) {
                    log.error("Invalid collection added with illigal character " + charBuffer[i]);
                    Assert.fail("Invalid collection added with illigal character " + charBuffer[i]);

                }
            }
        }
    }

    @Override
    public void runFailureCase() {

    }

    @Override
    public void cleanup() {

    }
}
