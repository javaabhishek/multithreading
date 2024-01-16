package com.soft.multithreading.iq;

public class LastThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
                System.out.println(" Thread 1");
        });

        Thread t2=new Thread(()->{
            System.out.println(" Thread 2");
        });

        t1.start();
        t1.join();
        t2.start();
        t2.join();

        System.out.println("main thread");
    }
}
