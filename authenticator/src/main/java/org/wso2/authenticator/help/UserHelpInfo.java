package org.wso2.authenticator.help;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserHelpInfo {

    public boolean isRequired();

    public String getHelpText();
    
    public String getInputType();
    
    public String getLabel();
    
}