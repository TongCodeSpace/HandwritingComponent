package com.tong.schedule;

public class Job implements Comparable<Job> {

    private Runnable task;

    private Long time;

    private Long delay;


    public Job(Runnable task, Long time, Long delay) {
        this.task = task;
        this.time = time;
        this.delay = delay;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @Override
    public int compareTo(Job o) {
        return this.time.compareTo(o.time);
    }
}
