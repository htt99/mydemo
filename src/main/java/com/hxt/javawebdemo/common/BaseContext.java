package com.hxt.javawebdemo.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>(); // 不必随着实例创建多次创建。（ThreadLocalMap的生命周期是跟随着该线程的生命周期的，和这个变量无关？就是key一样？还是这是单点？同时两个id登录呢？）

    public static void setCurrentId(Long id){ // 实际上是传递给了当前线程对应的（唯一；默认null，set后创建）ThreadLocalMap，再在map中set key为threadLocal，value为id
        threadLocal.set(id); //
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
