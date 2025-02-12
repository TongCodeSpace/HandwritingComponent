package com.tong.schedule;

import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //simpleScheduleFail();
        schedule();


    }

    private static void simpleScheduleSuccess() throws InterruptedException {
        ScheduleService scheduleService = new ScheduleService();
        System.out.println("start schedule1");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }, 100L);
        Thread.sleep(1000L);
        System.out.println("start schedule2");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("world");
            }
        }, 100L);
    }

    private static void simpleScheduleFail() throws InterruptedException {
        ScheduleService scheduleService = new ScheduleService();
        System.out.println("start schedule1");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }, 100L);
        Thread.sleep(1000L);
        System.out.println("start schedule2");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("world");
            }
        }, 100L);
        System.out.println("start schedule3");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello2");
            }
        }, 100L);
        //第四个任务分配不到线程永远不会执行
        System.out.println("start schedule4");
        scheduleService.simpleSchedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("world2");
            }
        }, 100L);
    }


    private static void schedule() throws InterruptedException {
        ScheduleService scheduleService = new ScheduleService();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
        System.out.println("start schedule1");
        scheduleService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("time:" + dateTimeFormatter.format(java.time.LocalDateTime.now()) + ", hello");
            }
        }, 1000L);
        System.out.println("start schedule2");
        scheduleService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("time" + dateTimeFormatter.format(java.time.LocalDateTime.now()) + ", world");
            }
        }, 1000L);
        System.out.println("start schedule3");
        scheduleService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("time" + dateTimeFormatter.format(java.time.LocalDateTime.now()) + ", hello2");
            }
        }, 1000L);
        //第四个任务分配不到线程永远不会执行
        System.out.println("start schedule4");
        scheduleService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("time" + dateTimeFormatter.format(java.time.LocalDateTime.now()) + ", world2");
            }
        }, 1000L);
    }


}
