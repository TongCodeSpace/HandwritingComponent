package com.tong.pool;


public class DiscardRejectHandler implements RejectHandler {
    @Override
    public void reject(Runnable runnable, PersonalTheadPool pool) {
        pool.getJobs().poll();
        System.out.println("Discard");
        runnable.run();
    }
}
