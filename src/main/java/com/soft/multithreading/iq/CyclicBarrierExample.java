package com.soft.multithreading.iq;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        // Create a CyclicBarrier with a party size of 5
        CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                // This runnable will be executed when all 4 threads reach the barrier
                System.out.println("All threads have completed. Fifth thread can start now.");
            }
        });

        // Create and start the first four threads
        for (int i = 0; i < 4; i++) {
            new Thread(new MyRunnable(barrier), "Thread " + (i + 1)).start();
        }
    }

    static class MyRunnable implements Runnable {
        private final CyclicBarrier barrier;

        public MyRunnable(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running.");
            // Simulate some work
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " has finished.");

            try {
                // Wait for all threads to reach the barrier
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
