package com.mzl.concurrentProgramming.chap1;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-06-05 21:33
 **/
public class WaitLongTimeTest {

    private List synchedList;

    public WaitLongTimeTest() {
        // 创建一个同步列表
        synchedList = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     * @description: 删除列表中的元素
     * @param null
     * @return:
     * @author: lihuagen
     * @time: 2021/6/17 18:18
     */
    public String removeElement() throws InterruptedException {
        synchronized (synchedList) {
            // 列表为空就等待
            while (synchedList.isEmpty()) {
                System.out.println("List is empty...");
                // 设置等待时间
                synchedList.wait(10000);
                System.out.println("Waiting...");
            }
            String element = (String) synchedList.remove(0);
            return element;
        }
    }

    /**
     * @description: 添加元素到列表
     * @param element 被添加的元素
     * @return:
     * @author: lihuagen
     * @time: 2021/6/17 18:19
     */
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
        final WaitLongTimeTest demo = new WaitLongTimeTest();
        Thread runA = new Thread(() -> {
            try {
                String item = demo.removeElement();
                System.out.println("" + item);
            } catch (InterruptedException ix) {
                System.out.println("Interrupted Exception!");
            } catch (Exception x) {
                System.out.println("Exception thrown.");
            }
        });

        Thread runB = new Thread(
            // 执行添加元素操作，并开始循环
            () -> demo.addElement("Hello!")
        );

        try {
            Thread threadA1 = new Thread(runA, "Baidu");
            threadA1.start();

            Thread.sleep(500);

            Thread threadA2 = new Thread(runA, "Mybj");
            threadA2.start();

            Thread.sleep(500);

            Thread threadB = new Thread(runB, "Taobao");
            threadB.start();

            Thread.sleep(1000);

            // 当一个线程调用共享对象的wait()方法被阻塞挂起后，如果其他线程中断了该线程，则该线程就会抛出 InterruptedException 异常，并返回
            threadA1.interrupt();
            threadA2.interrupt();
        } catch (InterruptedException x) {

        }
    }
}
