package org.wso2.carbon.registry.multitenancy.test;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;

public class Worker5 extends Worker {

    public Worker5(String threadName, int iterations, Registry registry) {
        super(threadName, iterations, registry);
    }

    public void run() {

        long time1 = System.nanoTime();
        long timePerThread = 0;
        //RemoteRegistry registry = null;
        Resource resource = null;
        try {
            //System.setProperty("javax.net.ssl.trustStore", "G:\\testing\\wso2registry-SNAPSHOT\\resources\\security\\client-truststore.jks");
            //System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
            //System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            //registry = new RemoteRegistry(new URL("https://10.100.1.136:9443/registry/"), "admin", "admin");
            //registry = new RemoteRegistry(new URL("https://10.100.1.235:9443/registry/"), "admin", "admin");
            Resource resource1 = registry.newResource();
            resource1.setContent("ABC");
            registry.put("/ama/test/path", resource1);
            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                Resource res = registry.newResource();
                res.setContent("abc");
                registry.put("/test/" + i, res);
                System.out.println("Updated " + i);
                resource = registry.get("/ama/test/path");
                resource.setContent("updated");
                resource.setProperty("abc", "abc");
                registry.put("/ama/test/path", resource);
                resource.discard();
                long end = System.nanoTime();
                timePerThread += (end - start);

                Thread.sleep(100);
            }
            long averageTime = timePerThread / (iterations * 1000000);
            System.out.println("CSV-avg-time-per-thread," + threadName + "," + averageTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}