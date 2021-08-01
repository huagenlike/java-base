package com.mzl.dataStructure.queue;

import java.util.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Set
 * @description: Set集合的详解
 * @date 2021/7/26 15:45
 * Set:注重独一无二的性质,该体系集合可以知道某物是否已近存在于集合中,不会存储重复的元素
 */
public class SetTest {
    public static void main(String[] args) {
//        setTest();
//        hashSetTest();
//        hashSetTest1();
//        treeSetTest();
//        treeSetComparableTest();
//        treeSetComparableTest1();
        linkedHashSetTest();
    }

    /**
     * Set 集合存和取的顺序不一致。
     * 用于存储无序(存入和取出的顺序不一定相同)元素，值不能重复。
     * 该集合中没有特有的方法，直接继承自Collection。
     * 案例：set集合添加元素并使用迭代器迭代元素。
     **/
    private static void setTest() {
        Set hs = new HashSet();
        hs.add("世界军事");
        hs.add("兵器知识");
        hs.add("舰船知识");
        hs.add("汉和防务");
        System.out.println(hs);
        // [舰船知识, 世界军事, 兵器知识, 汉和防务]
        Iterator it = hs.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * 哈希表边存放的是哈希值。HashSet存储元素的顺序并不是按照存入时的顺序（和List显然不同） 是按照哈希值来存的所以取数据也是按照哈希值取得。
     * 总结：
     *  元素的哈希值是通过元素的hashcode方法来获取的, HashSet首先判断两个元素的哈希值，如果哈希值一样，接着会比较equals方法 如果 equls结果为true ，HashSet就视为同一个元素。如果equals 为false就不是同一个元素。
     *  哈希值相同equals为false的元素是怎么存储呢,就是在同样的哈希值下顺延（可以认为哈希值相同的元素放在一个哈希桶中）。也就是哈希一样的存一列。
     * HashSet：通过hashCode值来确定元素在内存中的位置。一个hashCode位置上可以存放多个元素。
     * HashSet 和ArrayList集合都有判断元素是否相同的方法，
     *  boolean contains(Object o)
     *  HashSet使用hashCode和equals方法，ArrayList使用了equals方法
     **/
    private static void hashSetTest() {
        // Set 集合存和取的顺序不一致。
        Set hs = new HashSet();
        hs.add("世界军事");
        hs.add("兵器知识");
        hs.add("舰船知识");
        hs.add("汉和防务");

        // 返回此 set 中的元素的数量
        System.out.println(hs.size()); // 4

        // 如果此 set 尚未包含指定元素，则返回 true
        boolean add = hs.add("世界军事"); // false
        System.out.println(add);

        // 返回此 set 中的元素的数量
        System.out.println(hs.size());// 4
        Iterator it = hs.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * 使用HashSet存储自定义对象，并尝试添加重复对象（对象的重复的判定）
     **/
    private static void hashSetTest1() {
        HashSet hs = new HashSet();
        hs.add(new Person("jack", 20));
        hs.add(new Person("rose", 20));
        hs.add(new Person("hmm", 20));
        hs.add(new Person("lilei", 20));
        hs.add(new Person("jack", 20));

        Iterator it = hs.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            System.out.println(next);
        }
    }

    /**
     * 使用TreeSet集合存储字符串元素，并遍历
     * TreeSet  红-黑树的数据结构，默认对元素进行自然排序（String）。如果在比较的时候两个对象返回值为0，那么元素重复。
     * 既然TreeSet可以自然排序,那么TreeSet必定是有排序规则的。
     *  1:让存入的元素自定义比较规则。
     *  2:给TreeSet指定排序规则。
     * 方式一：元素自身具备比较性
     *  元素自身具备比较性，需要元素实现Comparable接口，重写compareTo方法，也就是让元素自身具备比较性，这种方式叫做元素的自然排序也叫做默认排序。
     * 方式二：容器具备比较性
     *  当元素自身不具备比较性，或者自身具备的比较性不是所需要的。那么此时可以让容器自身具备。需要定义一个类实现接口Comparator，重写compare方法，并将该接口的子类实例对象作为参数传递给TreeMap集合的构造方法。
     **/
    private static void treeSetTest() {
        TreeSet ts = new TreeSet();
        ts.add("ccc");
        ts.add("aaa");
        ts.add("ddd");
        ts.add("bbb");

        // [aaa, bbb, ccc, ddd]
        System.out.println(ts);
    }

    /**
     * 一，让元素自身具备比较性。
     * 也就是元素需要实现Comparable接口，覆盖compareTo 方法。
     * 这种方式也作为元素的自然排序，也可称为默认排序。
     * 年龄按照搜要条件，年龄相同再比姓名。
     **/
    private static void treeSetComparableTest() {
        TreeSet ts = new TreeSet();
        ts.add(new Person("aa", 20, "男"));
        ts.add(new Person("bb", 18, "女"));
        ts.add(new Person("cc", 17, "男"));
        ts.add(new Person("dd", 17, "女"));
        ts.add(new Person("dd", 15, "女"));
        ts.add(new Person("dd", 15, "女"));


        System.out.println(ts);
        // 5
        System.out.println(ts.size());
    }

    /**
     * 二，让容器自身具备比较性，自定义比较器。
     * 需求：当元素自身不具备比较性，或者元素自身具备的比较性不是所需的。
     * 那么这时只能让容器自身具备。
     * 定义一个类实现Comparator 接口，覆盖compare方法。
     * 并将该接口的子类对象作为参数传递给TreeSet集合的构造函数。
     * 当Comparable比较方式，及Comparator比较方式同时存在，以Comparator比较方式为主。
     **/
    private static void treeSetComparableTest1 () {
        TreeSet ts = new TreeSet(new MyComparator());
        ts.add(new Book("think in java", 100));
        ts.add(new Book("java 核心技术", 75));
        ts.add(new Book("现代操作系统", 50));
        ts.add(new Book("java就业教程", 35));
        ts.add(new Book("think in java", 100));
        ts.add(new Book("ccc in java", 100));

        System.out.println(ts);
    }

    /**
     * 会保存插入的顺序。
     * 看到array，就要想到角标。
     * 看到link，就要想到first，last。
     * 看到hash，就要想到hashCode,equals.
     * 看到tree，就要想到两个接口。Comparable，Comparator。
     **/
    private static void linkedHashSetTest() {
        LinkedHashSet<String> strings = new LinkedHashSet<>();
        strings.add("c");
        strings.add("a");
        strings.add("b");
        strings.add("d");
        strings.add("e");
        System.out.println(strings);
    }
}


class Person implements Comparable {
    private String name;
    private int age;
    private String gender;

    Person() {

    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode:" + this.name);
        return this.name.hashCode() + age * 37;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println(this + "---equals---" + obj);
        if (obj instanceof Person) {
            Person p = (Person) obj;
            return this.name.equals(p.name) && this.age == p.age;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {

        return "Person@name:" + this.name + " age:" + this.age;
    }

    @Override
    public int compareTo(Object obj) {

        Person p = (Person) obj;
        System.out.println(this+" compareTo:"+p);
        if (this.age > p.age) {
            return 1;
        }
        if (this.age < p.age) {
            return -1;
        }
        return this.name.compareTo(p.name);
    }

}


class MyComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Book b1 = (Book) o1;
        Book b2 = (Book) o2;
        System.out.println(b1+" comparator "+b2);
        if (b1.getPrice() > b2.getPrice()) {
            return 1;
        }
        if (b1.getPrice() < b2.getPrice()) {
            return -1;
        }
        return b1.getName().compareTo(b2.getName());
    }

}

class Book {
    private String name;
    private double price;

    public Book() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Book(String name, double price) {

        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book [name=" + name + ", price=" + price + "]";
    }

}
