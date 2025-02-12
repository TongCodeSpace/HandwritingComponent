package com.tong.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * 定时任务执行器
 */
public class ScheduleService {

    ExecutorService pool = Executors.newFixedThreadPool(3);
    Trigger trigger = new Trigger();


    /**
     * 2. 线程池只负责执行任务，通过一个触发器进行延时操作
     */
    public void schedule(Runnable task, Long delay) {
        Job newJob = new Job(task, System.currentTimeMillis(),  delay);
        trigger.jobs.add(newJob);
        trigger.wakeUp();
    }

    /**
     * 1. 最简单的方式执行任务
     * @param task 执行任务
     * @param delay 延迟时间 单位：毫秒
     * 这是最容易想到的方式，问题是会收到线程池的线程数量限制
     */
    public void simpleSchedule(Runnable task, Long delay) {
        //task 需要按照延迟时间不断重复进行执行
        pool.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(delay);
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 等待合适的时间，把任务丢到线程池中
     */
    class Trigger {

        //需要阻塞防止线程安全问题
        PriorityBlockingQueue<Job> jobs = new PriorityBlockingQueue<>();

        //获取已经到了执行时间的任务并执行
        //jobs 中没有需要执行的任务时，线程会被挂起，需要手动执行
        Thread thead = new Thread(() -> {
            while (true) {
                while (jobs.isEmpty()) {
                    LockSupport.park();
                }
                Job job = jobs.peek();
                if (Long.compare(System.currentTimeMillis(), job.getTime()) > 0) {
                    pool.execute(jobs.poll().getTask());
                    jobs.offer(new Job(job.getTask(), System.currentTimeMillis() + job.getDelay(), job.getDelay()));
                } else {
                    LockSupport.parkUntil(job.getTime());
                }
            }
        });

        {
            thead.start();
        }

        //唤醒执行器的线程
         void wakeUp() {
            LockSupport.unpark(thead);
        }

    }
}
