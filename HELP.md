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
## 第1章 并发编程线程基础
    线程是进程中的一个实体，线程本身是不会独立存在的。
    进程是代码在数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，线程则是进程的一个执行路径，一个进程中至少有一个线程，进程中的多个线程共享进程的资源。
    操作系统在分配资源时是把资源分配给进程的，但是CPU资源比较特殊，它是被分配到线程的，因为真正要占用CPU运行的是线程，所以也说线程是CPU分配的基本单位。
    进程：是系统进行资源分配和调度的基本单位
        一个进程中有多个线程，多个线程共享进程的堆和方法区资源，但是每个线程有自己的程序计数器和栈区域。
    线程：是CPU分配的基本单位
        程序计数器是一块内存区域，用来记录线程当前要执行的指令地址。
    sleep与yield方法的区别在于：
        当线程调用sleep方法时调用线程会被阻塞挂起指定的时间，在这期间线程调度器不会去调度该线程。
        而调用yield方法时，线程只是让出自己剩余的时间片，并没有被阻塞挂起，而是处于就绪状态，线程调度器下一次调度时就有可能调度到当前线程执行。
    线程上下文切换时机有：当前线程的 CPU 时间片使用完处于就绪状态时，当前线程被其他线程中断时。
    线程通常都有五种状态：创建、就绪、运行、阻塞和死亡。
        第一是创建状态。在生成线程对象，并没有调用该对象的start方法，这是线程处于创建状态。
        第二是就绪状态。当调用了线程对象的start方法之后，该线程就进入了就绪状态，但是此时线程调度程序还没有把该线程设置为当前线程，此时处于就绪状态。在线程运行之后，从等待或者睡眠中回来之后，也会处于就绪状态。
        第三是运行状态。线程调度程序将处于就绪状态的线程设置为当前线程，此时线程就进入了运行状态，开始运行run函数当中的代码。
        第四是阻塞状态。线程正在运行的时候，被暂停，通常是为了等待某个时间的发生(比如说某项资源就绪)之后再继续运行。sleep,suspend，wait等方法都可以导致线程阻塞。
        第五是死亡状态。如果一个线程的run方法执行结束或者调用stop方法后，该线程就会死亡。对于已经死亡的线程，无法再使用start方法令其进入就绪。
    子线程中，主线程中断，会抛出 InterruptedException 异常，子线程会继续运行，需要子线程中断（thread.interrupt();）
    主线程中，子线程中断，子线程会抛出 InterruptedException 异常。
