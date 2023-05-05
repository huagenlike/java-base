package com.mzl.designMode.iteratorModel;

import java.util.*;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description  - Iterator模式
 * @createTime 2022/11/03
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class iteratorDemo {

    public static void main(String[] args) {
        listItr();
//        demo();
//        prevAndNexItr();
//        setItr();
//        linkedListItr();
//        arrayListItr();
    }

    /**
     * 向后遍历 - list，不受容量限制
     **/
    public static void listItr() {
        BookShelfList bookShelf = new BookShelfList(4);
        bookShelf.appendBook(new Book("Around thr world in 80 days"));
        bookShelf.appendBook(new Book("bible"));
        bookShelf.appendBook(new Book("cinderella"));
        bookShelf.appendBook(new Book("daddy-long-legs"));

        Iterator it = bookShelf.iterator();

        while (it.hasNext()) {
            Book book = (Book) it.next();
            System.out.println(book.getName());
        }
    }

    /**
     * 向后遍历 - 使用数组，受数组容量限制
     **/
    public static void netItr() {
        BookShelf bookShelf = new BookShelf(4);
        bookShelf.appendBook(new Book("Around thr world in 80 days"));
        bookShelf.appendBook(new Book("bible"));
        bookShelf.appendBook(new Book("cinderella"));
        bookShelf.appendBook(new Book("daddy-long-legs"));

        Iterator it = bookShelf.iterator();

        while (it.hasNext()) {
            Book book = (Book) it.next();
            System.out.println(book.getName());
        }
    }

    /**
     * 向前或向后遍历
     **/
    public static void prevAndNexItr() {
        List<Integer> list = Arrays.asList(1,2,4);

        ListIterator<Integer> listItr = list.listIterator();

        while(listItr.hasNext()) {
            System.out.print(listItr.next());
        }

        System.out.println();

        while(listItr.hasPrevious()) {
            System.out.print(listItr.previous());
        }
    }

    /**
     * 字符串数组迭代
     **/
    public static void stringArrItr() {
        String[] ints = new String[] {"1", "2", "3", "4", "5"};
        Arrays.stream(ints).iterator().forEachRemaining(name -> System.out.println(name));
    }

    /**
     * set迭代
     **/
    public static void setItr() {
        Set<String> strSet = new HashSet<>();
        strSet.add("a");
        strSet.add("b");
        strSet.add("c");
        java.util.Iterator<String> iterator = strSet.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }

    /**
     * set迭代
     **/
    public static void linkedListItr() {
        List<String> linkedListItr = new LinkedList<>();
        linkedListItr.add("a");
        linkedListItr.add("b");
        linkedListItr.add("c");
        java.util.Iterator<String> iterator = linkedListItr.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }

    public static void arrayListItr() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        java.util.Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }

    public static void mapItr() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "aa");
        map.put("b", "bb");
        map.put("c", "cc");
    }

}
