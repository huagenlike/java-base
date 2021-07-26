package com.mzl.dataStructure.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ListTest
 * @description: Java集合详解--什么是List
 * @date 2021/7/26 16:48
 * List是继承于Collection接口，除了Collection通用的方法以外，扩展了部分只属于List的方法。
 */
public class ListTest {

    public static void main(String[] args) {

    }

    /**
     * 线程不安全
     * ArrayList是一个数组实现的列表，由于数据是存入数组中的，所以它的特点也和数组一样，查询很快，但是中间部分的插入和删除很慢。
     **/
    private static void arrayListTest() {
        int oldCapacity = 1024;
        System.out.println(oldCapacity >> 1);
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("lisi", 15));
        users.add(new User("zhangsan", 18));
        users.add(new User("wangwu", 20));
        users.add(new User("zhaoliu", 81));
        users.add(new User("lisi", 16));

        System.out.println(users);
        // 引用类型：比较的是地址是否相同
        boolean lisi = users.remove(new User("lisi", 16));
        System.out.println(users);
    }

    /**
     * Vector就是ArrayList的线程安全版，它的方法前都加了synchronized锁，其他实现逻辑都相同。
     * 如果对线程安全要求不高的话，可以选择ArrayList，毕竟synchronized也很耗性能
     **/
    private static void vectorTest() {
        Vector<User> users = new Vector<>();

    }

    /**
     * LinkedList还是一个双向链表。
     **/
    private static void linkedListTest() {
        LinkedList<User> users = new LinkedList<>();
    }
}

class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
