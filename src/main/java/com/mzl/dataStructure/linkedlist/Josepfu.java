package com.mzl.dataStructure.linkedlist;

/**
 * 约瑟夫问题（环形单向链表）
 */
public class Josepfu {

    public static void main(String[] args) {
        // 测试一把看看构建环形链表，和遍历是否ok
        CircleSingleLinkedList circleSingleLinkedList = new CircleSingleLinkedList();
        circleSingleLinkedList.addNode(5);// 加入5个小孩节点
        circleSingleLinkedList.showNode();

        // 测试一把小孩出圈是否正确
        circleSingleLinkedList.countNode(2, 2, 5); // 2->4->1->5->3
    }

}

/**
 * 创建一个环形的单向链表
 */
class CircleSingleLinkedList {
    /**
     * 创建一个first节点,当前没有编号
     */
    private Node first = null;

    /**
     * 添加小孩节点，构建成一个环形的链表
     *
     * @param nums 有多少个小孩
     */
    public void addNode(int nums) {
        if (nums < 1) {
            System.out.println("nums值不正确");
            return;
        }
        // 辅助指针，帮助构建环形链表
        Node curNode = null;
        for (int i = 1; i <= nums; i++) {
            Node node = new Node(i);
            if (i == 1) {
                first = node;
                // 构成循环
                first.setNext(first);
                // 让curNode指向第一个节点
                curNode = first;
            } else {
                curNode.setNext(node);
                node.setNext(first);
                // 让curNode指向上一个节点
                curNode = node;
            }
        }
    }

    /**
     * 遍历当前的环形链表
     */
    public void showNode() {
        if (first == null) {
            System.out.println("没有任何小孩~~");
            return;
        }
        Node curNode = first;
        while (true) {
//            System.out.printf("小孩的编号 %d \n", curNode.getNo());
            // 说明遍历已结束
            if (curNode.getNext() == first) {
                break;
            }
            // 后移
            curNode = curNode.getNext();
        }
    }

    /**
     * 根据用户的输入，计算出小孩出圈的顺序
     *
     * @param startNo  表示从第几个小孩开始数数
     * @param countNum 表示数几下
     * @param nums     表示最初有多少小孩在圈中
     */
    public void countNode(int startNo, int countNum, int nums) {
        if (startNo < 1 || countNum < 1 || nums < 1) {
            System.out.println("参数异常，请正确传参");
        }
        // 临时变量，用来错开原环形链表，向上推一
        Node temp = first;
        while (true) {
            if (temp.getNext() == first) {
                break;
            }
            temp = temp.getNext();
        }

        // 因为默认first是指向第一个小孩，temp是指向第一小孩的上一个小孩，
        // 所以从第 startNo 个小孩开始的话，就得往前移动 startNo - 1
        for (int i = 0; i < startNo - 1; i++) {
            first = first.getNext();
            temp = temp.getNext();
        }

        while (true) {
            // 是最后一个小孩
            if (temp == first) {
                break;
            }
            for (int i = 0; i < countNum - 1; i++) {
                first = first.getNext();
                temp = temp.getNext();
            }
            System.out.println("出圈的人是：" + first.getNo());
            first = first.getNext();
            temp.setNext(first);
        }
        System.out.printf("最后留在圈中的小孩编号%d \n", first.getNo());
    }
}

/**
 * 创建一个Node类，表示一个节点
 */
class Node {
    private int no;
    private Node next;

    public Node(int no) {
        this.no = no;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
