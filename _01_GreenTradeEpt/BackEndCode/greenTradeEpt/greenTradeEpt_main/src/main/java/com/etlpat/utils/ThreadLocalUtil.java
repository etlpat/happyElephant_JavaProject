package com.etlpat.utils;


// ThreadLocal
//
//  功能：ThreadLocal提供线程局部变量
//      ①存取变量的方法：set()/get()
//      ②使用ThreadLocal存储的数据是线程安全的
//      ③用完记得调用remove方法释放，防止内存泄漏
//
//  原理：ThreadLocal为每个线程提供独立的空间来存储变量。
//       因此，不同线程存储的变量相互独立，互不干扰。
//
//  详细验证，见：src/test/java/com/etlpat/ThreadLocalTest.java


public class ThreadLocalUtil {
    // 创建一个全局唯一的ThreadLocal对象
    public static final ThreadLocal THREAD_LOCAL = new ThreadLocal();


    // 从ThreadLocal中取出变量
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    // 向ThreadLocal中添加变量
    public static void set(Object obj) {
        THREAD_LOCAL.set(obj);
    }

    // 移除ThreadLocal中变量
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
