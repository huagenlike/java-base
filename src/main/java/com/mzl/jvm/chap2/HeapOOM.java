package com.mzl.jvm.chap2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihuagen
 * @version 1.0
 * @className: HeapOOM
 * @description: Java堆溢出
 * @date 2021/7/20 11:23
 * vm：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * -verbose:gc 在控制台输出GC情况
 * -Xms20M -Xmx20M -Xmn10M 初始堆大小20MB，最大堆大小20MB，不可扩展，其中10MB分配给新生代，剩下10MB分配给老年代
 * -XX:+PrintGCDetails 在控制台输出详细的GC情况
 * -XX:SurvivorRatio=8 决定了新生代中Eden区与一个Survivor区的空间比例是8:1
 *
 * 解决方向：
 * 要解决这个内存区域的异常，常规的处理方法是首先通过内存映像分析工具（如Eclipse Memory Analyzer）对Dump出来的堆转储快照进行分析。
 * 第一步首先应确认内存中导致OOM的对象是否是必要的，也就是要先分清楚到底是出现了内存泄漏（Memory Leak）还是内存溢出（Memory Overflow）。图2-5显示了使用Eclipse Memory Analyzer打开的堆转储快照文件。
 * 如果是内存泄漏，可进一步通过工具查看泄漏对象到GC Roots的引用链，找到泄漏对象是通过怎样的引用路径、与哪些GC Roots相关联，才导致垃圾收集器无法回收它们，根据泄漏对象的类型信息以及它到GC Roots引用链的信息，一般可以比较准确地定位到这些对象创建的位置，进而找出产生内 存泄漏的代码的具体位置。
 * 如果不是内存泄漏，换句话说就是内存中的对象确实都是必须存活的，那就应当检查Java虚拟机的堆参数（-Xmx与-Xms）设置，与机器的内存对比，看看是否还有向上调整的空间。
 * 再从代码上检查是否存在某些对象生命周期过长、持有状态时间过长、存储结构设计不合理等情况，尽量减少程序运行期的内存消耗。
 * 以上是处理Java堆内存问题的简略思路，处理这些问题所需要的知识、工具与经验是后面三章的主题，后面我们将会针对具体的虚拟机实现、具体的垃圾收集器和具体的案例来进行分析，这里就先暂不展开
 */
public class HeapOOM {
    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