## 第2章 并发编程的其他基础知识
    并发和并行的概念
        并发：指同一个时间段内多个任务同时都在执行，并且都没有执行结束
        并行：在单位时间内多个任务同时执行
    共享资源：资源被多个线程所持有或者多个线程都可以去访问该资源。
    线程操作共享变量时，首先从主内存复制共享变量到自己的工作内存，然后对工作内存里的变量仅从处理，处理完后将变量值更新到主内存。    
    synchronized 块时java提供的一种原子性内置锁，也叫做监视器锁。是排他锁，其他线程必须等待该线程释放锁后才能获取该锁。
        当获取锁后会清空锁块内本地内存中将会被用到的共享变量，在使用这些共享变量时从主内存进行加载，在释放锁时将本地内存中修改的共享变量刷新到主内存。
        实现了原子性操作
        锁升级：偏向锁，自旋锁（默认自选10次），重量级锁
    volatile（保证了可见性和有序性）
        当一个变量被声明为volatile时，线程再写入变量时不会吧值缓存在寄存器或者其他地方，而是会把值刷新会主内存。其他线程读取该共享变量时，会从主内存重新获取最新值。
        禁止进行指令重排序。（实现有序性）
        只能保证对单次读/写的原子性。i++ 这种操作不能保证原子性。
        虽然提供了可见性保证，但并不保证操作的原子性。
    CAS 
        ABA问题：JDK中的AtomicStampedReference类给每个变量的状态值都配备了一个时间戳，从而避免了ABA问题的产生
    指令重排序
        java内存模型允许编译器和处理器对指令重排序以提高运行性能，并且只会对不存在数据依赖型的指令重排序。（不影响最终结果的，就可能被重排序）
    伪共享
        在Cache内部是按行存储的，其中每一行成为Cache行。Cache行是Cache与主内存进行数据交换的单位，Cache行的大小一般为2的幂次数字节。
        多个线程同时修改一个缓存行里面的多个变量时，由于同时只能有一个线程操作缓存行，所以相比将每个变量放到一个缓存行，性能会有所下降，这就是伪共享。
        CPU访问变量，不在Cache的话，会把该变量所在内存区域的一个Cache行大小的内存复制到Cache中。
    锁的概念
        悲观锁
            是指对数据的修改持保守态度，认为数据很容易就会被其他线程修改，所以在数据被处理（读和写）前先对数据进行加锁。
        乐观锁
             则认为数据在一般情况下不会造成冲突，所以在访问数据前不会加锁，而是在修改数据时对数据进行检查（通常是数据的版本，一般是version字段）而进一步决定后续步骤。如果数据没有冲突，则正常修改；如果数据版本产生了冲突，可以什么都不做，也可以重试。
        公平锁
            是指多个线程按照申请锁的顺序来获取锁。（先确认是否有等待线程，如果后，则到等待序列排队）
        非公平锁
            是指多个线程获取锁的顺序并不是按照申请锁的顺序，有可能后申请的线程比先申请的线程优先获取锁。有可能，会造成优先级反转或者饥饿现象。（一来就抢锁资源）
        独占锁（悲观锁）
            同一时间只有一个线程能到的该锁。如：ReentrantLock
        共享锁（乐观锁）
            可以同时由多个线程持有。如：ReadWriteLock 读写锁，它允许一个资源可以被多个线程同时进行读操作。
        可重入锁
            当一个线程已经获取一个对象的锁时，如果在释放该锁之前可以再次获取该对象的锁，那么则为可重入锁 。
        自旋锁
            是当前线程在获取锁时，如果发现锁已经被其他线程占有，它不会马上阻塞自己，在不放弃 CPU 使用权的情况下，多次尝试获取（默认是 10 次），很有可能在后面几次的尝试中其他线程已经释放了该锁。如果尝试指定次数以后仍然没有获取到锁才会阻塞。
        分段锁
            分段锁其实是一种锁的设计，并不是具体的一种锁，对于ConcurrentHashMap而言，其并发的实现就是通过分段锁的形式来实现高效的并发操作。
# 第3章 Java并发包中ThreadLocalRandom类原理剖析
    Random
        是线程安全的，如果要在线程环境中的话就有可能产生性能瓶颈。
        nextInt()方法调用了next(int bits)，next(int bits)内部使用了AtomicLong，并调用了它的compareAndSet方法来保证线程安全性。所以这个是一个线程安全的方法。
        使用CAS操作。当多个线程获取到相同的种子，在 while (!seed.compareAndSet(oldseed, nextseed)); 的时候可以保证只有一个线程更新老的种子为新的，其他线程会继续循环去重新获取种子，这样就保证随机数的随机性。
        但是，有使用CAS操作，会导致多个线程的进行自旋重试，这就会降低并发性能，所以ThreadLocalRandom应运而生。
    ThreadLocalRandom
        可以看出ThreadLocalRandom类继承了Random，并重写了nextint方法，在ThreadlocalRandom没有继承自Random的原子种子变量，在ThreadLocalRandom并没有具体的种子，具体的种子存放到具体调用线程的threadlocalrandomSeed变量里面，就和ThreadLocal一样就是一个工具类，当线程调用ThreadlocalRandom的current，ThreadLocalRandom负责初始化threalocalrandomseed变量，就是初始化种子
        当调用ThreadLocalRandom的nextInt的时候，实际上就是获取当前线程的threadlcoalrandomseed变量作为种子来计算新的种子，然后更新threadlocalrandomseed变量，根据新的种子使用具体的算法计算随机数，需要注意的是，threadlocalrandomseed仅仅是一个普通的long类型变量，并不是原子类型，但是由于他是线程级别的，所以他就不会存在线程安全的问题，并需要原子性变量，
        其中seed和probeGenerator是原子变量，他是在初始化种子的时候使用，每个线程只会调用一次，另外变量instance是ThreadLocalRandom的一个实例，该变量是static,多个线程使用的实例是同一个，但是由于具体的种子存在在线程里面的，所以在ThreadlocalRandom的实例里面只包含线程无关的的通用算法，因此他是线程安全的。
        if (UNSAFE.getInt(Thread.currentThread(), PROBE) == 0)：如上面代码(7)，如果线程的threadlocalRandomProbe的变量值为0时(默认他的值就是0)，则说明他是第一次调用current，那么就会使用localinit()方法计算当前线程的初始化种子，这里是为了延迟初始化，在不使用生成随机数功能时，就不会初始化线程的种子变量，这也是一种优化，
        localInit();：代码(8)首先根据probeGenerator计算当前线程中ThreadLocalRandomProbe的初始值，然后根据seeder计算当前线程的初始化种子，而后把这两个变量放到当前线程，代码9返回ThreadLocalRandom的实例，需要注意的是这个方法是静态方法，多个线程返回的是同一个实例，
