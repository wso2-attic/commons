package org.wso2.registry.jackrabbit.security;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

public class RegistryLoginModule implements javax.security.auth.spi.LoginModule {

    private Subject subject;

    private CallbackHandler handler;

    private Map sharedState;

    private Map options;

    private boolean loginOk;

    public boolean abort() throws LoginException {
        return true;
    }

    public boolean commit() throws LoginException {
        return true;
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {

        this.subject = subject;
        this.handler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;

    }

    public boolean login() throws LoginException {
        NameCallback namecallback = new NameCallback("username :");
        PasswordCallback passwordcallback = new PasswordCallback("password :",
                false);

        try {
            handler.handle(new Callback[] { namecallback, passwordcallback });

            String username = namecallback.getName();
            String password = new String(passwordcallback.getPassword());

            if ("wso2".equals(username) && "wso2".equals(password)) {
                return true;
            } else {
                return false;
            }

        } catch (UnsupportedCallbackException e) {
        } catch (java.io.IOException e) {
        }

        return false;

    }

    public boolean logout() throws LoginException {
        return true;
    }

}
