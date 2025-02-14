package com.tong.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        PersonalTheadPool theadPool = new PersonalTheadPool(5, 10, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new DiscardRejectHandler());
        for (int i = 0; i < 100; i++) {
            int temp = i;
            theadPool.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + "    输出：" + temp);
            });
        }
        System.out.println("the main thread still running");

    }
}
