/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.xkms2.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSException;
import org.wso2.xkms2.service.DefaultXKMSExecutor;
import org.wso2.xkms2.util.XKMSUtil;

import org.apache.neethi.util.Service;

/*
 * 
 */

public class XKMSMessageReceiver extends AbstractInOutMessageReceiver {

    private boolean isInit = false;

    static {
        org.apache.xml.security.Init.init();
    }

    private ProtocolExchange protocolExchange;

    public XKMSMessageReceiver() {
        this.protocolExchange = new ProtocolExchange();
    }

    public void invokeBusinessLogic(MessageContext inMsgCtx,
            MessageContext outMsgCtx) throws AxisFault {

        try {

            if (!isInit) {
                initExecutors(inMsgCtx);
            }
            
            OMElement outXKMSElement = protocolExchange
                    .exchangeServer(inMsgCtx);
            outXKMSElement = XKMSUtil.getOMElement(outXKMSElement);
            SOAPFactory fac = getSOAPFactory(inMsgCtx);
            SOAPEnvelope outEnvelop = fac.getDefaultEnvelope();
            outEnvelop.getBody().addChild(outXKMSElement);
            outMsgCtx.setEnvelope(outEnvelop);
        } catch (XKMSException e) {

            e.printStackTrace();
            throw AxisFault.makeFault(e);
        }

    }

    private void initExecutors(MessageContext inMsgCtx) throws AxisFault {

        List executorImpls = null;
        ServiceContext serviceContext = inMsgCtx.getServiceContext();

        Parameter parameter = inMsgCtx
                .getParameter(XKMS2Constants.XKMS_EXECUTOR_IMPL);
        if (parameter != null) {
            String paraValue = (String) parameter.getValue();
            String[] implClasses = paraValue.split(",");
            executorImpls = Arrays.asList(implClasses);

        }

        if (executorImpls == null) {
            String popValue = System
                    .getProperty(XKMS2Constants.XKMS_EXECUTOR_IMPL);
            if (popValue != null) {
                String[] implsClasses = popValue.split(",");
                executorImpls = Arrays.asList(implsClasses);
            }
        }

        List executorObjs = new ArrayList();

        if (executorImpls != null) {
            for (Iterator iterator = executorImpls.iterator(); iterator
                    .hasNext();) {
                String className = (String) iterator.next();
                try {
                    XKMSServiceExecutor executor = (XKMSServiceExecutor) Class
                            .forName(className.trim()).newInstance();
                    executorObjs.add(executor);

                } catch (ClassNotFoundException e) {
                    throw AxisFault.makeFault(e);
                } catch (InstantiationException e) {
                    throw AxisFault.makeFault(e);
                } catch (IllegalAccessException e) {
                    throw AxisFault.makeFault(e);
                }
            }

        }

        if (executorObjs.isEmpty()) {
            Iterator providers = Service.providers(XKMSServiceExecutor.class);
            for (; providers.hasNext();) {
                XKMSServiceExecutor executor = (XKMSServiceExecutor) providers
                        .next();
                executorObjs.add(executor);
            }
        }

        if (executorObjs.isEmpty()) {
            // switch for the Default
            DefaultXKMSExecutor executor = new DefaultXKMSExecutor();
            executorObjs.add(executor);
        }

        for (Iterator iterator = executorObjs.iterator(); iterator.hasNext();) {
            XKMSServiceExecutor executor = (XKMSServiceExecutor) iterator
                    .next();
            executor.init(serviceContext);
            String[] types = executor.getAssociatedElemenTypes();
            for (int i = 0; i < types.length; i++) {
                XKMSServiceExecutorManager.register(types[i], executor);
            }
        }

        isInit = true;
    }

}
