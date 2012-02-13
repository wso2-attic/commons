/*
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.apache.openjpa.osgi;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.kernel.BrokerFactory;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.util.Options;
import org.apache.openjpa.osgi.deployment.OSGiAwareClassLoaderManager;
import org.apache.openjpa.persistence.*;

import java.util.Map;

public class OSGiPersistenceProviderImpl extends PersistenceProviderImpl {

    private OSGiAwareClassLoaderManager classLoaderManager;

    public OSGiPersistenceProviderImpl(){
        classLoaderManager = new OSGiAwareClassLoaderManager();
    }


    @Override
    public OpenJPAEntityManagerFactory createEntityManagerFactory(String name, String resource, Map m) {
        OSGiPersistenceProductDerivation pd = new OSGiPersistenceProductDerivation();
        try {
            Object poolValue = Configurations.removeProperty(EMF_POOL, m);
            if(classLoaderManager == null){
                throw new PersistenceException("OSGiAwareClasssLoaderManager null.", null, null, true);
            }
            ConfigurationProvider cp = pd.load(resource, name, m, classLoaderManager.getClassLoader(name));
            if (cp == null) {
                return null;
            }

            BrokerFactory factory = getBrokerFactory(cp, poolValue, null);
            OpenJPAConfiguration conf = factory.getConfiguration();
            _log = conf.getLog(OpenJPAConfiguration.LOG_RUNTIME);
            pd.checkPuNameCollisions(_log,name);

            loadAgent(_log, conf);

            // TODO - Can this be moved back to BrokerImpl.initialize()?
            // Create appropriate LifecycleEventManager
            loadValidator(_log, conf);

            // We need to wait to preload until after we get back a fully configured/instantiated
            // BrokerFactory. This is because it is possible that someone has extended OpenJPA
            // functions and they need to be allowed time to configure themselves before we go off and
            // start instanting configurable objects (ie:openjpa.MetaDataRepository). Don't catch
            // any exceptions here because we want to fail-fast.
            Options o = Configurations.parseProperties(Configurations.getProperties(conf.getMetaDataRepository()));
            if(o.getBooleanProperty("Preload")){
                conf.getMetaDataRepositoryInstance().preload();
            }

            return JPAFacadeHelper.toEntityManagerFactory(factory);
        } catch (Exception e) {
            if (_log != null) {
                _log.error(_loc.get("create-emf-error", name), e);
            }

            /*
             *
             * Maintain 1.x behavior of throwing exceptions, even though
             * JPA2 9.2 - createEMF "must" return null for PU it can't handle.
             *
             * JPA 2.0 Specification Section 9.2 states:
             * "If a provider does not qualify as the provider for the named persistence unit,
             * it must return null when createEntityManagerFactory is invoked on it."
             * That specification compliance behavior has happened few lines above on null return.
             * Throwing runtime exception in the following code is valid (and useful) behavior
             * because the qualified provider has encountered an unexpected situation.
             */
            throw PersistenceExceptions.toPersistenceException(e);
        }
    }
}
