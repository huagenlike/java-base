package com.mzl.concurrentProgramming.chap1;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lihuagen
 * @version 1.0
 * @className: WaitLongTimeAndNanosTest
 * @description: TODO
 * @date 2021/6/20 11:01
 * public final void wait(long timeout, int nanos)
 *  timeout - 等待时间（以毫秒为单位）。
 *  nanos - 额外等待时间（以纳秒为单位）。
 */
public class WaitLongTimeAndNanosTest {
    private List synchedList;

    public WaitLongTimeAndNanosTest() {
        // 创建线程安全的linkedList
        synchedList = Collections.synchronizedList(new LinkedList<>());
    }

    // 删除列表中的元素
    public String removeElement() throws InterruptedException {
        synchronized (synchedList) {
            // 列表为空就等待，等待  5 秒加上 500 纳秒
            while (synchedList.isEmpty()) {
                System.out.println("List is empty...");
                synchedList.wait(5000, 500);
                System.out.println("Waiting...");
            }
            String element = (String) synchedList.remove(0);
            return element;
        }
    }

    // 添加元素到列表
    public void addElement(String element) {
        System.out.println("Opening...");
        synchronized (synchedList) {
            // 添加一个元素，并通知元素已存在
            synchedList.add(element);
            System.out.println("New Element:'" + element + "'");
            synchedList.notifyAll();
            System.out.println("notifyAll called!");
        }
        System.out.println("Closing...");
    }

    public static void main(String[] args) {
        final WaitLongTimeAndNanosTest demo = new WaitLongTimeAndNanosTest();
        Runnable runnableA = () -> {
            try {
                String item = demo.removeElement();
                System.out.println("" + item);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception!");
            } catch (Exception e) {
                System.out.println("Exception thrown.");
            }
        };

        Runnable runnableB = () -> {
            demo.addElement("Hello!");
        };

        try {
            Thread threadA1 = new Thread(runnableA, "Google");
            threadA1.start();

            Thread.sleep(500);

            Thread threadA2 = new Thread(runnableA, "Runoob");
            threadA2.start();

            Thread.sleep(500);

            Thread threadB = new Thread(runnableB, "Taobao");
            threadB.start();

            Thread.sleep(1000);

            threadA1.interrupt();
            threadA2.interrupt();
        } catch (InterruptedException x) {
        }

    }
}
