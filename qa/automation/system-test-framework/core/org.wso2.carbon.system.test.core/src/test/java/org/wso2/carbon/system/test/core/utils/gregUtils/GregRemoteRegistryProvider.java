package org.wso2.carbon.system.test.core.utils.gregUtils;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class GregRemoteRegistryProvider {
    private static final Log log = LogFactory.getLog(GregRemoteRegistryProvider.class);
    public RemoteRegistry registry;

    public RemoteRegistry getRegistry(String tenantId) {
        String registryURL;
        //tenant details
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        String username = tenantDetails.getTenantName();
        String password = tenantDetails.getTenantPassword();

        if (FrameworkSettings.getStratosTestStatus()) {
           registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + "/t/" + tenantDetails.getTenantDomain() + File.separator + "registry" + File.separator;
       } else {
           if (FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT != null) {
               registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + FrameworkSettings.GREG_SERVER_WEB_CONTEXT_ROOT + File.separator + "registry" + File.separator;
           } else {
               registryURL = "https://" + FrameworkSettings.GREG_SERVER_HOST_NAME + ":" + FrameworkSettings.GREG_SERVER_HTTPS_PORT + File.separator + "registry" + File.separator;
           }

       }

        try {
            registry = new RemoteRegistry(new URL(registryURL), username, password);
        } catch (RegistryException e) {
            log.error("Registry API RegistryException thrown :" + e.getMessage());
            Assert.fail("Registry API RegistryException thrown :" + e.getMessage());
        } catch (MalformedURLException e) {
            log.error("Registry API MalformedURLException thrown :" + e.getMessage());
            Assert.fail("Registry API MalformedURLException thrown :" + e.getMessage());

        }
        return registry;
    }


}
