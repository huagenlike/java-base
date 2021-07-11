package com.mzl.concurrentProgramming.chap6;

import java.util.concurrent.locks.StampedLock;

/**
 * @author lihuagen
 * @version 1.0
 * @className: Point
 * @description: StampedLock的案例
 * @date 2021/7/9 16:53
 * 管理二维点的例子来理解 StampedLock 的概念
 *
 * 使用乐观读锁还是很容易犯错误的，必须要小心，且必须要保证如下的使用顺序。
 * long stamp= lock.tryOptimisticRead(); // 非阻塞获取版本信息
 * copyVaraibale2ThreadMemory() // 复制变量到线程本地堆栈
 * if (!lock.validate(stamp)) { // 校验
 *      long stamp = lock.readLock();ll 获取读锁
 *      try {
 *          copyVaraibale2ThreadMemory(); // 复制变量到线程本地堆栈
 *      } finally {
 *          lock.unlock(stamp); // 释放悲观锁
 *      }
 * }
 * useThreadMemoryVarables(); // 使用线程本地堆栈里面的数据进行操作
 */
public class Point {
    // 成员变量
    private double x, y;
    // 锁实例
    private final StampedLock sl = new StampedLock();

    // 排他锁 - 写锁（writeLock）
    // 首先分析下move方法,该方法的作用是使用参数的增量值,改变当前 point坐标的位置。
    // 代码先获取到了写锁,然后对 point坐标进行修改,而后释放锁。
    // 该锁是排它锁,这保证了其他线程调用move函数时会被阻塞,也保证了其他线程不能获取读锁,来读取坐标的值,直到当前线程显式释放了写锁,保证了对变量xy操作的原子性和数据一致性。
    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    /**
     * 廖雪峰的官方网站，使用StampedLock：https://www.liaoxuefeng.com/wiki/1252599548343744/1309138673991714
     * StampedLock和ReadWriteLock相比，改进之处在于：读的过程中也允许获取写锁后写入！这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。
     * 乐观锁的意思就是乐观地估计读的过程中大概率不会有写入，因此被称为乐观锁。反过来，悲观锁则是读的过程中拒绝有写入，也就是写入必须等待。显然乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行。
     *
     * 我们通过tryOptimisticRead()获取一个乐观读锁，并返回版本号。接着进行读取，读取完成后，我们通过validate()去验证版本号，如果在读取过程中没有写入，版本号不变，验证成功，我们就可以放心地继续后续操作。
     * 如果在读取过程中有写入，版本号会发生变化，验证将失败。在失败的时候，我们再通过获取悲观读锁再次读取。由于写入的概率不高，程序在绝大部分情况下可以通过乐观读锁获取数据，极少数情况下使用悲观读锁获取数据。
     **/
    // 乐观读锁（tryOptimisticRead）
    // 该方法的作用是计算当前位置到原点(坐标为0.0)的距离,
    // 代码(1)首先尝试获取乐观读锁,如果当前没有其他线程获取到了写锁,那么代码(1)会返回一个非0的 stamp用来表示版本信息,
    // 代码(2)复制坐标变量到本地方法栈里面。
    // 代码(3)检查在代码(1)中获取到的 stamp值是否还有效,之所以还要在此校验是因为代码(1)获取读锁时并没有通过CAS操作修改锁的状态,而是简单地通过与或操作返回了一个版本信息,在这里校验是看在获取版本信息后到现在的时间段里面是否有其他线程持有了写锁,如果有则之前获取的版本信息就无效了如果校验成功则执行
    // 代码(7)使用本地方法栈里面的值进行计算然后返回。
    // 需要注意的是,在代码(3)中校验成功后,在代码(7)计算期间,其他线程可能获取到了写锁并且修改了xy的值,而当前线程执行代码(7)进行计算时采用的还是修改前的值的副本也就是操作的值是之前值的一个副本,一个快照,并不是最新的值。
    // 另外还有个问题,代码(2)和代码(3)能否互换?答案是不能。
    // 假设位置换了,那么首先执行 validate,假如 validate通过了,要复制xy值到本地方法栈,而在复制的过程中很有可能其他线程已经修改了xy中的一个值,这就造成了数据的不一致。
    // 那么你可能会问,即使不交换代码(2)和代码(3),在复制xy值到本地方法栈时,也会存在其他线程修改了xy中的一个值的情况,这不也会存在问题吗?
    // 这个确实会存在,但是,别忘了复制后还有 validate这一关呢,如果这时候有线程修改了xy中的某一值,那么肯定是有线程在调用validate前,调用 sl.tryOptimisticRead 后获取了写锁,这样进行 validate时就会失败。
    // 现在你应该明白了,这也是乐观读设计的精妙之处,而且也是在使用时容易出问题的地方。
    // 下面继续分析, validate失败后会执行代码(4)获取悲观读锁,如果这时候其他线程持有写锁,则代码(4)会使当前线程阻塞直到其他线程释放了写锁。
    // 如果这时候没有其他线程获取到写锁,那么当前线程就可以获取到读锁,然后执行代码(5)重新复制新的坐标值到本地方法栈,再然后就是代码(6)释放了锁。
    // 复制时由于加了读锁,所以在复制期间如果有其他线程获取写锁会被阻塞,这保证了数据的一致性。另外,这里的xy没有被声明为 volatil的,会不会存在内存不可见性问题呢?答案是不会,因为加锁的语义保证了内存的可见性。
    // 最后代码(7)使用方法栈里面的数据计算并返回,同理,这里在计算时使用的数据也可能不是最新的,其他写线程可能已经修改过原来的x,y值了。
    double distanceFromOrigin() {
        // 1.尝试获取乐观读锁
        long stamp = sl.tryOptimisticRead();
        // 2.将全部变量复制到方法体栈内
        double currentX = x, currentY = y;
        // 3.检查（1）处获取了读锁戳记后，锁有没有被其他写线程排他性抢占
        if (!sl.validate(stamp)) {
            // 4.如果被抢占则获取一个共享读锁（悲观获取）
            stamp = sl.readLock();
            try {
                // 5.将全部变量复制到方法体栈内
                currentX = x;
                currentY = y;
            } finally {
                // 6.释放共享读锁
                sl.unlockRead(stamp);
            }
        }
        // 7.返回计算结果
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    // 使用悲观锁获取读锁，并尝试转换为写锁
    // 方法的作用是,如果当前坐标为原点则移动到指定的位置代码
    // (1)获取悲观读锁,保证其他线程不能获取写锁来修改xy值。
    // 然后代码(2)判断,如果当前点在原点则更新坐标,
    // 代码(3)尝试升级读锁为写锁。这里升级不一定成功,因为多个线程都可以同时获取悲观读锁,当多个线程都执行到代码(3)时只有一个可以升级成功,升级成功则返回非0的 stamp,否则返回0。这里假设当前线程升级成功,
    // 然后执行代码(4)更新 stamp值和坐标值,之后退出循环。
    // 如果升级失败则执行代码(5)首先释放读锁,然后申请写锁,获取到写锁后再循环重新设置坐标值。
    // 最后代码(6)释放锁
    void moveIfAtOrigin(double newX, double newY) {
        // 1.这里可以使用乐观读锁替换
        long stamp = sl.readLock();
        try {
            // 2.如果当前点在原点则移动
            while (x == 0.0 && y == 0.0) {
                // 3.尝试将获取的读锁升级为写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                } else {
                    // 5.读锁升级写锁失败则释放读锁，显示获取独占写锁，然后循环重试
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            // 6.释放锁
            sl.unlock(stamp);
        }
    }
}
