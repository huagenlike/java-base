package com.mzl.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author may
 * @version 1.0
 * @className: ExecutorConfig
 * @description: springboot的线程池配置
 * @date 2021/4/27 16:50
 * CPU 密集型任务
 *  对于 CPU 密集型计算，多线程本质上是提升多核 CPU 的利用率，所以对于一个 8 核的 CPU，每个核一个线程，理论上创建 8 个线程就可以了。
 *  如果设置过多的线程数，实际上并不会起到很好的效果。此时假设我们设置的线程数量是 CPU 核心数的 2 倍，因为计算任务非常重，会占用大量的 CPU 资源，所以这时 CPU 的每个核心工作基本都是满负荷的，
 *  而我们又设置了过多的线程，每个线程都想去利用 CPU 资源来执行自己的任务，这就会造成不必要的上下文切换，此时线程数的增多并没有让性能提升，反而由于线程数量过多会导致性能下降。
 *  因此，对于 CPU 密集型的计算场景，理论上线程的数量 = CPU 核数就是最合适的，不过通常把线程的数量设置为CPU 核数 +1，会实现最优的利用率。
 * IO 密集型任务
 *  对于 IO 密集型任务最大线程数一般会大于 CPU 核心数很多倍，因为 IO 读写速度相比于 CPU 的速度而言是比较慢的，如果我们设置过少的线程数，就可能导致 CPU 资源的浪费。而如果我们设置更多的线程数，那么当一部分线程正在等待 IO 的时候，它们此时并不需要 CPU 来计算，那么另外的线程便可以利用 CPU 去执行其他的任务，互不影响，这样的话在任务队列中等待的任务就会减少，可以更好地利用资源。
 *  对于 IO 密集型计算场景，最佳的线程数是与程序中 CPU 计算和 IO 操作的耗时比相关的，《Java并发编程实战》的作者 Brain Goetz 推荐的计算方法如下：
 *      线程数 = CPU 核心数 * (1 + IO 耗时/ CPU 耗时)
 *  还有一派的计算方式是《Java虚拟机并发编程》中提出的：
 *      线程数 = CPU 核心数 / (1 - 阻塞系数)
 *      其中计算密集型阻塞系数为 0，IO 密集型阻塞系数接近 1，一般认为在 0.8 ~ 0.9 之间。比如 8 核 CPU，按照公式就是 2 / ( 1 - 0.9 ) = 20 个线程数
 */
public class ExecutorConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public Executor asyncServiceExecutor() {
        logger.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(5);
        //配置最大线程数
        executor.setMaxPoolSize(5);
        //配置队列大小
        executor.setQueueCapacity(100);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
