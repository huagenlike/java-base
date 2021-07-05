package com.mzl.concurrentProgramming.chap4;

import java.io.*;
import java.util.Date;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TransientTest
 * @description: transient 的使用
 * @date 2021/7/5 15:13
 * 这就说明了一旦变量被 transient 修饰，变量将不再是对象持久化的一部分，该变量内容在序列化后无法获得访问。
 */
public class TransientTest implements Serializable {
    private static final long serialVersionUID = -5836283489677344417L;
    private int classValue = 10;
    private transient Date date = new Date();
    private transient static int staticValue = 10;

    public static void main(String[] args) throws Exception {
        TransientTest m = new TransientTest();
        m.classValue = 11;
        TransientTest.staticValue = 11;
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("0xjh000")));
        out.writeObject(m);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("0xjh000")));
        TransientTest m1 = (TransientTest) in.readObject();
        in.close();

        System.out.println(m1.classValue);
        System.out.println((m1.date == null ? "date is null" : "date is not null"));
    }
}