# 第4章 java并发包中原子操作类原理剖析
    JUC 并发包中包含有 Atomiclnteger、AtomicLong和tomicBoolean 等原子性操作类，它们的原理类似，本章讲解 AtomicLong 类。 
    AtomicLong 是原子性递增或者递减类，其内部使用 Unsafe 来实现。
    AtomicLong
        如上代码中的两个线程各自统计自己所持数据中0的个数，每当找到一个0就会调用 AtomicLong 原子性递增方法。
        在没有原子类的情况下， 实现计数器需要使用一定的同步措施， 如使用 synchronized 关键字等，但是这些都是阻塞算法，对性能有一定损耗，
        而本章介绍的这些原子操作使用 CAS 非阻塞算法，性能更好，但是在高并发情况下AtomicLong 还会存在性能问题 JDK 提供了一个在高并发下性能更好的 LongAdder 类
        使用 AtomicLong 时，在高并发下大量线程会同时去竞争更新同一个原子变量，但是由于同时只有一个线程的 CAS 操作会成功，这就造成了大量线程竞争失败后，会通过无限循环不断进行自旋尝试 CAS 操作， 这会白白浪费 CPU 资源。
    LongAdder
        (1) LongAdder 的结构是怎样的？ 
        (2) 当前线程应该访问 Cell 数组里面的哪一个 Cell 元素？ 
        (3) 如何初始化 Cell 数组？ 
        (4) Cell 数组如何扩容？ 
        (5) 线程访问分配的 Cell 元素有冲突后如何处理？ 
        (6) 如何保证线程操作被分配的 Cell 元素的原子性？
            @sun.misc.Contended 用于解决伪共享（false sharing）问题
            cas(long cmp, long val)方法，做的事情也就是简单的CAS操作
        LongAdder 会将这个原子变量分离成一个 Cell 数组，每个线程通过 Hash 获取到自己数组，这样就减少了乐观锁的重试次数，从而在高竞争下获得优势；
        而在低竞争下表现的又不是很好，可能是因为自己本身机制的执行时间大于了锁竞争的自旋时间，因此在低竞争下表现性能不如 AtomicInteger。
        在低竞争的并发环境下 AtomicInteger 的性能是要比 LongAdder 的性能好，而高竞争环境下 LongAdder 的性能比 AtomicInteger 好，因此我们在使用时要结合自身的业务情况来选择相应的类型。
    LongAccumulator 类原理探究
        分段进行CAS重试，最后结果再累加，提升了程序的并行性能 
# 第5章 Java并发包中并发List源码剖析
    CopyOnWriteArrayList
        并发包中的并发List只有CopyOnWriteArrayList。
        CopyOnWriteArrayList使用写时复制的策略，来保证list的一致性，而获取-修改-写入，这三步操作不是原子性的，所以，在增删改的过程中都使用了独占锁，来保证在某个时刻，只有一个线程能对list进行操作，是线程安全的，但是写时复制会导致弱一致性问题。
        另外，CopyOnWriteArrayList 提供了弱一致性的迭代器，从而保证在获取迭代器后，其他线程对 list 的修改是不可见的，迭代器遍历的数组是一个快照。
        而且，CopyOnWriteArraySet 的底层是 使用 CopyOnWriteArrayList 实现的。
