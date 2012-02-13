/* Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*  http://www.apache.org/licenses/LICENSE-2.0
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.stratos.tenant.mgt.ui.TenantMgtAdminServiceStub;
import org.wso2.stratos.tenant.mgt.ui.beans.xsd.TenantInfoBean;

import java.io.File;

public class TenantTestCase extends TestTemplate {

    private static final Log log = LogFactory.getLog(TenantTestCase.class);

    @Override
    public void init() {
    }

    @Override
    public void runSuccessCase() {
        try {
            File filePath = new File("./");
            String relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }

            TenantMgtAdminServiceStub tenantMgtAdminServiceStub = new InitializeTenantMgtAdminService().executeAdminStub(sessionCookie);
            TenantMgtAdminCommand tenantMgtAdminCommand = new TenantMgtAdminCommand(tenantMgtAdminServiceStub);
            //  TenantInfoBean tenantInfoBean  = tenantMgtAdminCommand.getTenantCommand("thika.gmail.com");
            for (int i = 0; i <= 1000; i++) {
                TenantInfoBean newTenantInfoBean = new TenantInfoBean();
                TenantInfoBean getTenantInfoBean = new TenantInfoBean();
                newTenantInfoBean.setEmail("tenant_load" + i + "@wso2.com");
                newTenantInfoBean.setAdminPassword("abc1231");
                newTenantInfoBean.setAdmin("loadtenant_new1" + i);
                newTenantInfoBean.setTenantDomain("tenant_load" + i + ".com");
                newTenantInfoBean.setFullname("wso2_Inc_" + i);
                System.out.println("Tenant ID " + i + " inserting");
                tenantMgtAdminCommand.addTenantCommand(newTenantInfoBean);
                getTenantInfoBean = tenantMgtAdminCommand.getTenantCommand("tenant_load" + i + ".com");
                System.out.println("Added Tenant Domain = "+getTenantInfoBean.getTenantDomain());
                getTenantInfoBean.setTenantDomain("Updated-tenant_load" + i + ".com");
                tenantMgtAdminCommand.updateTenant(getTenantInfoBean);

            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void runFailureCase() {
    }

    @Override
    public void cleanup() {
        //      loadDefaultConfig();

    }
}
