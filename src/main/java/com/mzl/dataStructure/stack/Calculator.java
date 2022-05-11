package com.mzl.dataStructure.stack;

/**
 * 栈实现综合计算器（中缀表达式）
 *  输入一个表达式，计算式：【7*2*2-5+1-5+3-3】
 * 中缀表达式就是常见的运算表达式，如（3+4）*5-6
 */
public class Calculator {

    public static void main(String[] args) {
        String expression = "17-2+3*(5+2/2*2)-5+1-15+13-23";// 15+17-5+13-23
        // 创建两个栈，数栈，一个符号栈
        ArrayStack2 numStack = new ArrayStack2(30);
        ArrayStack2 operStack = new ArrayStack2(30);
        int index = 0;
        int numLeft = 0;
        int numRight = 0;
        int res = 0;
        int oper = 0;
        char ch = ' ';
        String keepNum = "";
        while (true) {
            ch = expression.substring(index, index + 1).charAt(0);
            System.out.println("当前的字符（" + ch + "）：");

            if (ch == '(') {
                operStack.push(ch);
            } else if (ch == ')') {
                while (true) {
                    oper = operStack.pop();
                    if (oper == '(') {
                        break;
                    }
                    numRight = numStack.pop();
                    numLeft = numStack.pop();
                    res = operStack.cal(numLeft, numRight, oper);
                    numStack.push(res);
                }
            } else {
                // 判定是否是符号
                if (operStack.isOper(ch)) {
                    // 判定当前符号栈是否为空
                    if (operStack.isEmpty()) {
                        //如果为空直接入符号栈..
                        operStack.push(ch);
                    } else {
                        System.out.println("栈顶的符号（" + (char) operStack.peek() + "）：" + operStack.priority(operStack.peek()));
                        //如果符号栈有操作符，就进行比较,如果当前的操作符的优先级小于或者等于栈中的操作符,就需要从数栈中pop出两个数,
                        //在从符号栈中pop出一个符号，进行运算，将得到结果，入数栈，然后将当前的操作符入符号栈
                        if (operStack.priority(ch) <= operStack.priority(operStack.peek())) {
                            numRight = numStack.pop();
                            numLeft = numStack.pop();
                            oper = operStack.pop();
                            res = operStack.cal(numLeft, numRight, oper);
                            numStack.push(res);
                            operStack.push(ch);
                        } else {
                            operStack.push(ch);
                        }
                    }
                } else {
                    //处理多位数
                    keepNum += ch;

                    //如果ch已经是expression的最后一位，就直接入栈
                    if (index == expression.length() - 1) {
                        numStack.push(Integer.parseInt(keepNum));
                    } else {
                        // 如果当前字符的下一个字符为运算符号，则将拼接的数字压入数字栈，并且清空keepNum
                        if (operStack.isOper(expression.substring(index + 1, index + 2).charAt(0)) || expression.substring(index + 1, index + 2).charAt(0) == '(' || expression.substring(index + 1, index + 2).charAt(0) == ')') {
                            numStack.push(Integer.parseInt(keepNum));
                            keepNum = "";
                        }
                    }
                }
            }

            index++;
            if (index >= expression.length()) {
                break;
            }
        }

        while (true) {
            if (operStack.isEmpty()) {
                break;
            }
            oper = operStack.pop();
            numRight = numStack.pop();
            numLeft = numStack.pop();
            res = operStack.cal(numLeft, numRight, oper);
            numStack.push(res);
        }
        System.out.println("最后的值：" + numStack.pop());
    }
}

class ArrayStack2 {
    /**
     * 栈的大小
     */
    private int maxSize;
    /**
     * 数组，数组模拟栈，数据就放在该数组
     */
    private int[] stack;
    /**
     * top表示栈顶，初始化为-1
     */
    private int top = -1;

    //构造器
    public ArrayStack2(int maxSize) {
        this.maxSize = maxSize;
        stack = new int[this.maxSize];
    }

    //增加一个方法，可以返回当前栈顶的值, 但是不是真正的pop
    public int peek() {
        return stack[top];
    }

    //栈满
    public boolean isFull() {
        return top == maxSize - 1;
    }

    //栈空
    public boolean isEmpty() {
        return top == -1;
    }

    //入栈-push
    public void push(int value) {
        //先判断栈是否满
        if (isFull()) {
            System.out.println("栈满");
            return;
        }
        top++;
        stack[top] = value;
    }

    //出栈-pop, 将栈顶的数据返回
    public int pop() {
        //先判断栈是否空
        if (isEmpty()) {
            //抛出异常
            throw new RuntimeException("栈空，没有数据~");
        }
        int value = stack[top];
        top--;
        return value;
    }

    //显示栈的情况[遍历栈]， 遍历时，需要从栈顶开始显示数据
    public void list() {
        if (isEmpty()) {
            System.out.println("栈空，没有数据~~");
            return;
        }
        //需要从栈顶开始显示数据
        for (int i = top; i >= 0; i--) {
            System.out.printf("stack[%d]=%d\n", i, stack[i]);
        }
    }

    //返回运算符的优先级，优先级是程序员来确定, 优先级使用数字表示
    //数字越大，则优先级就越高.（假定只支持加减乘除）
    public int priority(int oper) {
        if (oper == '*' || oper == '/') {
            return 1;
        } else if (oper == '+' || oper == '-') {
            return 0;
        } else if (oper == ')') {
            return 0;
        }
        return -1;
    }

    //判断是不是一个运算符
    public boolean isOper(char val) {
        return val == '+' || val == '-' || val == '*' || val == '/';
    }

    //计算方法
    public int cal(int numLeft, int numRight, int oper) {
        int res = 0; // 颙与存放计算的结果
        switch (oper) {
            case '+':
                res = numLeft + numRight;
                break;
            case '-':
                res = numLeft - numRight;
                break;
            case '*':
                res = numLeft * numRight;
                break;
            case '/':
                res = numLeft / numRight;
                break;
            default:
                break;
        }
        return res;
    }
}
