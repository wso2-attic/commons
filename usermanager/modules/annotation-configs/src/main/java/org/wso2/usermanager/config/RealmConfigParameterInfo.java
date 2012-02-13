package org.wso2.usermanager.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RealmConfigParameterInfo {

    public boolean isRequired();

    public String getHelpText();
}