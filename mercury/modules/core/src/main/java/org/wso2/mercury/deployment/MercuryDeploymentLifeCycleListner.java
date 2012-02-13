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
package org.wso2.mercury.deployment;

import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.description.*;
import org.apache.axis2.deployment.DeploymentLifeCycleListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.persistence.exception.PersistenceException;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.persistence.dto.*;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.state.RMDSequence;
import org.wso2.mercury.state.RMDContext;
import org.wso2.mercury.client.MercuryClient;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Iterator;


public class MercuryDeploymentLifeCycleListner implements DeploymentLifeCycleListener {

    private static Log log = LogFactory.getLog(MercuryDeploymentLifeCycleListner.class);

    public void preDeploy(AxisConfiguration axisConfig) throws AxisFault {
        // nothing todo here
    }

    public void postDeploy(ConfigurationContext configurationContext) throws AxisFault {
        // at this moment all the deployment is over has to restart all the non terminated
        // RMD and RMS sequeces.
        PersistenceManager persistenceManager
                = (PersistenceManager) configurationContext.getProperty(MercuryConstants.RM_PERSISTANCE_MANAGER);
        if (persistenceManager != null) {
            try {
                startRMDSequences(persistenceManager, configurationContext);
                startRMSSequences(persistenceManager, configurationContext);
            } catch (PersistenceException e) {
                log.error("Can not start the persisted sequences", e);
                throw new AxisFault("Can not start the persisted sequences", e);
            }
        }

    }

    /**
     * here we start only the rmd sequences at the server side. the client side sequences
     * are started at the out handler level.
     * @param persistenceManager
     * @throws PersistenceException
     */
    private void startRMDSequences(PersistenceManager persistenceManager,
                                   ConfigurationContext configurationContext) throws PersistenceException, AxisFault {

        List rmdSequences = persistenceManager.getNonTerminatedRMDSequences();
        RMDSequenceDto rmdSequenceDto = null;
        Axis2InfoDto axis2InfoDto = null;
        RMDContext rmdContext = (RMDContext) configurationContext.getProperty(MercuryConstants.RMD_CONTEXT);
        for (Iterator iter = rmdSequences.iterator(); iter.hasNext();) {
            rmdSequenceDto = (RMDSequenceDto) iter.next();
            axis2InfoDto = persistenceManager.getAxis2InfoID(rmdSequenceDto.getAxis2InfoID());
            if (axis2InfoDto.isServerSide()) {
                rmdContext.loadRMDSequenceFromPersistanceStorage(
                        rmdSequenceDto, axis2InfoDto, persistenceManager, configurationContext);
            }
        }

    }

    private void startRMSSequences(PersistenceManager persistenceManager,
                                   ConfigurationContext configurationContext) throws PersistenceException, AxisFault {
        List<RMSSequenceDto> rmsSequences = persistenceManager.getNonTerminatedRMSSequences();
        Axis2InfoDto axis2InfoDto = null;
        List<EngagedModuleDto> engagedModules = null;
        InternalKeyDto internalKeyDto = null;
        for (RMSSequenceDto rmsSequenceDto : rmsSequences) {

            // sends a terminate sequence message after creating a service client
            axis2InfoDto = persistenceManager.getAxis2InfoID(rmsSequenceDto.getAxis2InfoID());
            // RMS sequences are started only at the client side. At server side RMSSequences are started
            // When RMD sequences are started.
            if (!axis2InfoDto.isServerSide()) {
                AxisService axisServce = getAnnonymousAxisService(axis2InfoDto.getServiceName());
                // we don't want to garbade collect this service. so add this to configuration and
                // send
                configurationContext.getAxisConfiguration().addService(axisServce);
                ServiceClient serviceClient = new ServiceClient(configurationContext, axisServce);
                serviceClient.setTargetEPR(new EndpointReference(rmsSequenceDto.getEndPointAddress()));

                engagedModules = persistenceManager.getEngagedModules(axis2InfoDto.getId());
                for (EngagedModuleDto engagedModuleDto : engagedModules) {
                    serviceClient.engageModule(engagedModuleDto.getModuleName());
                }

                internalKeyDto = persistenceManager.getInternalKeyWithID(rmsSequenceDto.getInternalKeyID());
                serviceClient.getOptions().setUseSeparateListener(true);
                serviceClient.getOptions().setProperty(MercuryClientConstants.RESUME_SEQUENCE, Constants.VALUE_TRUE);
                serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, internalKeyDto.getKey());
                // set the operation name according to the Mep
                if (rmsSequenceDto.getMep().equals(WSDL2Constants.MEP_URI_OUT_IN)) {
                    AxisCallback dummyCallback = new AxisCallback() {
                        public void onMessage(MessageContext msgContext) {
                        }

                        public void onFault(MessageContext msgContext) {
                        }

                        public void onError(Exception e) {
                        }

                        public void onComplete() {
                        }
                    };
                    serviceClient.sendReceiveNonBlocking(null, dummyCallback);
                } else {
                    serviceClient.fireAndForget(null);
                }

                // this have to be think more. do we have to stop here or not.
                serviceClient.getOptions().setProperty(MercuryClientConstants.RESUME_SEQUENCE, Constants.VALUE_FALSE);
                MercuryClient mercuryClient = new MercuryClient(serviceClient);
                mercuryClient.terminateSequence(internalKeyDto.getKey());
            } else {
                AxisService axisService =
                        configurationContext.getAxisConfiguration().getService(axis2InfoDto.getServiceName());
                ServiceClient serviceClient = new ServiceClient(configurationContext, axisService);
                serviceClient.setTargetEPR(new EndpointReference(rmsSequenceDto.getEndPointAddress()));

                internalKeyDto = persistenceManager.getInternalKeyWithID(rmsSequenceDto.getInternalKeyID());
                serviceClient.getOptions().setProperty(MercuryClientConstants.RESUME_SEQUENCE, Constants.VALUE_TRUE);
                serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, internalKeyDto.getKey());
                // here it is enogh to send a fire and forget since we don't have to register call backs.

                // this operation is added only to send this message through the service client
                // the original service may have different operations
                serviceClient.fireAndForget(new QName(axisService.getTargetNamespace(),
                        MercuryConstants.MERCURY_OPERATION_OUT_ONLY), null);

            }

        }

    }

    private AxisService getAnnonymousAxisService(String serviceName){
        AxisService axisService = new AxisService(serviceName);
        RobustOutOnlyAxisOperation robustoutoonlyOperation = new RobustOutOnlyAxisOperation(
                ServiceClient.ANON_ROBUST_OUT_ONLY_OP);
        axisService.addOperation(robustoutoonlyOperation);

        OutOnlyAxisOperation outOnlyOperation = new OutOnlyAxisOperation(ServiceClient.ANON_OUT_ONLY_OP);
        axisService.addOperation(outOnlyOperation);

        OutInAxisOperation outInOperation = new OutInAxisOperation(ServiceClient.ANON_OUT_IN_OP);
        axisService.addOperation(outInOperation);
        return axisService;
    }


}
