package com.mzl.dataStructure.stack;

import java.util.Scanner;

/**
 * 栈：
 * 先入后出的有序列表
 *  允许插入和删除的一端，为变化的一段，成为栈顶
 *  另一端为固定的一端，成为栈底
 * 应用场景：
 *  子程序的调用：在跳往子程序前，会先将下个指令的地址存到堆栈中，直到子程序执行完后再地址取出，以回到原来的程序中
 *  处理递归调用：和子程序的调用类似，只是除了存储下一个指令的地址外，也将参数、区域变量等数据存入堆栈中。
 *  表达式的转换【中缀表达式转后缀表达式】与求值（实际解决）
 *  二叉树的遍历
 *  图形的深度优先（depth-first）搜索法
 */
public class ArrayStackDemo {

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(5);
        String key = "";
        // 控制是否退出菜单
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);

        while (loop) {
            System.out.println("show：表示显示栈");
            System.out.println("exit：退出程序");
            System.out.println("push：表示添加数据到栈（入栈)");
            System.out.println("pop：表示从栈去除数据（出栈)");
            System.out.println("请输入您的选择");
            key = scanner.next();
            switch (key) {
                case "show":
                    stack.list();
                    break;
                case "push":
                    System.out.println("请输入一个数");
                    int value = scanner.nextInt();
                    stack.push(value);
                    break;
                case "pop":
                    int res = stack.pop();
                    System.out.println("出栈的数据是：" + res);
                    break;
                case "exit":
                    scanner.close();
                    loop = false;
                    break;
            }
        }
        System.out.println("程序退出");
    }

}

class ArrayStack {
    /**
     * 栈的大小
     */
    private int maxSize;

    /**
     * 数组，数组模拟栈，数据就存放在该数组
     */
    private int[] stack;

    /**
     * 表示栈顶，初始化为-1
     */
    private int top = -1;

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        stack = new int [this.maxSize];
    }

    /**
     * 栈满
     */
    public boolean isFull() {
        return top == maxSize - 1;
    }

    /**
     * 栈空
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * 入栈
     */
    public void push(int value) {
        if (isFull()) {
            System.out.println("栈满");
            return;
        }
        top++;
        stack[top] = value;
    }

    /**
     * 出栈
     */
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("栈空，没有数据");
        }
        int value = stack[top];
        top--;
        return value;
    }

    public void list() {
        if (isEmpty()) {
            System.out.println("栈空，没有数据");
        }
        for (int i = top; i >= 0; i--) {
            System.out.println(stack[i]);
        }
    }
}
