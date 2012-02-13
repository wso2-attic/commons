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
package org.wso2.carbon.registry.metadata.test.wsdl;

import junit.framework.Assert;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.metadata.test.util.RegistryConsts;
import org.wso2.carbon.registry.relations.test.admin.commands.InitializeRelationsAdminCommand;
import org.wso2.carbon.registry.relations.test.admin.commands.RelationsAdminCommand;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;
import org.wso2.carbon.registry.resource.ui.beans.xsd.ResourceTreeEntryBean;

import java.io.File;

/**
 * This class used to add WSDL files in to the governance registry using resource-admin command.
 */
public class WSDLAdd extends TestTemplate {
    //private static final Log log = LogFactory.getLog(WSDLAdd.class);
    private ResourceAdminCommand resourceAdminCommand;
    RelationsAdminCommand relationsAdminCommand;
    private boolean isFound;


    @Override
    public void init() {
    }

    /**
     * runSuccessCase having two different of test-cases.adding wsdl from local file system and adding wsdl from global url.
     */
    @Override
    public void runSuccessCase() {

        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);
        RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub(sessionCookie);
        relationsAdminCommand = new RelationsAdminCommand(relationAdminServiceStub);
        resourceAdminCommand = new ResourceAdminCommand(resourceAdminServiceStub);
        addWSDL();
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    private void addWSDL() {
        String resource = frameworkPath + File.separator + "components" + File.separator + "registry" +
                          File.separator + "registry-metadata-test" + File.separator + "src" + File.separator +
                          "test" + File.separator + "java" + File.separator + "resources" + File.separator + "sample.wsdl";

        resourceAdminCommand.addResourceSuccessCase("/_system/governance/wsdls/sample.wsdl",
                                                    RegistryConsts.APPLICATION_WSDL_XML, "txtDesc", "file:///" + resource, null);
        resourceAdminCommand.importResourceSuccessCase("/_system/governance/wsdls", "WeatherForecastService.wsdl",
                                                       RegistryConsts.APPLICATION_WSDL_XML, "txtDesc",
                                                       "http://www.restfulwebservices.net/wcf/WeatherForecastService.svc?wsdl", null);
        ResourceTreeEntryBean searchFile1 = resourceAdminCommand.getResourceTreeEntrySuccessCase
                ("/_system/governance/wsdls/http/footballpool/dataaccess/eu");
        ResourceTreeEntryBean searchFile2 = resourceAdminCommand.getResourceTreeEntrySuccessCase
                ("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01");
        String[] resourceChild1 = searchFile1.getChildren();
        String[] resourceChild2 = searchFile2.getChildren();
        for (int childCount = 0; childCount <= resourceChild1.length; childCount++) {
            if (resourceChild1[childCount].equalsIgnoreCase("/_system/governance/wsdls/http/footballpool/dataaccess/eu/sample.wsdl")) {
                isFound = true;
                break;
            }
        }
        if (isFound = false) {
            Assert.fail("uploaded resource not found in /_system/governance/wsdls/http/footballpool/dataaccess/eu/sample.wsdl");
        }
        for (int childCount = 0; childCount <= resourceChild2.length; childCount++) {
            if (resourceChild2[childCount].equalsIgnoreCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl")) {
                isFound = true;
                break;
            }
        }
        if (isFound = false) {
            Assert.fail("uploaded resource not found in /_system/governance/wsdls/http/footballpool/dataaccess/eu/sample.wsdl");
        }
    }
}
