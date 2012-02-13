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
package org.wso2.carbon.logging.view.test;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.logging.view.test.commands.InitializeLogViewerAdmin;
import org.wso2.carbon.logging.view.test.commands.LogViewerAdminCommand;
import org.wso2.carbon.logging.view.types.axis2.GetLogsResponse;
import org.wso2.carbon.logging.view.types.axis2.SearchLogResponse;
import org.wso2.carbon.logging.view.types.carbon.LogMessage;

/**
 *
 */
public class implemantation  extends TestTemplate {

    private static final Log log = LogFactory.getLog(implemantation.class);

    @Override
    public void init() {
        log.info("Initializing LocalEntryCommandTest class ");
        log.debug("LocalEntryCommand Test Initialized");
    }
    @Override
    public void runSuccessCase() {
        try{
            boolean methodStatus;
            LogViewerStub logViewerStub = new InitializeLogViewerAdmin().executeAdminStub();
            LogMessage[] logMessage = new LogViewerAdminCommand(logViewerStub).addEntrySuccessCase("automation");
            for(int i=0;i<logMessage.length;i++){
                System.out.println(logMessage[i].getLogMessage());
            }


           }
        catch (Exception e)
        {
           e.printStackTrace();
        }
    }

     @Override
    public void runFailureCase() {

     }
     @Override
    public void cleanup() {

    }
}


