# java8
    partitioningBy分隔/groupingBy分组
    maxBy取最大/minBy取最小
    averagingInt /averagingLong/averagingDouble取平均值
    collectingAndThen 此方法是在进行归纳动作结束之后，对归纳的结果进行二次处理。
    reduce 操作可以实现从Stream中生成一个值，其生成的值不是随意的，而是根据指定的计算模型。比如，之前提到count、min和max方法，因为常用而被纳入标准库中。事实上，这些方法都是reduce操作。

    CompletableFuture ：实现异步编程
    supplyAsync：有返回值
    whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
    whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
    thenCompose：将在与上游任务相同的线程上调用generateRequest()(如果上游任务已经完成，则调用该线程).
    thenComposeAsync：将在提供的执行程序(如果提供)上调用generateRequest()，否则将在默认的ForkJoinPool上调用.

    1、new Thread的弊端
        a. 每次new Thread新建对象性能差。
        b. 线程缺乏统一管理，可能无限制新建线程，相互之间竞争，及可能占用过多系统资源导致死机或oom。
        c. 缺乏更多功能，如定时执行、定期执行、线程中断。
    相比new Thread，Java提供的四种线程池的好处在于：
        a. 重用存在的线程，减少对象创建、消亡的开销，性能佳。
        b. 可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞。
        c. 提供定时执行、定期执行、单线程、并发数控制等功能。
    2、Java 线程池
        Java通过Executors提供四种线程池，分别为：
            newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
            newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
            newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
            newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
        如果线程池中线程的数量过多，最终它们会竞争稀缺的处理器和内存资源，浪费大量的时间在上下文切换上。反之，如果线程的数目过少，正如你的应用所面临的情况，处理器的一些核可能就无法充分利用。
        N threads = N CPU * U CPU * (1 + W/C)
            N CPU是处理器的核的数目，可以通过Runtime.getRuntime().availableProcessors()得到
            U CPU是期望的CPU利用率（该值应该介于0和1之间）
            W/C是等待时间与计算时间的比率
    对集合进行并行计算有两种方式：
        要么将其转化为并行流，利用map这样的操作开展工作，
        要么枚举出集合中的每一个元素，创建新的线程，在CompletableFuture内对其进行操作。
    并行——使用流还是CompletableFutures？
        如果你进行的是计算密集型的操作，并且没有I/O，那么推荐使用Stream接口，因为实现简单，同时效率也可能是最高的（如果所有的线程都是计算密集型的，那就没有必要创建比处理器核数更多的线程）。
        反之，如果你并行的工作单元还涉及等待I/O的操作（包括网络连接等待），那么使用CompletableFuture灵活性更好，你可以像前文讨论的那样，依据等待/计算，或者W/C的比率设定需要使用的线程数。这种情况不使用并行流的另一个原因是，处理流的流水线中如果发生I/O等待，流的延迟特性会让我们很难判断到底什么时候触发了等待。
    Java 8的 CompletableFuture API提供了名为thenCompose的方法，它就是专门为这一目的而设计的，thenCompose方法允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。

# java并发编程之美
    第1章 并发编程线程基础
        线程是进程中的一个实体，线程本身是不会独立存在的。
        进程是代码在数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，线程则是进程的一个执行路径，一个进程中至少有一个线程，进程中的多个线程共享进程的资源。
        操作系统在分配资源时是把资源分配给进程的，但是CPU资源比较特殊，它是被分配到线程的，因为真正要占用CPU运行的是线程，所以也说线程是CPU分配的基本单位。
        进程：是系统进行资源分配和调度的基本单位
            一个进程中有多个线程，多个线程共享进程的堆和方法区资源，但是每个线程有自己的程序计数器和栈区域。
        线程：是CPU分配的基本单位
            程序计数器是一块内存区域，用来记录线程当前要执行的指令地址。
