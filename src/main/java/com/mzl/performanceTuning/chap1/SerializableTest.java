package com.mzl.performanceTuning.chap1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lihuagen
 * @version 1.0
 * @className: SerializableTest
 * @description: 网络通信优化之序列化优化
 * @date 2021/11/5 9:12
 */
public class SerializableTest {

    public static void main(String[] args) {
        demo4();
        demo5();
    }

    public static void demo1() {
        Set root = new HashSet();
        Set s1 = root;
        Set s2 = new HashSet();
        for (int i = 0; i < 100; i++) {
            Set t1 = new HashSet();
            Set t2 = new HashSet();
            //使t2不等于t1
            t1.add("foo");
            s1.add(t1);
            s1.add(t2);
            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }
    }

    /**
     * 序列化后的流太大
     * 序列化后的二进制数组越大，占用的存储空间就越多，存储硬件的成本就越高。如果我们是进行网络传输，则占用的带宽就更多，这时就会影响到系统的吞吐量。
     * Java 序列后的流会变大，最终会影响到系统的吞吐量。
     **/
    public static void demo2() {

        User user = new User();
        user.setUserName("test");
        user.setPassword("test");

        ByteArrayOutputStream os =new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] testByte = os.toByteArray();
        System.out.print("ObjectOutputStream 字节编码长度：" + testByte.length + "\n");
    }

    /**
     * Java 序列化实现的二进制编码完成的二进制数组大小，比 ByteBuffer 实现的二进制编码完成的二进制数组大小要大上几倍。
     **/
    public static void demo3() {
        System.out.println("-----------------------------");
        User user = new User();
        user.setUserName("test");
        user.setPassword("test");

        ByteBuffer byteBuffer = ByteBuffer.allocate( 2048);

        byte[] userName = user.getUserName().getBytes();
        byte[] password = user.getPassword().getBytes();
        byteBuffer.putInt(userName.length);
        byteBuffer.put(userName);
        byteBuffer.putInt(password.length);
        byteBuffer.put(password);

        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        System.out.print("ByteBuffer 字节编码长度：" + bytes.length+ "\n");

    }

    /**
     * 序列化性能太差
     * 序列化的速度也是体现序列化性能的重要指标，如果序列化的速度慢，就会影响网络通信的效率，从而增加系统的响应时间。
     * Java 序列化中的编码耗时要比 ByteBuffer 长很多
     **/
    public static void demo4() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("test");

        long startTime = System.currentTimeMillis();

        for(int i=0; i<1000; i++) {
            ByteArrayOutputStream os =new ByteArrayOutputStream();
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(os);
                out.writeObject(user);
                out.flush();
                out.close();
                byte[] testByte = os.toByteArray();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.print("ObjectOutputStream 序列化时间：" + (endTime - startTime) + "\n");
    }

    /**
     * ByteBuffer 序列化中的编码耗时要比 Java 的快很多
     **/
    public static void demo5() {
        User user = new User();
        user.setUserName("test");
        user.setPassword("test");
        long startTime1 = System.currentTimeMillis();
        for(int i=0; i<1000; i++) {
            ByteBuffer byteBuffer = ByteBuffer.allocate( 2048);

            byte[] userName = user.getUserName().getBytes();
            byte[] password = user.getPassword().getBytes();
            byteBuffer.putInt(userName.length);
            byteBuffer.put(userName);
            byteBuffer.putInt(password.length);
            byteBuffer.put(password);

            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
        }
        long endTime1 = System.currentTimeMillis();
        System.out.print("ByteBuffer 序列化时间：" + (endTime1 - startTime1)+ "\n");
    }

}

class User implements Serializable {

    String userName;
    String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
