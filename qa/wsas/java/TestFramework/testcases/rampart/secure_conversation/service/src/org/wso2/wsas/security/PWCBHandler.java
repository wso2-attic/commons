
package org.wso2.wsas.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class PWCBHandler implements CallbackHandler{

	public void handle(Callback[] callbacks) throws IOException,
    UnsupportedCallbackException {
for (int i = 0; i < callbacks.length; i++) {
    WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
    String id = pwcb.getIdentifer();
    if("qaclient".equals(id)) {
        pwcb.setPassword("qaclient");
    } else if("qaserver".equals(id)) {
        pwcb.setPassword("qaserver");
    }
}
}
	
}
