package com.mzl.java8.chap2;

/**
 * @author may
 * @version 1.0
 * @className: MeaningOfThis
 * @description: TODO
 * @date 2021/4/23 14:46
 */
public class MeaningOfThis {
    public final int value = 4;
    public void doIt()
    {
        int value = 6;
        Runnable r = new Runnable(){
            public final int value = 5;
            @Override
            public void run(){
                int value = 10;
                // 因为this指的是包含它的Runnable，而不是外面的类MeaningOfThis
                System.out.println(this.value);
            }
        };
        r.run();
    }
    public static void main(String...args)
    {
        MeaningOfThis m = new MeaningOfThis();
        m.doIt(); // ???
    }
}
