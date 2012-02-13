/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.siddhi.core.thread;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * ThreadPool for siddhi
 */
public class SiddhiThreadPool implements ExecutorService {

    private static Logger log;
    private int poolSize = Integer.MAX_VALUE;
    private int maxPoolSize = Integer.MAX_VALUE;
    private long keepAliveTime = 1;
    private ArrayBlockingQueue workQueue;
    private int workQueueSize = 5;
    private ThreadPoolExecutor threadPoolExecutor;

    public SiddhiThreadPool(int poolSize, int maxPoolSize, long keepAliveTime, int poolBlockQSize) {
        log = Logger.getLogger(this.getClass());
        workQueue = new ArrayBlockingQueue<Runnable>(poolBlockQSize);
        threadPoolExecutor = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    }

    public SiddhiThreadPool() {
        log = Logger.getLogger(this.getClass());
        workQueue = new ArrayBlockingQueue<Runnable>(5);
        threadPoolExecutor = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    public void terminate() {
        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            log.debug(this.getClass().getSimpleName() + " isTerminated: " + threadPoolExecutor.isTerminated());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Runnable> shutdownNow() {
        return threadPoolExecutor.shutdownNow();
    }

    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    public boolean isTerminated() {
        return threadPoolExecutor.isTerminated();
    }

    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return threadPoolExecutor.awaitTermination(l, timeUnit);
    }

    public <T> Future<T> submit(Callable<T> tCallable) {
        return threadPoolExecutor.submit(tCallable);
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        return threadPoolExecutor.submit(runnable, t);
    }

    public Future<?> submit(Runnable runnable) {
        return threadPoolExecutor.submit(runnable);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables)
            throws InterruptedException {
        return threadPoolExecutor.invokeAll(callables);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables, long l,
                                         TimeUnit timeUnit) throws InterruptedException {
        return threadPoolExecutor.invokeAll(callables, l, timeUnit);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> callables)
            throws InterruptedException, ExecutionException {
        return threadPoolExecutor.invokeAny(callables);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> callables, long l, TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return threadPoolExecutor.invokeAny(callables, l, timeUnit);
    }

    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }
}
