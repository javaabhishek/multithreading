package com.soft.multithreading.iq;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    public static void main(String[] args) {
        // Create a CountDownLatch with a count of 4
        CountDownLatch latch = new CountDownLatch(4);

        // Create and start the first four threads
        for (int i = 0; i < 4; i++) {
            new Thread(new MyRunnable(latch), "Thread " + (i + 1)).start();
        }

        try {
            // Wait for all four threads to complete
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create and start the fifth thread
        new Thread(new MyRunnable(null), "Thread 5").start();
    }

    static class MyRunnable implements Runnable {
        private final CountDownLatch latch;

        public MyRunnable(CountDownLatch latch) {
            this.latch = latch;
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

            if (latch != null) {
                latch.countDown(); // Decrease the latch count
            }
        }
    }
}
