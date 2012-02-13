package org.wso2.registry.jackrabbit;

public class JackrabbitConfig {
    
    private String repositoryXML;
    
    private String repositoryHome;
    
    private String repositoryName;
    
    private String systemUser;
    
    private String systemUserPassword;
    
    private String customNodetypesXML;
    
    public static final String INITIAL_CONTEXT_FACTORY = 
        "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory";
    
    public static final String PROVIDER_URL = "localhost";

    public String getRepositoryXML() {
        return repositoryXML;
    }

    public void setRepositoryXML(String repositoryXML) {
        this.repositoryXML = repositoryXML;
    }

    public String getRepositoryHome() {
        return repositoryHome;
    }

    public void setRepositoryHome(String repositoryHome) {
        this.repositoryHome = repositoryHome;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    public String getSystemUserPassword() {
        return systemUserPassword;
    }

    public void setSystemUserPassword(String systemUserPassword) {
        this.systemUserPassword = systemUserPassword;
    }

    public String getCustomNodetypesXML() {
        return customNodetypesXML;
    }

    public void setCustomNodetypesXML(String customNodetypesXML) {
        this.customNodetypesXML = customNodetypesXML;
    }
    
 
    
    

}
