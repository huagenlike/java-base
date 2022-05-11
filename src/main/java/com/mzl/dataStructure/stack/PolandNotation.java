package com.mzl.dataStructure.stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * 逆波兰计算器
 */
public class PolandNotation {

    /**
     * 完成将一个中缀表达式转成后缀表达式的功能
     * 1. 1+((2+3)×4)-5 => 转成  1 2 3 + 4 × + 5 –
     * 2. 因为直接对str 进行操作，不方便，因此 先将  "1+((2+3)×4)-5" =》 中缀的表达式对应的List
     * 即 "1+((2+3)×4)-5" => ArrayList [1,+,(,(,2,+,3,),*,4,),-,5]
     * 3. 将得到的中缀表达式对应的List => 后缀表达式对应的List
     * 即 ArrayList [1,+,(,(,2,+,3,),*,4,),-,5]  =》 ArrayList [1,2,3,+,4,*,+,5,–]
     *
     * @param args
     */
    public static void main(String[] args) {
        //完成将一个中缀表达式转成后缀表达式的功能
       /* String expression = "1+((2+3)*4)-5";//注意表达式
        List<String> infixExpressionList = toInfixExpressionList(expression);
        System.out.println("中缀表达式对应的List=" + infixExpressionList); // ArrayList [1,+,(,(,2,+,3,),*,4,),-,5]
        List<String> suffixExpreesionList = parseSuffixExpreesionList(infixExpressionList);
        System.out.println("后缀表达式对应的List" + suffixExpreesionList); //ArrayList [1,2,3,+,4,*,+,5,–]

        System.out.printf("expression=%d", calculate(suffixExpreesionList)); // ?*/
        //先定义给逆波兰表达式
        //(30+4)×5-6  => 30 4 + 5 × 6 - => 164
        // 4 * 5 - 8 + 60 + 8 / 2 => 4 5 * 8 - 60 + 8 2 / +
        //测试
        //说明为了方便，逆波兰表达式 的数字和符号使用空格隔开
        //String suffixExpression = "30 4 + 5 * 6 -";

        String expression = "4*5-8+60+8/2";//注意表达式
        List<String> infixExpressionList = toInfixExpressionList(expression);
        System.out.println(infixExpressionList);
        /* String suffixExpression = "4 5 * 8 - 60 + 8 2 / +"; // 76
        //思路
        //1. 先将 "3 4 + 5 × 6 - " => 放到ArrayList中
        //2. 将 ArrayList 传递给一个方法，遍历 ArrayList 配合栈 完成计算

        List<String> list = getListString(suffixExpression);
        System.out.println("rpnList=" + list);
        int res = calculate(list);
        System.out.println("计算的结果是=" + res);*/

    }

    /**
     * 中缀表达式转成对应的List
     *
     * @param s
     * @return
     */
    private static List<String> toInfixExpressionList(String s) {
        List<String> list = new ArrayList<>();
        int index = 0;
        String keepNum;
        char c;
        do {
            c = s.charAt(index);
            if (c < 48 || c > 57) {
                list.add("" + c);
                index++;
            } else {
                keepNum = "";
                while (index < s.length() && c >= 48 && c <=57) {
                    keepNum += c;
                    index++;
                    if (index < s.length()) {
                        c = s.charAt(index);
                    }
                }
                list.add(keepNum);
            }
        } while (index < s.length());
        return list;
    }

    /**
     * 将得到的中缀表达式对应的List => 后缀表达式对应的List
     * 即 ArrayList [1,+,(,(,2,+,3,),*,4,),-,5]  =》 ArrayList [1,2,3,+,4,*,+,5,–]
     *
     * @param ls
     * @return
     */
    private static List<String> parseSuffixExpreesionList(List<String> ls) {
        return null;
    }

    /**
     * 将一个逆波兰表达式， 依次将数据和运算符 放入到 ArrayList中
     *
     * @param suffixExpression
     * @return
     */
    private static List<String> getListString(String suffixExpression) {
        String[] split = suffixExpression.split(" ");
        return new ArrayList<>(Arrays.asList(split));
    }

    /**
     * 完成对逆波兰表达式的运算
     *
     * @param ls
     * @return
     */
    private static int calculate(List<String> ls) {
        Stack<String> stack = new Stack<>();
        for (String item : ls) {
            if (item.matches("\\d+")) {
                stack.push(item);
            } else {
                int numRight = Integer.parseInt(stack.pop());
                int numLeft = Integer.parseInt(stack.pop());
                int res = 0;
                switch (item) {
                    case "+":
                        res = numLeft + numRight;
                        break;
                    case "-":
                        res = numLeft - numRight;
                        break;
                    case "*":
                        res = numLeft * numRight;
                        break;
                    case "/":
                        res = numLeft / numRight;
                        break;
                    default:
                        throw new RuntimeException("运算符有误");
                }
                stack.push(res + "");
            }
        }
        // 最后保留的就是运算结果
        return Integer.parseInt(stack.pop());
    }

}

/**
 * Operation 可以返回一个运算符 对应的优先级
 */
class Operation {
    private static int ADD = 1;
    private static int SUB = 1;
    private static int MUL = 2;
    private static int DIV = 2;

    //写一个方法，返回对应的优先级数字
    public static int getValue(String operation) {
        int result = 0;
        switch (operation) {
            case "+":
                result = ADD;
                break;
            case "-":
                result = SUB;
                break;
            case "*":
                result = MUL;
                break;
            case "/":
                result = DIV;
                break;
            default:
                System.out.println("不存在该运算符" + operation);
                break;
        }
        return result;
    }
}
