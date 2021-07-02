package com.mzl.concurrentProgramming.chap2;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ReadThread
 * @description: 指令重排序
 * @date 2021/7/2 11:49
 */
public class InstructionReordering{
    private static int num = 0;
    private static boolean ready = false;

    public static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (ready) { // 1
                    System.out.println(num + num); // 2
                }
                System.out.println("read thread....");
            }
        }
    }

    public static class WriteThread extends Thread {
        @Override
        public void run() {
            num = 2; // 3
            ready = true; //4
            System.out.println("writeThread set over....");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadThread rt = new ReadThread();
        rt.start();

        WriteThread wt = new WriteThread();
        wt.start();

        Thread.sleep(10);

        rt.interrupt();
        System.out.println("main exit");
    }

}

