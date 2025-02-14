package com.tong.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PersonalTheadPool {

    private int coreSize;
    private int maxSize;
    private int timeout;
    private TimeUnit timeUnit;
    private RejectHandler handler;
    List<CoreThread> corePool = Collections.synchronizedList(new ArrayList<>());
    List<SupportThread> supportPool = Collections.synchronizedList(new ArrayList<>());
    BlockingQueue<Runnable> jobs;



    public PersonalTheadPool(int coreSize, int maxSize,
                             int timeout, TimeUnit timeUnit,
                             ArrayBlockingQueue<Runnable> jobQueue,
                             RejectHandler handler) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.jobs = jobQueue;
        this.handler = handler;
    }

    public void execute(Runnable job) {
        //核心线程还有空闲，起新线程
        if (corePool.size() < coreSize) {
            CoreThread coreThread = new CoreThread();
            corePool.add(coreThread);
            coreThread.start();
        }
        //任务入列
        if(jobs.offer(job)) {
            //入列成功，直接结束
            return;
        }
        //队列满了起 support 线程进行支持
        if (supportPool.size() + coreSize < maxSize) {
            SupportThread supportThread = new SupportThread();
            supportPool.add(supportThread);
            supportThread.start();
        }
        //写线程池满了的抛弃策略
        if (!jobs.offer(job)) {
            //写线程池满了的抛弃策略
            handler.reject(job, this);
        }
    }

    class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable job = jobs.take();
                    job.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable job = jobs.poll(timeout, timeUnit);
                    if (job == null) {
                        break;
                    }
                    job.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " is over");
        }
    }

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public RejectHandler getHandler() {
        return handler;
    }

    public void setHandler(RejectHandler handler) {
        this.handler = handler;
    }

    public List<CoreThread> getCorePool() {
        return corePool;
    }

    public void setCorePool(List<CoreThread> corePool) {
        this.corePool = corePool;
    }

    public List<SupportThread> getSupportPool() {
        return supportPool;
    }

    public void setSupportPool(List<SupportThread> supportPool) {
        this.supportPool = supportPool;
    }

    public BlockingQueue<Runnable> getJobs() {
        return jobs;
    }

    public void setJobs(BlockingQueue<Runnable> jobs) {
        this.jobs = jobs;
    }
}
