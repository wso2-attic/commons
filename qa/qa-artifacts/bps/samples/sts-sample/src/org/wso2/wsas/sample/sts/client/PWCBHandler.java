package org.wso2.wsas.sample.sts.client;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

public class PWCBHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
		WSPasswordCallback cb = (WSPasswordCallback) callbacks[0];
		cb.setPassword("wso2wsas");
	}
}
