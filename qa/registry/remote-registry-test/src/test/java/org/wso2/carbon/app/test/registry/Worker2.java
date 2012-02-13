package org.wso2.carbon.app.test.registry;

import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.exceptions.ResourceNotFoundException;

public class Worker2 extends Worker {

    public Worker2(String threadName, int iterations, Registry registry) {
        super(threadName, iterations, registry);
    }

    public void run() {

        long time1 = System.nanoTime();

        try {
            for (int i = 0; i < iterations; i++) {
                Resource r1 = registry.newResource();
                r1.setContent("test content".getBytes());
                r1.setProperty("property1", "value1");
                r1.setProperty("property2", "value2");
                r1.setProperty("property3", "value3");
                //System.out.println("~~~~~begin put~~~~~");
                long putStart = System.nanoTime();
                //*****************************//
                registry.put(basePath + i, r1);
                //*****************************//
                long putEnd = System.nanoTime();
                long putTime = putEnd - putStart;
                System.out.println("CSV," + threadName + "," + "put," + putTime / 1000000);
                //System.out.println("~~~~~end put~~~~~");
                r1.discard();

                long getStart = System.nanoTime();
                //*****************************//
                Resource r2 = registry.get(basePath + i);
                //*****************************//
                long getEnd = System.nanoTime();
                long getTime = getEnd - getStart;
                System.out.println("CSV," + threadName + "," + "get," + getTime / 1000000);

                r2.discard();

                //System.out.println("~~~~~begin delete~~~~~");
                long deleteStart = System.nanoTime();
                //*****************************//
                registry.delete(basePath + i);
                //*****************************//
                long deleteEnd = System.nanoTime();
                long deleteTime = deleteEnd - deleteStart;
                System.out.println("CSV," + threadName + "," + "delete," + deleteTime / 1000000);

                //System.out.println("~~~~~end delete~~~~~");
            }

        } catch (RegistryException e) {
            //log.error("Error occured while running the performance test. Thread: " +
            //        threadName + ", Iterations: " + iterations, e);
            e.printStackTrace();
        }

        long time2 = System.nanoTime();

        long elapsedTime = time2 - time1;
        System.out.println("============= Thread: " + threadName + ". Time taken for test: " +
                elapsedTime + "  =============");
    }
}