/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 
  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/


package org.wso2.carbon.registry.resource.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.registry.resource.test.commands.InitializeResourceAdminCommand;
import org.wso2.carbon.registry.resource.test.commands.ResourceAdminCommand;
import org.wso2.carbon.registry.resource.ui.ResourceAdminServiceStub;

import javax.activation.DataHandler;
import java.io.File;
import java.net.URL;

public class ResourceAdminServiceTest extends TestTemplate {


    private static final Log log = LogFactory.getLog(ResourceAdminServiceTest.class);

    @Override
    public void init() {

    }

    @Override
    public void runSuccessCase(){
        try{
        ResourceAdminServiceStub resourceAdminServiceStub = new InitializeResourceAdminCommand().executeAdminStub(sessionCookie);

            String collectionPath = new ResourceAdminCommand(resourceAdminServiceStub).addCollectionSuccessCase("/","Test","","");
            log.debug("collection added to " + collectionPath);
           // new ResourceAdminCommand(resourceAdminServiceStub).addResourceSuccessCase("/Test/echo_back.xslt", "application/xml", "xslt files", null,null);
            new ResourceAdminCommand(resourceAdminServiceStub).addTextResourceSuccessCase("/Test","Hello","text/plain","sample","Hello world");

        }
        catch (Exception e){}

    }

    @Override
    public void runFailureCase() {


    }

    @Override
    public void cleanup() {

    }
}
