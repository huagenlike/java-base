package com.mzl.demo.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SingleTable {
    private Integer id;
    private String key1;
    private Integer key2;
    private String key3;
    private String keyPart1;
    private String keyPart2;
    private String keyPart3;
    private String commonField;


    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("a");
        list1.add("b");
        list1.add("c");
        List<String> list2 = new ArrayList<>();
        list2.add("aa");
        list2.add("bb");
        list2.add("cc");
        List<String> list3 = new ArrayList<>();
        list3.add("aaa");
        list3.add("bbb");
        list3.add("ccc");
        List<String> list4 = new ArrayList<>();
        list4.add("aaaa");
        list4.add("bbbb");
        list4.add("cccc");
        List<String> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);

        System.out.println(list);


    }
}
