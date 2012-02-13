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


package org.wso2.carbon.security.mgt.test.commands;

import junit.framework.TestCase;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.security.ui.config.ApplySecurity;
import org.wso2.carbon.security.ui.config.DisableSecurityOnService;
import org.wso2.carbon.security.ui.config.SecurityAdminServiceStub;

public class SecurityAdminCommand extends TestCase{

    private static final Log log = LogFactory.getLog(SecurityAdminCommand.class);
    SecurityAdminServiceStub securityAdminServiceStub;

    public SecurityAdminCommand(SecurityAdminServiceStub securityAdminServiceStub) {
        this.securityAdminServiceStub = securityAdminServiceStub;
        log.debug("ServiceAdminStub added");
    }

    public void disableSecuritySuccessCase(String serviceName)throws SecurityException
    {
      try{
          DisableSecurityOnService disableSecurityOnService = new DisableSecurityOnService();
          disableSecurityOnService.setServiceName(serviceName);
          securityAdminServiceStub.disableSecurityOnService(disableSecurityOnService);
          }
      catch (Exception e){
          log.error("Disable security failed : " +e.toString());
          Assert.fail("Disable security failed");
      }
   }

    public void disableSecurityFailureCase(String serviceName)throws SecurityException
    {
      try{
          DisableSecurityOnService disableSecurityOnService = new DisableSecurityOnService();
          disableSecurityOnService.setServiceName(serviceName);
          securityAdminServiceStub.disableSecurityOnService(disableSecurityOnService);
          log.error("Security disabled without authentication");
          Assert.fail("Security disabled without authentication");
          }
      catch (AxisFault e){
            if(e.toString().contains("AxisEngine Access Denied. Please login first")){
             Assert.fail("Expected exception not found");
             log.error("Expected exception not found");
            }

        }
        catch (Exception e)
        {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }

    public void applySecuritySuccessCase(ApplySecurity applySecurity)throws Exception
    {
        try{
            securityAdminServiceStub.applySecurity(applySecurity);
        }
        catch (Exception e)
        {
          log.error("Apply security failed : " +e.toString());
          Assert.fail("Apply security failed");
        }
    }

    public void applySecurityFailureCase(ApplySecurity applySecurity)throws Exception
    {
        try{
            securityAdminServiceStub.applySecurity(applySecurity);
        }
        catch (AxisFault e){
            if(e.toString().contains("AxisEngine Access Denied. Please login first")){
             Assert.fail("Expected exception not found");
             log.error("Expected exception not found");
            }

        }
        catch (Exception e)
        {
            Assert.fail("Unexpected Exception");
            e.printStackTrace();
            log.error("Unexpected Exception" + e.getMessage());
        }
    }


}
