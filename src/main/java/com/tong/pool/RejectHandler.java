package com.tong.pool;

public interface RejectHandler {

    void reject(Runnable runnable, PersonalTheadPool pool);
}
