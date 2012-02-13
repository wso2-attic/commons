package org.wso2.tools.ksexplorer;

import java.io.Serializable;

public class KeyInfo implements Serializable{
    
    private String alias;
    
    private String password;
    
    private boolean include = true;
    
    private boolean privateKey;

    public KeyInfo() {
        
    }
    
    public boolean isPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(boolean privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean include) {
        this.include = include;
    }
    

}
