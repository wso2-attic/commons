/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.ws.dataservice;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.receivers.RawXMLINOutMessageReceiver;

public class DBMessageReceiver extends RawXMLINOutMessageReceiver {
	/**
	 * Invokes the business logic invocation on the service implementation
	 * class
	 * 
	 * @param msgContext
	 *            the incoming message context
	 * @param newmsgContext
	 *            the response message context
	 * @throws AxisFault
	 *             on invalid method (wrong signature) or behavior (return
	 *             null)
	 */
	public void invokeBusinessLogic(MessageContext msgContext, MessageContext newMsgContext)
	throws AxisFault {
		try {
			OMElement result = DBUtils.invoke(msgContext);
			SOAPFactory fac = getSOAPFactory(msgContext);
			SOAPEnvelope envelope = fac.getDefaultEnvelope();
			if (result != null) {
				envelope.getBody().addChild(result);
			}
			newMsgContext.setEnvelope(envelope);

		} catch (AxisFault e) {
		    throw e;
		}
	}
}