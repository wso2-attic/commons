/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.modules;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.axis2.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.wso2.mercury.persistence.PersistenceManager;
import org.wso2.mercury.state.RMDContext;
import org.wso2.mercury.state.RMSContext;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.util.RMDispatchInfo;
import org.wso2.mercury.workers.SequenceRemovalWorker;
import org.wso2.mercury.security.RMSecurityManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This module provides the WS-RM functionality to Axis2.
 */

public class MercuryModule implements Module {

    private static Log log = LogFactory.getLog(MercuryModule.class);

    private SequenceRemovalWorker sequenceRemovalWorker;
    // initialize the module
    public void init(ConfigurationContext configContext,
                     AxisModule module) throws AxisFault {

        // load the module parameters to the axis configuration if they are not already loaded
        AxisConfiguration axiConfiguration = configContext.getAxisConfiguration();
        if (axiConfiguration.getParameter(MercuryConstants.RMS_SEQUENCE_TIMEOUT) == null){
            axiConfiguration.addParameter(MercuryConstants.RMS_SEQUENCE_TIMEOUT,
                   Utils.getParameterValue(module.getParameter(MercuryConstants.RMS_SEQUENCE_TIMEOUT)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME) == null){
            axiConfiguration.addParameter(MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMS_SEQUENCE_RETRANSMIT_TIME)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME) == null){
            axiConfiguration.addParameter(MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMS_SEQUENCE_WORKER_SLEEP_TIME)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT) == null){
            axiConfiguration.addParameter(MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMS_MAXIMUM_RETRANSMIT_COUNT)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMD_SEQUENCE_TIMEOUT) == null){
            axiConfiguration.addParameter(MercuryConstants.RMD_SEQUENCE_TIMEOUT,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMD_SEQUENCE_TIMEOUT)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME) == null){
            axiConfiguration.addParameter(MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMD_SEQUENCE_RETRANSMIT_TIME)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME) == null){
            axiConfiguration.addParameter(MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.RMD_SEQUENCE_WORKER_SLEEP_TIME)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.INVOKER_TIMEOUT) == null){
            axiConfiguration.addParameter(MercuryConstants.INVOKER_TIMEOUT,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.INVOKER_TIMEOUT)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.INVOKER_WORKER_SLEEP_TIME) == null){
            axiConfiguration.addParameter(MercuryConstants.INVOKER_WORKER_SLEEP_TIME,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.INVOKER_WORKER_SLEEP_TIME)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.ENFORCE_RM) == null){
            axiConfiguration.addParameter(MercuryConstants.ENFORCE_RM,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.ENFORCE_RM)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.NOTIFY_THREADS) == null){
            axiConfiguration.addParameter(MercuryConstants.NOTIFY_THREADS,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.NOTIFY_THREADS)));
        }
        if (axiConfiguration.getParameter(MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING) == null){
            axiConfiguration.addParameter(MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING,
                    Utils.getParameterValue(module.getParameter(MercuryConstants.BUILD_MESSAGE_WITHOUT_WAITING)));
        }

        long sequenceRemvalWorkerSleepTime = 30000;
        if (axiConfiguration.getParameter(MercuryConstants.SEQUENCE_REMOVAL_WORKER_SLEEP_TIME) != null) {
            sequenceRemvalWorkerSleepTime =
                    Long.parseLong((String) axiConfiguration.getParameter(
                            MercuryConstants.SEQUENCE_REMOVAL_WORKER_SLEEP_TIME).getValue());
        } else if (module.getParameter(MercuryConstants.SEQUENCE_REMOVAL_WORKER_SLEEP_TIME) != null) {
            sequenceRemvalWorkerSleepTime =
                    Long.parseLong((String) module.getParameter(MercuryConstants.SEQUENCE_REMOVAL_WORKER_SLEEP_TIME).getValue());
        }

        // register two new context objects
        configContext.setProperty(MercuryConstants.RMS_CONTEXT, new RMSContext(configContext));
        configContext.setProperty(MercuryConstants.RMD_CONTEXT, new RMDContext(configContext));
        configContext.setProperty(MercuryConstants.RM_DISPATCH_INFO, new RMDispatchInfo());

        //initialize the persistence storage manager and stored save it in
        // configuratin context
        String persistanceManagerClass = null;
        if (axiConfiguration.getParameter(MercuryConstants.RM_PERSISTANCE_MANAGER) != null){
            persistanceManagerClass = (String) axiConfiguration.getParameter(
                    MercuryConstants.RM_PERSISTANCE_MANAGER).getValue();
        } else if (module.getParameter(MercuryConstants.RM_PERSISTANCE_MANAGER) != null) {
            persistanceManagerClass = (String) module.getParameter(MercuryConstants.RM_PERSISTANCE_MANAGER).getValue();
        }
        if (persistanceManagerClass != null) {
            try {
                Class persistanceClass = Class.forName(persistanceManagerClass);
                Constructor constructor = persistanceClass.getConstructor(new Class[]{AxisConfiguration.class});
                PersistenceManager persistenceManager =
                        (PersistenceManager) constructor.newInstance(new Object[]{axiConfiguration});
                configContext.setProperty(MercuryConstants.RM_PERSISTANCE_MANAGER, persistenceManager);
            } catch (InstantiationException e) {
                log.error("Can not instantiate persistence manager", e);
                throw new AxisFault("Can not instantiate persistence manager", e);
            } catch (IllegalAccessException e) {
                log.error("Illegal Access to persistence manager", e);
                throw new AxisFault("Illegal Access to persistence manager", e);
            } catch (ClassNotFoundException e) {
                log.error("Persistance manager class not found", e);
                throw new AxisFault("Persistance manager class not found", e);
            } catch (NoSuchMethodException e) {
                log.error("There is no constructor which takes an AxisConfiguration as an parameter", e);
                throw new AxisFault("There is no constructor which takes an AxisConfiguration as an parameter", e);
            } catch (InvocationTargetException e) {
                log.error("Problem with invoking constructor", e);
                throw new AxisFault("Problem with invoking constructor", e);
            }
        }

        //initialize the security storage manager and stored it in
        // configuratin context
        String securityManagerClassName = null;
        if (axiConfiguration.getParameter(MercuryConstants.RM_SECURITY_MANAGER) != null){
            securityManagerClassName = (String) axiConfiguration.getParameter(
                    MercuryConstants.RM_SECURITY_MANAGER).getValue();
        } else if (module.getParameter(MercuryConstants.RM_SECURITY_MANAGER) != null) {
            securityManagerClassName = (String) module.getParameter(MercuryConstants.RM_SECURITY_MANAGER).getValue();
        }
        if (securityManagerClassName != null) {
            try {
                Class securityManagerClass = Class.forName(securityManagerClassName);
                Constructor constructor = securityManagerClass.getConstructor(new Class[]{ConfigurationContext.class});

                RMSecurityManager rmSecurityManager = (RMSecurityManager) constructor.newInstance(new Object[]{configContext});
                rmSecurityManager.initSecurity(module);
                configContext.setProperty(MercuryConstants.RM_SECURITY_MANAGER, rmSecurityManager);
            } catch (InstantiationException e) {
                log.error("Can not instantiate security manager", e);
                throw new AxisFault("Can not instantiate persistence manager", e);
            } catch (IllegalAccessException e) {
                log.error("Illegal Access to security manager", e);
                throw new AxisFault("Illegal Access to persistence manager", e);
            } catch (ClassNotFoundException e) {
                log.error("Security manager class not found", e);
                throw new AxisFault("Persistance manager class not found", e);
            } catch (NoSuchMethodException e) {
                log.error("No Constructor with Configuration Context found", e);
                throw new AxisFault("No Constructor with Configuration Context found", e);
            } catch (InvocationTargetException e) {
                log.error("Problem in instantiating class", e);
                throw new AxisFault("Problem in instantiating class", e);
            }
        }

        // start the sequence remover.
        sequenceRemovalWorker = new SequenceRemovalWorker(configContext);
        sequenceRemovalWorker.setSequenceRemovalWorkerSleepTime(sequenceRemvalWorkerSleepTime);
        configContext.getThreadPool().execute(sequenceRemovalWorker);

    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
        //TODO: implement if necessary
    }

    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {
        // remove context objects
        log.info("Shutting down Mecury");
        sequenceRemovalWorker.shutDownWorker();
        configurationContext.removeProperty(MercuryConstants.RMS_CONTEXT);
        configurationContext.removeProperty(MercuryConstants.RMD_CONTEXT);
        configurationContext.removeProperty(MercuryConstants.RM_DISPATCH_INFO);
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
        //TODO: implement if necessary
    }

    public boolean canSupportAssertion(Assertion assertion) {
        //TODO: implement if necessary
        return false;
    }
}
