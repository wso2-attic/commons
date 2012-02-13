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

package org.wso2.carbon.registry.multitenancy.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.internal.RegistryCoreServiceComponent;
import org.wso2.carbon.registry.core.jdbc.EmbeddedRegistryService;
import org.wso2.carbon.registry.core.jdbc.InMemoryEmbeddedRegistryService;
import org.wso2.carbon.user.core.service.RealmService;

public class EmbeddedTestCase extends TestCase {
	
	private int iterationsNumber;
	private int concurrentUsers;
	private int workerClass;
	private int tenantsNumber;

	public EmbeddedTestCase(String text) {
		super(text);
	}

	

	public class RealmUnawareRegistryCoreServiceComponent extends
	RegistryCoreServiceComponent {

		public void setRealmService(RealmService realmService) {
			super.setRealmService(realmService);
		}
	}
	
	protected EmbeddedRegistryService registryService;
    protected Registry registry;

	public void setUp() throws Exception {

		setIterationsNumber(Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("iterations")));
		setConcurrentUsers(Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("concurrent-users")));
		setTenantsNumber(Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("tenants")));


		System.out.println("\n \n Number of iterations : " + getIterationsNumber());
		System.out.println("Number of concurrent users in a tenant domain :" + getConcurrentUsers());
		System.out.println("Number of tenant domains : " + getTenantsNumber());

		if (System.getProperty("carbon.home") == null) {
            File file = new File("src/test/resources/carbon-home");
            if (file.exists()) {
                System.setProperty("carbon.home", file.getAbsolutePath());
            }
            file = new File("../src/test/resources/carbon-home");
            if (file.exists()) {
                System.setProperty("carbon.home", file.getAbsolutePath());
            }
        }
		try {
			
			InputStream is;
	        try {
	            is = new FileInputStream("src/test/resources/registry.xml");
	        } catch (Exception e) {
	            is = null;
	        }
	        /*
	        RealmService realmService = new InMemoryRealmService();
	        RegistryContext registryContext  = RegistryContext.getBaseInstance(is, realmService);
	        registryContext.setSetup(true);
	        registryContext.selectDBConfig("h2-db");*/

	        registryService = new InMemoryEmbeddedRegistryService(is);
	        registry = registryService.getGovernanceUserRegistry("admin", 0);
	        
//			embeddedRegistryService = new InMemoryEmbeddedRegistryService();
//			comp = new RealmUnawareRegistryCoreServiceComponent();
//			comp.setRealmService(embeddedRegistryService.getRealmService());
//			comp.registerBuiltInHandlers(embeddedRegistryService);
//			registry = embeddedRegistryService.getSystemRegistry();



		} catch (Exception e) {
			fail("Failed to initialize the registry. Caused by: " + e.getMessage());
		}
	}

	public int getIterationsNumber() {
		return iterationsNumber;
	}

	public void setIterationsNumber(int iterationsNumber) {
		this.iterationsNumber = iterationsNumber;
	}

	public int getConcurrentUsers() {
		return concurrentUsers;
	}

	public void setConcurrentUsers(int concurrentUsers) {
		this.concurrentUsers = concurrentUsers;
	}

	public int getWorkerClass() {
		return workerClass;
	}

	public void setWorkerClass(int workerClass) {
		this.workerClass = workerClass;
	}

	public int getTenantsNumber() {
		return tenantsNumber;
	}

	public void setTenantsNumber(int tenantsNumber) {
		this.tenantsNumber = tenantsNumber;
	}
}

