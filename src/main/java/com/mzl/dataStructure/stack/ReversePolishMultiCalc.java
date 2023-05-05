package com.mzl.dataStructure.stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * 逆波兰计算器完整版
 * 功能包括：
 * 1.支持+、-、*、/、（）
 * 2.多位数，支持小鼠
 * 3.兼容处理，过滤任何空白字符，包括空格、制表符、换页符
 */
public class ReversePolishMultiCalc {

    /**
     * 匹配 + - * / ( ) 运算符
     */
    static final String SYMBOL = "\\+|-|\\*|/|\\(|\\)";

    static final String LEFT = "(";
    static final String RIGHT = ")";
    static final String ADD = "+";
    static final String MINUS = "-";
    static final String TIMES = "*";
    static final String DIVISION = "/";

    /**
     * 加減 + -
     */
    static final int LEVEL_01 = 1;
    /**
     * 乘除 * /
     */
    static final int LEVEL_02 = 2;

    /**
     * 括号
     */
    static final int LEVEL_HIGH = Integer.MAX_VALUE;


    static Stack<String> stack = new Stack<>();
    static List<String> data = Collections.synchronizedList(new ArrayList<String>());

    /**
     * 去除所有空白符
     *
     * @param s
     * @return
     */
    public static String replaceAllBlank(String s) {
        // \\s+ 匹配任何空白字符，包括空格、制表符、换页符等等, 等价于[ \f\n\r\t\v]
        return s.replaceAll("\\s+", "");
    }

    /**
     * 判断是不是数字 int double long float
     *
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        // Pattern.compile函数，来实现对指定字符串的截取
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(s).matches();
    }

    /**
     * 判断是不是运算符
     *
     * @param s
     * @return
     */
    public static boolean isSymbol(String s) {
        return s.matches(SYMBOL);
    }

    /**
     * 匹配运算等级
     *
     * @param s
     * @return
     */
    public static int calcLevel(String s) {
        if ("+".equals(s) || "-".equals(s)) {
            return LEVEL_01;
        } else if ("*".equals(s) || "/".equals(s)) {
            return LEVEL_02;
        }
        return LEVEL_HIGH;
    }

    /**
     * 匹配（12.8 + (2 - 3.55)*4+10/5.0） => 12.8 2 3.55 - 4 * + 10 5.0 / +
     *  转成后缀表达式的list
     * @param s
     * @return
     * @throws Exception
     */
    public static List<String> doMatch(String s) throws Exception {
        if (s == null || "".equals(s.trim())) throw new RuntimeException("data is empty");
        if (!isNumber(s.charAt(0) + "")) throw new RuntimeException("data illeagle,start not with a number");
        s = replaceAllBlank(s);
        String each;
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            if (isSymbol(s.charAt(i) + "")) {
                System.out.println("符号");
                each = "";
            } else {

                System.out.println("数字");
            }
        }

        return null;
    }

    /**
     * 算出结果
     *
     * @param list
     * @return
     */
    public static Double doCalc(List<String> list) {
        return 0.0;
    }

    public static Double doTheMath(String numLeft, String numRight, String symbol) {
        Double result;
        switch (symbol) {
            case ADD:
                result = Double.valueOf(numLeft) + Double.valueOf(numRight);
                break;
            case MINUS:
                result = Double.valueOf(numLeft) - Double.valueOf(numRight);
                break;
            case TIMES:
                result = Double.valueOf(numLeft) * Double.valueOf(numRight);
                break;
            case DIVISION:
                result = Double.valueOf(numLeft) / Double.valueOf(numRight);
                break;
            default:
                result = null;
        }
        return result;
    }

    public static void main(String[] args) {
        //String math = "9+(3-1)*3+10/2";
        String math = "12.8 + (2 - 3.55)*4+10/5.0";
        try {
            doCalc(doMatch(math));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
