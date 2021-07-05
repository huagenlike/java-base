package com.mzl.concurrentProgramming.chap4;

import java.io.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ExternalizableTest
 * @description: 序列化问题
 * @date 2021/7/5 15:19
 * 我们知道在Java中，对象的序列化可以通过实现两种接口来实现，若操作的是一个Serializable对象，则所有的序列化将会自动进行，若操作的是 一个Externalizable对象，则没有任何东西可以自动序列化，需要在writeExternal方法中进行手工指定所要序列化的变量，这与是否被transient修饰无关。因此第二个例子输出的是变量content初始化的内容，而不是null。
 */
public class ExternalizableTest implements Externalizable {
    private transient String content = "哈哈~我将会被序列化，不管我是是否被transient关键字修饰";

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(content);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        content = (String) in.readObject();
    }

    public static void main(String[] args) throws Exception {
        ExternalizableTest et = new ExternalizableTest();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File("ext0000")));
        out.writeObject(et);

        ObjectInput in = new ObjectInputStream(new FileInputStream(new File("ext0000")));
        ExternalizableTest et1 = (ExternalizableTest) in.readObject();
        System.out.println(et1.content);

        out.close();
        in.close();
    }
}
