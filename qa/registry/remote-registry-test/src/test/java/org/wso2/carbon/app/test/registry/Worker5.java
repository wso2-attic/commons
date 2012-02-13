package org.wso2.carbon.app.test.registry;

import java.net.URL;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

public class Worker5 extends Worker {

    public Worker5(String threadName, int iterations, Registry registry) {
        super(threadName, iterations, registry);
    }

    public void run() {

        long time1 = System.nanoTime();
        long timePerThread = 0;

        Resource resource = null;
        try {

            String path = "/ama/test/path";
            Resource resource1 = registry.newResource();
            resource1.setContent("ABC".getBytes());
            registry.put(path, resource1);

            
            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                Resource res = registry.newResource();
                res.setContent("abc".getBytes());
                registry.put("/test/" + i, res);
                System.out.println("Updated " + i);
                resource = registry.get(path);
                resource.setContent("updated");
                resource.setProperty("abc", "abc");
                registry.put(path, resource);
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