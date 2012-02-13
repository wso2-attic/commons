/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.system.test.core.utils.seleniumUtils;

import org.wso2.carbon.admin.service.utils.FrameworkSettings;

import java.io.File;


public class GRegBackEndURLEvaluator {

    public String getBackEndURL() {
       String baseURL;     
       if (FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT != null) {
           baseURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT + File.separator + "carbon" + File.separator;
       } else {
           baseURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + "carbon" + File.separator;
       }
       return baseURL;
   }
  }
