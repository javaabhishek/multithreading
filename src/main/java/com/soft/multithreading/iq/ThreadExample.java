package com.soft.multithreading.iq;

public class ThreadExample {
    public static void main(String[] args) {
        // Create an array to hold the thread instances
        Thread[] threads = new Thread[5];

        // Create and start the first four threads
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(new MyRunnable(), "Thread " + (i + 1));
            threads[i].start();
        }

        // Wait for all the first four threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Create and start the fifth thread
        Thread thread5 = new Thread(new MyRunnable(), "Thread 5");
        thread5.start();
    }

    static class MyRunnable implements Runnable {
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
        }
    }
}
