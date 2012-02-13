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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.wso2.carbon.registry.core.internal.RegistryCoreServiceComponent;
import org.wso2.carbon.registry.core.jdbc.InMemoryEmbeddedRegistryService;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.common.DefaultRealmService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.Tenant;
import org.wso2.carbon.user.core.tenant.TenantManager;




public class RemotePerfTest extends EmbeddedTestCase {


	private String username = "admin";
	private File outputFile;

	public RemotePerfTest(String text) {
		super(text);

	}

	protected RegistryService registryService = null;
	protected TenantManager tenantManager = null;

	protected ArrayList<Integer> tenantList = new ArrayList<Integer>();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		// create a embedding registry core service component  inner class
		class EmbeddingRegistryCoreServiceComponent extends
		RegistryCoreServiceComponent {

			public void setRealmService(RealmService realmService) {
				super.setRealmService(realmService);
			}
		}

		InMemoryEmbeddedRegistryService embeddedRegistryService = new InMemoryEmbeddedRegistryService();
		EmbeddingRegistryCoreServiceComponent comp = new EmbeddingRegistryCoreServiceComponent();
		comp.setRealmService(embeddedRegistryService.getRealmService());

		//    		registry = embeddedRegistryService.getConfigUserRegistry("admin", 0);

		// first we need to create a realm service, passing bundle context as null
		RealmService realmService = null;
		try {
			realmService = new DefaultRealmService(null);



			// now we create an instance of RegistryCoreServiceComponent
			EmbeddingRegistryCoreServiceComponent registryComponent =
				new EmbeddingRegistryCoreServiceComponent();
			// setting the realm service, the only service that the registry component depends on
			registryComponent.setRealmService(realmService);
			tenantManager = comp.getRealmService().getTenantManager();
			// now we can build the registryService, it is like calling the start of the service component except
			// setting the registry servie registered in the bundle context
			for (int i = 0; i < getTenantsNumber(); i++) {
				Tenant tenant = new Tenant();
				tenant.setDomain(UUID.randomUUID().toString());
				int tid = tenantManager.addTenant(tenant);
				tenantList.add(tid);
			}

			registryService = registryComponent.buildRegistryService();
			//    		registry = registryService.getRegistry(username, password);
			outputFile = new File("test-output.out");
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void testWorker1() throws Exception {

		int numUsers = getConcurrentUsers();
		//        TenantManager tenantManager = comp.getRealmService().getTenantManager();
		//        Tenant tenant = new Tenant();
		//        tenant.setDomain("random.com");
		//        int tenantId = tenantManager.addTenant(tenant);
		ArrayList<Long> tenantRunningTime = new ArrayList<Long>();

		long wholeTimeStart = System.nanoTime();
		for (int j : tenantList) {
			registry = registryService.getRegistry(username, j);


			Worker[] workers = new Worker[numUsers];
			for (int i = 0; i < numUsers; i++) {

				Worker worker = new Worker1("T" + i, getIterationsNumber(), registry);
				//System.out.println("inside loop");
				workers[i] = worker;
			}

			long time1 = System.nanoTime();

			for (int i = 0; i < numUsers; i++) {
				workers[i].start();
			}

			for (int i = 0; i < numUsers; i++) {
				workers[i].join();
			}

			long time2 = System.nanoTime();
			long elapsedTime = time2 - time1;
			tenantRunningTime.add(elapsedTime);
//			System.out.println("Time taken for tenant iteration " + j + " : " + elapsedTime / 1000000 + " ms");
		}
		long wholeTimeEnd = System.nanoTime();
		long totalTime = wholeTimeEnd - wholeTimeStart;
		int k = 1;
		
		StringBuilder builder = new StringBuilder();
		builder.append("\n \nNumber of iterations : " + getIterationsNumber());
		builder.append("\nNumber of concurrent users in a tenant domain :" + getConcurrentUsers());
		builder.append("\nNumber of tenant domains : " + getTenantsNumber());
		builder.append("\n Worker 1 : \n");
		for (Long runningTime : tenantRunningTime) {
			builder.append("Time taken for tenant "+ k++ + " of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + runningTime / 1000000 + " ms \n");
		}
		builder.append("\n Average time taken (per tenant per iteration) of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + (totalTime / (1000000 * getTenantsNumber() * getIterationsNumber())) + " ms" );
		builder.append("\n Time taken for the whole test: " + (totalTime) / 1000000 + " ms");
		
		System.out.println(builder.toString());
		
		FileWriter writer = new FileWriter(outputFile, true);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
		
	}

	public void testWorker2() throws Exception {

		ArrayList<Long> tenantRunningTime = new ArrayList<Long>();
		int numUsers = getConcurrentUsers();
		long wholeTimeStart = System.nanoTime();
		for (int j : tenantList) {
			registry = registryService.getRegistry(username, j);


			Worker[] workers = new Worker[numUsers];
			for (int i = 0; i < numUsers; i++) {
				Worker worker = new Worker2("T" + i, getIterationsNumber(), registry);
				workers[i] = worker;
			}

			long time1 = System.nanoTime();

			for (int i = 0; i < numUsers; i++) {
				workers[i].start();
			}

			for (int i = 0; i < numUsers; i++) {
				workers[i].join();
			}

			long time2 = System.nanoTime();
			long elapsedTime = time2 - time1;
			tenantRunningTime.add(elapsedTime);
//			System.out.println("Time taken for tenant iteration " + j + " : " + elapsedTime / 1000000 + " ms");
		}
		long wholeTimeEnd = System.nanoTime();
		long totalTime = wholeTimeEnd - wholeTimeStart;
		int k = 1;
		
		StringBuilder builder = new StringBuilder();
		builder.append("\n Worker 2 : \n");
		for (Long runningTime : tenantRunningTime) {
			builder.append("Time taken for tenant "+ k++ + " of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + runningTime / 1000000 + " ms \n");
		}
		builder.append("\n Average time taken (per tenant per iteration) of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + (totalTime / (1000000 * getTenantsNumber() * getIterationsNumber())) + " ms" );
		builder.append("\n Time taken for the whole test: " + (totalTime) / 1000000 + " ms");
		
		System.out.println(builder.toString());
		
		FileWriter writer = new FileWriter(outputFile, true);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
	}

	public void testWorker3() throws Exception {

		ArrayList<Long> tenantRunningTime = new ArrayList<Long>();
		int numUsers = getConcurrentUsers();
		long wholeTimeStart = System.nanoTime();
		for (int j : tenantList) {
			registry = registryService.getRegistry(username, j);
			Worker[] workers = new Worker[numUsers];
			for (int i = 0; i < numUsers; i++) {
				Worker worker = new Worker3("T" + i, getIterationsNumber(), registry);
				workers[i] = worker;
			}

			long time1 = System.nanoTime();

			for (int i = 0; i < numUsers; i++) {
				workers[i].start();
			}

			for (int i = 0; i < numUsers; i++) {
				workers[i].join();
			}

			long time2 = System.nanoTime();
			long elapsedTime = time2 - time1;
			tenantRunningTime.add(elapsedTime);
//			System.out.println("Time taken for tenant iteration " + j + " : " + elapsedTime / 1000000 + " ms");
		}
		long wholeTimeEnd = System.nanoTime();
		long totalTime = wholeTimeEnd - wholeTimeStart;
		int k = 1;
		
		StringBuilder builder = new StringBuilder();
		builder.append("\n Worker 3 : \n");
		for (Long runningTime : tenantRunningTime) {
			builder.append("Time taken for tenant "+ k++ + " of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + runningTime / 1000000 + " ms \n");
		}
		builder.append("\n Average time taken (per tenant per iteration) of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + (totalTime / (1000000 * getTenantsNumber() * getIterationsNumber())) + " ms" );
		builder.append("\n Time taken for the whole test: " + (totalTime) / 1000000 + " ms");
		
		System.out.println(builder.toString());
		
		FileWriter writer = new FileWriter(outputFile, true);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
	}

	public void testWorker4() throws Exception {

		ArrayList<Long> tenantRunningTime = new ArrayList<Long>();
		int numUsers = getConcurrentUsers();
		long wholeTimeStart = System.nanoTime();
		for (int j : tenantList) {
			registry = registryService.getRegistry(username, j);
			Worker[] workers = new Worker[numUsers];
			for (int i = 0; i < numUsers; i++) {
				Worker worker = new Worker4("T" + i, getIterationsNumber(), registry);
				workers[i] = worker;
			}

			long time1 = System.nanoTime();

			for (int i = 0; i < numUsers; i++) {
				workers[i].start();
			}

			for (int i = 0; i < numUsers; i++) {
				workers[i].join();
			}

			long time2 = System.nanoTime();
			long elapsedTime = time2 - time1;
			tenantRunningTime.add(elapsedTime);
//			System.out.println("Time taken for tenant iteration " + j + " : " + elapsedTime / 1000000 + " ms");
		}
		long wholeTimeEnd = System.nanoTime();
		long totalTime = wholeTimeEnd - wholeTimeStart;
		int k = 1;
		
		StringBuilder builder = new StringBuilder();
		builder.append("\n Worker 4 : \n");
		for (Long runningTime : tenantRunningTime) {
			builder.append("Time taken for tenant "+ k++ + " of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + runningTime / 1000000 + " ms \n");
		}
		builder.append("\n Average time taken (per tenant per iteration) of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + (totalTime / (1000000 * getTenantsNumber() * getIterationsNumber())) + " ms" );
		builder.append("\n Time taken for the whole test: " + (totalTime) / 1000000 + " ms");
		
		System.out.println(builder.toString());
		
		FileWriter writer = new FileWriter(outputFile, true);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
	}

	public void testWorker5() throws Exception {

		ArrayList<Long> tenantRunningTime = new ArrayList<Long>();
		int numUsers = getConcurrentUsers();
		long wholeTimeStart = System.nanoTime();
		for (int j : tenantList) {
			registry = registryService.getRegistry(username, j);
			Worker[] workers = new Worker[numUsers];
			for (int i = 0; i < numUsers; i++) {
				Worker worker = new Worker5("T" + i, getIterationsNumber(), registry);
				workers[i] = worker;
			}

			long time1 = System.nanoTime();

			for (int i = 0; i < numUsers; i++) {
				workers[i].start();
			}

			for (int i = 0; i < numUsers; i++) {
				workers[i].join();
			}

			long time2 = System.nanoTime();
			long elapsedTime = time2 - time1;
			tenantRunningTime.add(elapsedTime);
//			System.out.println("Time taken for tenant iteration " + j + " : " + elapsedTime / 1000000 + " ms");
		}
		long wholeTimeEnd = System.nanoTime();
		long totalTime = wholeTimeEnd - wholeTimeStart;
		int k = 1;
		
		StringBuilder builder = new StringBuilder();
		builder.append("\n Worker 5 : \n");
		for (Long runningTime : tenantRunningTime) {
			builder.append("Time taken for tenant "+ k++ + " of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + runningTime / 1000000 + " ms \n");
		}
		builder.append("\n Average time taken (per tenant per iteration) of " + getConcurrentUsers() + " users for " + getIterationsNumber() + " iterations : " + (totalTime / (1000000 * getTenantsNumber() * getIterationsNumber())) + " ms" );
		builder.append("\n Time taken for the whole test: " + (totalTime) / 1000000 + " ms \n \n");
		
		System.out.println(builder.toString());
		
		FileWriter writer = new FileWriter(outputFile, true);
		writer.append(builder.toString());
		writer.flush();
		writer.close();
	}

}
