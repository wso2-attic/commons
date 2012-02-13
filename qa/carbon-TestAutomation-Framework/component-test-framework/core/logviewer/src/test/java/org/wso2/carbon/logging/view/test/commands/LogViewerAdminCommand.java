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
package org.wso2.carbon.logging.view.test.commands;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.logging.view.types.axis2.GetLogs;
import org.wso2.carbon.logging.view.types.axis2.GetLogsResponse;
import org.wso2.carbon.logging.view.types.carbon.LogMessage;

import java.rmi.RemoteException;

/**
 *
 */
public class LogViewerAdminCommand extends TestCase{
    private static final Log log = LogFactory.getLog(LogViewerAdminCommand.class);
    LogViewerStub logViewerStub;

    public LogViewerAdminCommand(LogViewerStub logViewerStub) {
        this.logViewerStub = logViewerStub;
        log.debug("logViewerStub added");

    }

    public LogMessage[] addEntrySuccessCase(String keyword)throws RemoteException{
        GetLogsResponse getLogsResponse = new GetLogsResponse();
        GetLogs getLogs = new GetLogs();
        getLogs.setKeyword(keyword);
        getLogsResponse = logViewerStub.getLogs(getLogs);
        LogMessage[] logMessage  = getLogsResponse.get_return();
        return logMessage;
    }

    public void addEntryFailureCase()  {

    }
}

