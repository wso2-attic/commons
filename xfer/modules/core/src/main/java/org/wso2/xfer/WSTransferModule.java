/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.wso2.xfer;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.modules.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

public class WSTransferModule implements Module {

    private static final Log LOG = LogFactory.getLog(WSTransferModule.class
            .getClass().getName());

    private static final String ATTR_SERVICE_CLASS = "ServiceClass";

    private static final String OP_NAME_PUT = "put";

    private static final String OP_NAME_GET = "get";

    private static final String OP_NAME_CREATE = "create";

    private static final String OP_NAME_DELETE = "delete";

    public void init(ConfigurationContext configContext, AxisModule module)
            throws AxisFault {
        // TODO Auto-generated method stub
    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {

        if (axisDescription instanceof AxisService) {
            try {

                AxisService axisService = (AxisService) axisDescription;
                String implClassName = (String) axisService
                        .getParameterValue(WSTransferModule.ATTR_SERVICE_CLASS);

                if (implClassName == null
                        || (implClassName = implClassName.trim()).length() == 0) {
                    if (LOG.isDebugEnabled()) {
                        LOG
                                .debug("WSTransferModule is not to engaged to service "
                                        + axisService.getName()
                                        + " because it doesn't contain an implementation class.");
                    }
                    return;
                }

                ClassLoader classLoader = axisService.getClassLoader();
                Class implClass = classLoader.loadClass(implClassName);

                processAxisService(axisService, implClass);

            } catch (Exception ex) {
                throw AxisFault.makeFault(ex);
            }
        }
    }

    private void processAxisService(AxisService axisService, Class implClass)
            throws Exception {

        AxisOperation transferOperation;
        WSTransferMessageReceiver transferMessageReceiver = new WSTransferMessageReceiver();

        if (WSTransferOperations.class.isAssignableFrom(implClass)) {

            transferMessageReceiver = new WSTransferMessageReceiver();

            transferOperation = axisService
                    .getOperation(new QName(OP_NAME_GET));
            if (transferOperation != null) {
                setSoapAction(WSTransferConstants.ACTION_URI_GET,
                        transferOperation);
                transferOperation
                        .setOutputAction(WSTransferConstants.ACTION_URI_GET_RESPONSE);
                transferOperation.setMessageReceiver(transferMessageReceiver);
            }

            transferOperation = axisService
                    .getOperation(new QName(OP_NAME_PUT));
            if (transferOperation != null) {
                setSoapAction(WSTransferConstants.ACTION_URI_PUT,
                        transferOperation);
                transferOperation
                        .setOutputAction(WSTransferConstants.ACTION_URI_PUT_RESPONSE);
                transferOperation.setMessageReceiver(transferMessageReceiver);
            }

            transferOperation = axisService.getOperation(new QName(
                    OP_NAME_DELETE));
            if (transferOperation != null) {
                setSoapAction(WSTransferConstants.ACTION_URI_DELETE,
                        transferOperation);
                transferOperation
                        .setOutputAction(WSTransferConstants.ACTION_URI_DELETE_RESPONSE);
                transferOperation.setMessageReceiver(transferMessageReceiver);
            }

        }

        if (WSTransferFactory.class.isAssignableFrom(implClass)) {

            transferOperation = axisService.getOperation(new QName(
                    OP_NAME_CREATE));

            if (transferOperation != null) {
                setSoapAction(WSTransferConstants.ACTION_URI_CREATE,
                        transferOperation);
                transferOperation
                        .setOutputAction(WSTransferConstants.ACTION_URI_CREATE_RESPONSE);
                transferOperation.setMessageReceiver(transferMessageReceiver);
            }
        }
    }

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription)
            throws AxisFault {
    }

    public void shutdown(ConfigurationContext configurationContext)
            throws AxisFault {
    }

    private void setSoapAction(String soapAction, AxisOperation op) {
        AxisService service = (AxisService) op.getParent();

//        Commenting out the following section to avoid a NPE from
//        AxisService#mapActionToOperation. Action mapping can't be removed by passing null for
//        AxisOperation.

//        String oldSoapAction = op.getInputAction();
//        if (oldSoapAction != null) {
            // effectively removing the old mapping
//            service.mapActionToOperation(oldSoapAction, null);
//        }

        op.setSoapAction(soapAction);
        service.mapActionToOperation(soapAction, op);
    }
}
