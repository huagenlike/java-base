package com.mzl.jvm.chap3;

/**
 * @author lihuagen
 * @version 1.0
 * @className: TestDemo
 * @description: TODO
 * @date 2021/8/10 15:14
 * 什么是栈：限定仅在表头进行插入和删除操作的线性表。
 * 栈帧是栈的元素
 * 栈帧中由一个局部变量表存储数据。局部变量表存储了基础数据（boolean、byte、char、short、int、float、long、double）的局部变量（包括参数）、和对象的引用（String、数组、对象等）但是不存储对象的内容。局部变量表所需的内存空间在编译期间完成分配，在方法运行期间不会改变局部变量表的大小
 * 局部变量表的容量以变量槽（Variable Slot）为最小单位，每个变量槽最大存储32位的数据类型，对于64位的数据类型（long、double），jvm会分配两个连续的变量槽来存储。
 *      static 方法在第0个槽位，存的是参数
 *      非static 方法在第0个槽位，存储方法所属对象实例的引用
 *      为了尽可能的节省栈帧空间，局部变量表中的 Slot 是可以复用的。
 */
public class TestDemo {
    public static void main(String[] args){
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
            // 不会进行内存回收
            // 当执行 System.gc() 方法时，变量 placeholder 还在作用域范围之内，虚拟机是不会回收的，它还是“有效”的。
            // System.gc();
        }
        // 【第一次修改】，不会进行内存回收
        // 当运行到 System.gc() 方法时，变量 placeholder 的作用域已经失效了。它已经“无用”了，虚拟机会回收它所占用的内存了吧？
        // System.gc();

        // 【第二次修改】，会进行内存回收
        // 在 System.gc() 方法之前，加入 int a = 0，再执行方法，查看垃圾回收情况。
        // 发现 placeholder 变量占用的64M内存空间被回收了，如果不理解局部变量表的Slot复用，很难理解这种现象的。
        // 而 placeholder 变量能否被回收的关键就在于：局部变量表中的 Slot 是否还存有关于 placeholder 对象的引用。
        // 第一次修改中，限定了 placeholder 的作用域，但之后并没有任何对局部变量表的读写操作，placeholder 变量在局部变量表中占用的Slot没有被其它变量所复用，所以作为 GC Roots 一部分的局部变量表仍然保持着对它的关联。所以 placeholder 变量没有被回收。
        // 第二次修改后，运行到 int a = 0 时，已经超过了 placeholder 变量的作用域，此时 placeholder 在局部变量表中占用的Slot可以交给其他变量使用。而变量a正好复用了 placeholder 占用的 Slot，至此局部变量表中的 Slot 已经没有 placeholder 的引用了，虚拟机就回收了placeholder 占用的 64M 内存空间。
        int a = 0;
        System.gc();
    }
}
