package org.wso2.carbon.registry.multitenancy.test;

import org.wso2.carbon.registry.core.Registry;

public abstract class Worker extends Thread {

    protected int iterations;
    protected Registry registry;
    protected String threadName;
    protected String basePath;

    public Worker(String threadName, int iterations, Registry registry) {
        this.threadName = threadName;
        this.iterations = iterations;
        this.registry = registry;

        this.basePath = "/test/thread_" + threadName + "/resourceZ";
    }
}