# 第6章 Java并发包中锁原理剖析
    LockSupport工具类
        JDK 中的 rt.jar 包里面的 LockSupport 是个工具类，它的主要作用是挂起和唤醒线程，该工具类是创建锁和其他同步类的基础。
        LockSupport类的核心方法其实就两个：park()和unpark()，其中park()方法用来阻塞当前调用线程，unpark()方法用于唤醒指定线程。
        park/unpark 与 wait/notify的区别：
            （1）wait和notify都是Object中的方法,在调用这两个方法前必须先获得锁对象，但是park不需要获取某个对象的锁就可以锁住线程。
            （2）notify只能随机选择一个线程唤醒，无法唤醒指定的线程，unpark却可以唤醒一个指定的线程。
            （3）unpark函数可以先于park调用，所以不需要担心线程间的执行的先后顺序。
        LockSupport.park()会检查线程是否设置了中断标志位，如果设置了，则返回（这里并不会清除中断标志位）
        Thread.interrupted()会清除中断标志
        sleep清除中断标志位但不会更改_counter
        方法介绍
            park()： 阻塞当前线程，直到unpark方法被调用或当前线程被中断，park方法才会返回。
            park(Object blocker)： 同park()方法，多了一个阻塞对象blocker参数。
            parkNanos(long nanos)： 同park方法，nanos表示最长阻塞超时时间，超时后park方法将自动返回。
            parkNanos(Object blocker, long nanos)： 同parkNanos(long nanos)方法，多了一个阻塞对象blocker参数。
            parkUntil(long deadline)： 同park()方法，deadline参数表示最长阻塞到某一个时间点，当到达这个时间点，park方法将自动返回。（该时间为从1970年到现在某一个时间点的毫秒数）
            parkUntil(Object blocker, long deadline)： 同parkUntil(long deadline)方法，多了一个阻塞对象blocker参数。
            unpark(Thread thread)： 唤醒处于阻塞状态的线程thread。
    抽象同步队列AQS概述
        AbstractQueuedSynchronizer抽象同步队列简称AQS，它是实现同步器的基础组件，并发包中锁的底层就是使用AQS实现的。
        AQS是一个FIFO的双向队列,内部通过节点head和tail记录队首和队尾元素，队列元素类型为Node，其中Node中的thread变量用来存放进入AQS队列里面的线程; prev记录当前节点的前驱节点,next记录当前节点的后继节点。
        AQS采用模板方法模式，父类抽象出通用模板，将方法延迟到子类加载。
        $Node节点:
            SHARED: 标记该线程是获取共享资源时被阻塞挂起放入AQS队列的
            EXCLUSIVE: 标记线程是获取独占资源时被挂起后放入AQS队列的
        $waitStatus:记录当前线程等待状态
            CANCELLED: 线程被取消了
            SINGAL: 线程需要被唤醒
            CONDITION: 线程在条件队列里面等待
            PROPAGATE: 释放共享资源时需要通知其他节点
        AQS维持了一个单一的状态信息state,可以通过getState,setState,compareAndSetState函数修改其值
            对于ReenrantLock: state表示当前线程获取锁的可重入次数;
            对于读写锁ReenrantReadWriteLock: state高16位表示读状态,也就是获取该读锁的次数,低16位表示获取到写锁的线程的可重入次数;
            对于Semphore来: state表示当前可用信号的个数;
            对于CountDownlatch: state表示计数器当前值;
        ConditionObject
            是条件变量，每个条件变量对应一个条件队列（单向链表队列），启用来存放调用条件变量得await方法后被阻塞的线程。
        对于AQS来说,线程同步的关键是对状态值 state进行操作。根据 state是否属于一个线程,操作 state的方式分为独占方式和共享方式。
            在独占方式下获取和释放资源使用的方法为: void acquire(int arg)、void acquirelnterruptibly(int arg)、boolean release(int arg)
            在共享方式下获取和释放资源的方法为: void acquireShared(int arg)、void acquireSharedinterruptibly (int arg) 、boolean releaseShared(int arg)
    ReentrantLock
        本节介绍了Reentrantlock的实现原理, Reentrantlock的底层是使用AQS实现的可重入独占锁。
        在这里AQS状态值为0表示当前锁空闲,为大于等于1的值则说明该锁已经被占用。
        该锁内部有公平与非公平实现,默认情况下是非公平的实现。
        另外,由于该锁是独占锁,所以某时只有一个线程可以获取该锁。
    ReentrantReadWriteLock（读写锁）
        ReentrantReadWriteLock 采用 读写分离的策略，允许多个线程可以同时获取读锁。
        本节介绍了读写锁ReentrantreadWritelock的原理,它的底层是使用AQS实现的，ReentrantreadWritelock巧妙地使用AQS的状态值的高16位表示获取到读锁的个数,低16位表示获取写锁的线程的可重入次数,并通过CAS对其进行操作实现了读写分离,这在读多写少的场景下比较适用。
    StampedLock
        StampedLock 的读写锁都是不可重入锁，该锁不是直接实现LockReaWriteLock接口 ，而是其在内部自己维护了一个双向阻塞队列。
        提供了三种读写模式的锁：
            写锁（排他或者独占锁，不是可重入锁）。
                某时只有一个线程可以获取该锁，当一个线程获取该锁后，其他请求读锁和写锁的线程必须等待，类似ReentrantReadWriteLock的写锁（不是可重入锁）
                方法writeLock()可能会阻塞等待独占访问，返回可以在方法中使用的标记unlockWrite(long)以释放锁。
                提供了非阻塞的tryWriteLock方法，tryWriteLock还提供了不定时和定时版本。当锁处于写模式时，可能无法获得读锁，所有乐观读验证都将失败。
            悲观读锁（共享锁，不是可重入锁）。
                在没有线程获取独占锁的情况下，多个线程可以同时获取该锁。如果已有线程获取写锁，则其他线程请求获取该锁会被阻塞，类似ReentrantReadWriteLock的读锁。
                这里说的悲观是指在具体操作数据前其会悲观地认为其他线程可能要对自己操作的数据进行修改，所以需要先对数据加锁，这是在读少写多的情况下的一种考虑。
                方法readLock()可能会阻塞等待非独占访问，返回可以在方法中使用的标记unlockRead(long)以释放锁。tryReadLock还提供了不定时和定时版本。
            乐观读锁。
                它是相对于悲观锁来说的，在操作数据前并没有通过 CAS 设置锁的状态，仅仅通过位运算测试。
                tryOptimisticRead() 仅当锁当前未处于写入模式时，方法才返回非零戳记。方法validate(long)如果自从获得给定的戳记后没有在写模式下获得锁，则返回 true。
                这种模式可以被认为是读锁的一个非常弱的版本，它可以随时被写者打破。对短的只读代码段使用乐观模式通常会减少争用并提高吞吐量。然而，它的使用本质上是脆弱的。
                乐观读取部分应该只读取字段并将它们保存在局部变量中以供验证后以后使用。
                在乐观模式下读取的字段可能非常不一致，因此仅当您对数据表示足够熟悉以检查一致性和/或重复调用方法时才适用validate(). 
                例如，当首先读取对象或数组引用，然后访问其字段、元素或方法之一时，通常需要这些步骤。          
        StampedLock 支持这三种锁在一定条件下进行相互转换。例如 long tryConvertToWriteLock(long stamp)期望把 stamp 标示的锁升级为写锁，这个函数会在下面几种情况下返回一个有效的stamp（也就是晋升写锁成功）
            当前锁己经是写锁模式了。
            当前锁处于读锁模式， 并且没有其他线程是读锁模式。
            当前处于乐观读模式，井且当前写锁可用。
        Stampedlock提供的读写锁与 Reentrantread Writelock类似,只是前者提供的是不可重入锁。但是前者通过提供乐观读锁在多线程多读的情况下提供了更好的性能,这是因为获取乐观读锁时不需要进行CAS操作设置锁的状态,而只是简单地测试状态。


# 马士兵多线程
## 互联网三高
    高性能、高拓展、高可用
    高性能的体现
        响应（低延时）
        吞吐（高吞吐量、高并发）
    提升系统性能：1.降低延时，2.提高吞吐
    amdahl定律告诉我们，提升系统的响应比较困难，所以现在很多系统的性能提升现在提高吞吐量上->各种各样的系统架构
    举例：
        集群 - 吞吐
        负载均衡 - 吞吐
        缓存 - 响应
        JVM优化 - 响应
        分库分表 - 吞吐/响应
        tomcat非阻塞协议 - 响应
        MQ异步 -
## 多线程
    是不是线程数量越多越好：不是
    多线程与同步锁
        synchronized {
            i++;
        }
        可见性、有序性、原子性

