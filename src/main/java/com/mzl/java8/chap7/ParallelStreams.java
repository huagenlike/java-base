package com.mzl.java8.chap7;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 并行流
 */
public class ParallelStreams {

    /**
     * 迭代获取总数
     */
    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 0; i <= n; i++) {
            result += i;
        }
        return result;
    }

    /**
     * 与上面的迭代等价
     * 接受数字n作为参数，并返回从1到给定参数的所有数字的和。
     */
    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
    }

    /**
     * 上面方法的并行流方法
     * 把流转换成并行流，从而让前面的函数归约过程（也就是求和）并行运行——对顺序流调用parallel方法
     */
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
    }

    /**
     * LongStream.rangeClosed的方法。这个方法与iterate相比有两个优点。
     *  LongStream.rangeClosed直接产生原始类型的long数字，没有装箱拆箱的开销。
     *  LongStream.rangeClosed会生成数字范围，很容易拆分为独立的小块。例如，范围1~20可分为1~5、6~10、11~15和16~20。
     * 因为数值流避免了非针对性流那些没必要的自动装箱和拆箱操作。由此可见，选择适当的数据结构往往比并行化算法更重要。
     */
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
    }

    /**
     * TODO （最优方法）
     * 使用正确的数据结构然后使其并行工作能够保证最佳的性能
     * 并行化过程本身需要对流做递归划分，把每个子流的归纳操作分配到不同的线程，然后把这些操作的结果合并成一个值。
     * 但在多个内核之间移动数据的代价也可能比你想的要大，所以很重要的一点是要保证在内核中并行执行工作的时间比在内核之间传输数据的时间长。
     * 总而言之，很多情况下不可能或不方便并行化。然而，在使用并行Stream加速代码之前，你必须确保用得对；如果结果错了，算得快就毫无意义了。让我们来看一个常见的陷阱。
     */
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
    }

    /**
     * 错用并行流而产生错误的首要原因，就是使用的算法改变了某些共享状态。下面是另一种实现对前 n 个自然数求和的方法，但这会改变一个共享累加器：
     * 那这种代码又有什么问题呢？
     * 因为它在本质上就是顺序的。每次访问total都会出现数据竞争。如果你尝试用同步来修复，那就完全失去并行的意义了。为了说明这一点，让我们试着把Stream变成并行的：
     */
    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    /**
     * TODO （并行陷阱）
     * 这回方法的性能无关紧要了，唯一要紧的是每次执行都会返回不同的结果。
     * 这是由于多个线程在同时访问累加器，执行total += value，而这一句虽然看似简单，却不是一个原子操作。
     * 问题的根源在于，forEach中调用的方法有副作用，它会改变多个线程共享的对象的可变状态。要是你想用并行Stream又不想引发类似的意外，就必须避免这种情况。
     */
    public static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    public static class Accumulator {
        private long total = 0;

        public void add(long value) {
            total += value;
        }
    }
}
