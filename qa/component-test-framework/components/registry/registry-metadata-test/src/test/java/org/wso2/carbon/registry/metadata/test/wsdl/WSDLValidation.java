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

import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.properties.test.admin.commands.InitializePropertiesAdminCommand;
import org.wso2.carbon.registry.properties.test.admin.commands.PropertiesAdminCommand;
import org.wso2.carbon.registry.properties.ui.PropertiesAdminServiceStub;
import org.wso2.carbon.registry.properties.ui.beans.xsd.PropertiesBean;
import org.wso2.carbon.registry.properties.ui.utils.xsd.Property;
import org.wso2.carbon.registry.relations.test.admin.commands.InitializeRelationsAdminCommand;
import org.wso2.carbon.registry.relations.test.admin.commands.RelationsAdminCommand;
import org.wso2.carbon.registry.relations.ui.RelationAdminServiceStub;

/**
 * This class used to add WSDL files in to the governance registry using resource-admin command.
 */
public class WSDLValidation extends TestTemplate {
    //private static final Log log = LogFactory.getLog(WSDLValidation.class);
    private PropertiesAdminCommand propertiesAdminCommand;
    RelationsAdminCommand relationsAdminCommand;


    @Override
    public void init() {
    }

    /**
     * runSuccessCase having two different of test-cases.adding wsdl from local file system and adding wsdl from global url.
     */
    @Override
    public void runSuccessCase() {

        PropertiesAdminServiceStub propertiesAdminServiceStub = new InitializePropertiesAdminCommand().executeAdminStub(sessionCookie);
        RelationAdminServiceStub relationAdminServiceStub = new InitializeRelationsAdminCommand().executeAdminStub(sessionCookie);
        relationsAdminCommand = new RelationsAdminCommand(relationAdminServiceStub);
        propertiesAdminCommand = new PropertiesAdminCommand(propertiesAdminServiceStub);

        try {
            PropertiesBean propertiesBean = propertiesAdminCommand.getPropertiesSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "yes");
            Property[] property = propertiesBean.getProperties();
            for (int i = 0; i <= property.length - 1; i++) {
                if (property[i].getKey().equalsIgnoreCase("WSDL Validation")) {
                    assertTrue("WSDL validation not matched with expected result", property[i].getValue().equalsIgnoreCase("valid"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while validating wsdl : " + e);
        }
        addPropertyTest();
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
    }

    private void addPropertyTest() {
        try {
            propertiesAdminCommand.setPropertySuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "TestProperty", "sample-value");
            PropertiesBean propertiesBean = propertiesAdminCommand.getPropertiesSuccessCase("/_system/governance/wsdls/http/www/restfulwebservices/net/ServiceContracts/2008/01/WeatherForecastService.wsdl", "yes");
            Property[] property = propertiesBean.getProperties();
            for (int i = 0; i <= property.length - 1; i++) {
                if (property[i].getKey().equalsIgnoreCase("TestProperty")) {
                    assertTrue("Newly added property not found", property[i].getValue().equalsIgnoreCase("sample-value"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while adding property " + e);
        }
    }
}
