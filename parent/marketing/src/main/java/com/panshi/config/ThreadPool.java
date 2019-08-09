package com.panshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author huangxiaolei
 * @create 2019/08/05
 */
public class ThreadPool {

    private static ThreadPoolExecutor thread;

    private ThreadPool(){

    }

    public static ThreadPoolExecutor getExecutorService(){

        if(thread == null){
            thread = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }

        return thread;
    }

}
