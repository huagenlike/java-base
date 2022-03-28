package com.mzl.dataStructure.queue;

public class ArrayQueueDemo {
}

// 使用数组模拟队列-编写一个ArrayQueue类
class ArrayQueue {
    private int maxSize;// 表示数组的最大容量
    private int front;// 队列头
    private int rear;// 队列尾
    private int[] arr;// 该数据用于存放数据, 模拟队列

    // 创建队列的构造器
    public ArrayQueue(int arrMaxSize) {
        maxSize = arrMaxSize;
        arr = new int[arrMaxSize];
        front = -1; // 指向队列头部，分析出front是指向队列头的前一个位置.
        rear = -1; // 指向队列尾，指向队列尾的数据(即就是队列最后一个数据)
    }

    // 判断队列是否满
    public boolean isFull() {
        return false;
    }

    // 判断队列是否为空
    public boolean isEmpty() {
        return false;
    }

    // 添加数据到队列
    public void addQueue(int n) {

    }

    // 获取队列的数据, 出队列
    public int getQueue() {
        return 0;
    }

    // 显示队列的所有数据
    public void showQueue() {

    }
    // 显示队列的头数据， 注意不是取出数据
    public int headQueue() {
        return 0;
    }

}
