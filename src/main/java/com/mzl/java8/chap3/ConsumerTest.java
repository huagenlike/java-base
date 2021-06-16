package com.mzl.java8.chap3;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ConsumerTest
 * @description: TODO
 * @date 2021/6/16 16:10
 * Consumer的作用顾名思义,是给定义一个参数,对其进行(消费)处理,处理的方式可以是任意操作.
 */
public class ConsumerTest {
    public static void main(String[] args) {

        List<Student> list = new ArrayList<>();

        Consumer<Student> consumer = x -> {

            list.add(x);
        };

        Student studentEntity = new Student(15,"zhangsan");
        Student studentEntity1 = new Student(16,"lisi");
        Student studentEntity2 = new Student(17,"wangwu");
        Student studentEntity3 = new Student(18,"zhaoliu");

        Stream.of(studentEntity,studentEntity1,studentEntity2,studentEntity3).forEach(consumer);

        list.stream().forEach(System.out::println);
    }

    /**
     * @param stringValue 待操作的值
     * @param consumer 操作函数
     *
     * 像我这样写是毫无意义的，才去百度了一下大神的简书  https://www.jianshu.com/p/0b955173045e
     */
    public static void testConsumer(String stringValue,Consumer<String> consumer) {
        consumer.accept(stringValue);
    }

    /**
     * @Author lhg
     * @Description
     * @Date 16:19 2021/6/16
     * @Param []
     * @return void
     * 比如将给定的一批用户里面的名称为"lisi"且年龄大于22岁的用户都给打包起来
     **/
    public static void demo() {
        List<Person1> lisiList = new ArrayList<>();
        Consumer <Person1> consumer  =  x -> {
            if (x.getName().equals("lisi")){
                lisiList.add(x);
            }
        };

        consumer = consumer.andThen(
                x -> lisiList.removeIf(y -> y.getAge() < 23)
        );

        Stream.of(
                new Person1(21,"zhangsan"),
                new Person1(22,"lisi"),
                new Person1(23,"wangwu"),
                new Person1(24,"wangwu"),
                new Person1(23,"lisi"),
                new Person1(26,"lisi"),
                new Person1(26,"zhangsan")
        ).forEach(consumer);

        System.out.println(JSON.toJSONString(lisiList));
    }
}

class Person1 {
    private Integer age;
    private String name;

    public Person1() {
    }

    public Person1(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Data
class Student {
    private Integer id;
    private String name;

    public Student() {
    }

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
