package org.wso2.wsas.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class PWCBHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
    UnsupportedCallbackException {
for (int i = 0; i < callbacks.length; i++) {
    
    //When the server side need to authenticate the user
    WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
    if (pwcb.getUsage() == WSPasswordCallback.USERNAME_TOKEN_UNKNOWN) {
        if(pwcb.getIdentifer().equals("charitha") && pwcb.getPassword().equals("charitha")) {
            return;
        } else {
            throw new UnsupportedCallbackException(callbacks[i], "check failed");
        }
    }
    
    //When the client requests for the password to be added in to the 
    //UT element
    pwcb.setPassword("charitha");
}
}
}
