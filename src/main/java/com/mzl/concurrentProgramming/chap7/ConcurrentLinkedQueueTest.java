package com.mzl.concurrentProgramming.chap7;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ConcurrentLinkedQueueTest
 * @description: TODO
 * @date 2021/7/12 15:46
 */
public class ConcurrentLinkedQueueTest {

    /**
    public boolean offer(E e) {
        //(1)e为null则抛出空指针异常
        checkNotNull(e);
        //(2)构造Node节点，在构造函数内部调用 unsafe.putObject
        final ConcurrentLinkedQueue.Node<E> newNode = new ConcurrentLinkedQueue.Node<E>(e);

        //(3)从尾节点进行插入
        for (ConcurrentLinkedQueue.Node<E> t = tail, p = t;;) {
            ConcurrentLinkedQueue.Node<E> q = p.next;
            //(4)如采q==null说明p是尾节点，则执行插入
            if (q == null) {
                //(5)使用 CAS设置p节点的next节点
                if (p.casNext(null, newNode)) {
                    //(6)CAS成功，则说明新增节点已经被放入链表，然后设置当前尾节点（包含head ，第1 , 3 , 5 ...个节点为尾节点）
                    if (p != t)
                        casTail(t, newNode);  // Failure is OK.
                    return true;
                }
            }
            else if (p == q)
                //(7)多线程操作时，由于 poll 操作移除元素后可能会把head变为自引用，也就是head得next变成了head ，所以这里需要重新找新的head
                p = (t != (t = tail)) ? t : head;
            else
                //(8)寻找尾节点
                p = (p != t && t != (t = tail)) ? t : q;
        }
    }
    */

}
