package com.mzl.performanceTuning.chap1;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lihuagen
 * @version 1.0
 * @className: StringTest
 * @description: 字符串和正则表达式调优
 * @date 2021/11/3 15:19
 */
public class StringTest {
    public static void main(String[] args) throws Exception {
        demo();
//        demo3();
//        demo4();
    }

    public static void demo() {
        List<Integer> list = new ArrayList<>();
        String str = "abcde";
        for (int i = 0; i < 5000000; i++) {
            str += i;
            list.add(i);
        }
    }

    public static void equalsTest() {
        // 此种方式：JVM 首先会检查该对象是否在字符串常量池中，如果在，就返回该对象引用，否则新的字符串将在常量池中被创建
        String str1 = "abc";
        // 此种方式：首先在编译类文件时，"abc"常量字符串将会放入到常量结构中，在类加载时，“abc"将会在常量池中创建；其次，在调用 new 时，JVM 命令将会调用 String 的构造函数，同时引用常量池中的"abc” 字符串，在堆内存中创建一个 String 对象；最后，str 将引用 String 对象。
        String str2 = new String("abc");
        String str3 = str2.intern();

        System.out.println(str1 == str2);
        System.out.println(str2 == str3);
        System.out.println(str1 == str3);
    }

    /**
     * IllegalArgumentException - 如果指定对象不是声明底层字段（或者其子类或实现者）的类或接口的实例，或者解包转换失败。
     * 因为JVM在编译时期, 就把final类型的String进行了优化, 在编译时期就会把String处理成常量。所以无法修改类似String str = "123456"的值。
     **/
    public static void demo1() throws Exception {
        // 改变String的值，但hashcode不改变
        String str = "abc";
        System.out.println(str.hashCode());

        Field field = str.getClass().getDeclaredField("value");
        field.setAccessible(true);
        field.set(str, new char[] {'1', '2', '3', '4'});
        System.out.println(str);
        System.out.println(str.hashCode());

        // String 做字符串拼接，效率低
        String str1 = "abcdef";
        long startTime = System.currentTimeMillis();
        for(int i=0; i<10000; i++) {
            str1 = str1 + i;
        }
        System.err.println(System.currentTimeMillis() - startTime);

        // （优化）StringBuilder 做字符串拼接，提升效率
        StringBuilder str2 = new StringBuilder("abcdef");
        startTime = System.currentTimeMillis();
        for(int i=0; i<10000; i++) {
            str2.append(i);
        }
        System.err.println(System.currentTimeMillis() - startTime);

        /*String str01 = "aaa";
        String str02 = "aaa";
        String str03 = "bbbb";
        System.out.println(str01 + "/" + str02);
        System.out.println(str01.hashCode() + "/" + str02.hashCode() + "/" + str03.hashCode());
        Field field = str02.getClass().getDeclaredField("value");
        field.setAccessible(true);
        field.set(str02, new char[]{'b', 'b', 'b', 'b'});
        System.out.println(str01 + "/" + str02);
        *//* jdk7以后已不包含 private final int count;
            field = str02.getClass().getDeclaredField("count");
            field.setAccessible(true);
            field.set(str02, 4);*//*

        System.out.println(str01.hashCode() + "/" + str02.hashCode() + "/" + str03.hashCode());
        System.out.println("---------------------------");
        field = str02.getClass().getDeclaredField("hash");
        field.setAccessible(true);
        field.set(str02, 0);
        System.out.println(str01.hashCode() + "/" + str02.hashCode() + "/" + str03.hashCode());
        System.out.println(str01 == str02);
        System.out.println(str02 == str03);*/
    }

    public static void demo2() {
        String text = "abbc";
        //String regex = "ab{1,3}+bc";
        String regex = "ab{1,3}+c";
        String[] split = text.split(regex);
        System.out.println(Arrays.asList(split));
    }

    /**
     * 减少捕获嵌套
     * 捕获组是指把正则表达式中，子表达式匹配的内容保存到以数字编号或显式命名的数组中，方便后面引用。一般一个 () 就是一个捕获组，捕获组可以进行嵌套。
     * 非捕获组则是指参与匹配却不进行分组编号的捕获组，其表达式一般由（?:exp）组成。
     * 在正则表达式中，每个捕获组都有一个编号，编号 0 代表整个匹配到的内容。
     **/
    public static void demo3() {
        String text = "<input high=\"20\" weight=\"70\">test</input>";
        String reg = "(<input.*?>)(.*?)(</input>)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(text);
        while(m.find()) {
            System.out.println(m.group(0));//整个匹配到的内容
            System.out.println(m.group(1));//(<input.*?>)
            System.out.println(m.group(2));//(.*?)
            System.out.println(m.group(3));//(</input>)
        }
    }

    /**
     * 减少捕获嵌套（优化）
     * 如果你并不需要获取某一个分组内的文本，那么就使用非捕获分组。例如，使用“(?:X)”代替“(X)
     * 减少不需要获取的分组，可以提高正则表达式的性能。
     **/
    public static void demo4() {
        String text = "<input high=\"20\" weight=\"70\">test</input>";
        String reg = "(?:<input.*?>)(.*?)(?:</input>)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(text);
        while(m.find()) {
            System.out.println(m.group(0));//整个匹配到的内容
            System.out.println(m.group(1));//(.*?)
        }
    }
}
