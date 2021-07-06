package com.mzl.concurrentProgramming.chap5;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author lihuagen
 * @version 1.0
 * @className: CopyOnWriteArrayListTest
 * @description: 多线程下迭代器的弱一致性
 * @date 2021/7/6 9:04
 * 明明是指针传递的引用啊，而不是副本。 如果在该线程使用返回的迭代器遍历元素的过程中，其他线程没有对 list 进行增删改，那么 snapshot 本身就是 list 的 array，因为它们是引用关系。
 * 但是如果在遍历期间其他线程对该 list 进行了增删改，那么 snapshot 快照了，因为增删改后 list 里面的数组被新数组替换了，这时候老数组被 snapshot 引用 。
 * 这也说明获取迭代器后，使用该迭代器元素时，其他线程对该list进行的增删改不可见，因为他们操作的是两个不同的数组，这就是弱一致性。
 */
public class CopyOnWriteArrayListTest {

    private static volatile CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
//        demo1();
        demo2();
    }

    // 校验 CopyOnWriteArrayList 弱一致性
    public static void demo1() throws InterruptedException {
        arrayList.add("hello");
        arrayList.add("alibaba");
        arrayList.add("welcome");
        arrayList.add("to");
        arrayList.add("hangzhou");

        Thread threadOne = new Thread(() -> {
            arrayList.set(1, "baba");
            arrayList.remove(2);
            arrayList.remove(3);
        });

        Iterator<String> itr = arrayList.iterator();
        threadOne.start();
        threadOne.join();

        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        System.out.println(arrayList);
    }

    // 主线程先获取到了array {1，2，3}，然后子线程拿到CPU，执行 remove ，删除掉1 ，
    // 删除的过程是：先拿到 ReentrantLock ，确保只有这个线程会删除元素，然后拷贝到 newArray，删除 “1” 后再将 array 指向 {2，3}，
    // 而主线程仍是指向的{1，2，3}。因此主线程 get(0) 取的元素仍是 1，虽然 “1” 在子线程中已经被删除了。
    // 这就是 写时复制策略产生的 弱一致性 问题。
    public static void demo2() throws InterruptedException {
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");

        Thread thread = new Thread(() -> {
            arrayList.remove(0);
        });

        thread.start();
//        thread.join();
        String s = arrayList.get(0);
        System.out.println(s);

    }
}
