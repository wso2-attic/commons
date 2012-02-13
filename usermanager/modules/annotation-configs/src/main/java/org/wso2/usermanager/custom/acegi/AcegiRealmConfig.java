package org.wso2.usermanager.custom.acegi;

import org.wso2.usermanager.config.RealmConfigParameterInfo;
import org.wso2.usermanager.config.UserManagerConfigException;

public class AcegiRealmConfig {

    public String authenticationProviderBeanMappingFile = null;

    public String authProviderId = null;

    public AcegiRealmConfig() {
    }

    public AcegiRealmConfig(AcegiRealmConfig oldConfig) {
        this.authenticationProviderBeanMappingFile = new String(oldConfig
                .getAuthenticationProviderBeanMappingFile());
        this.authProviderId = new String(oldConfig.getAuthProviderId());
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "BeanMapping file")
    public String getAuthenticationProviderBeanMappingFile() {
        return authenticationProviderBeanMappingFile;
    }

    public void setAuthenticationProviderBeanMappingFile(
            String authenticationProviderBeanMapping) {
        this.authenticationProviderBeanMappingFile = authenticationProviderBeanMapping;
    }

    @RealmConfigParameterInfo(isRequired = true, getHelpText = "Authentication provider id")
    public String getAuthProviderId() {
        return authProviderId;
    }

    public void setAuthProviderId(String authProviderId) {
        this.authProviderId = authProviderId;
    }

    public void validate() throws UserManagerConfigException {
        if (authProviderId == null
                || authenticationProviderBeanMappingFile == null) {
            throw new UserManagerConfigException("acegiConfigValidation");
        }

        if (authProviderId.equals("")
                || authenticationProviderBeanMappingFile.equals("")) {
            throw new UserManagerConfigException("acegiConfigValidation");
        }
    }

}
