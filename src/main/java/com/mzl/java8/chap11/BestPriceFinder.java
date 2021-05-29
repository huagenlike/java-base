package com.mzl.java8.chap11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 最佳价格查询器（不包含折扣）
 */
public class BestPriceFinder {

    public static void main(String[] args) {
        BestPriceFinder bestPriceFinder = new BestPriceFinder();
        long start = System.nanoTime();
        // 正如你预期的，findPrices方法的执行时间仅比4秒钟多了那么几毫秒，因为对这4个商店的查询是顺序进行的，并且一个查询操作会阻塞另一个，每一个操作都要花费大约1秒左右的时间计算请求商品的价格。
//        System.out.println(bestPriceFinder.findPricesSequential("myPhone27S"));
        // 对四个不同商店的查询实现了并行，所以完成所有操作的总耗时只有1秒多一点儿。你能做得更好吗？让我们尝试使用刚学过的CompletableFuture，将findPrices方法中对不同商店的同步调用替换为异步调用。
//        System.out.println(bestPriceFinder.findPricesParallel("myPhone27S"));
        // 使用CompletableFuture发起异步请，查询所有商店
        System.out.println(bestPriceFinder.findPricesFuture("myPhone27S"));

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

        bestPriceFinder.printPricesStream("myPhone27S");
    }

    // 商家列表
    private final List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                                                   new Shop("LetsSaveBig"),
                                                   new Shop("MyFavoriteShop"),
                                                   new Shop("BuyItAll"),
                                                   new Shop("ShopEasy"));

    // 创建一个定长线程池
    private final Executor executor = Executors.newFixedThreadPool(shops.size(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 使用守护线程——这种方式不会阻止程序的关停
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * 采用顺序查询所有商店的方式实现的findPrices方法
     * @param product
     * @return
     * 通过在shop构成的流上采用流水线方式执行三次map操作，我们得到了期望的结果
     *  第一个操作将每个shop对象转换成了一个字符串，该字符串包含了该 shop中指定商品的价格和折扣代码。
     *  第二个操作对这些字符串进行了解析，在Quote对象中对它们进行转换。
     *  最终，第三个map会操作联系远程的Discount服务，计算出最终的折扣价格，并返回该价格及提供该价格商品的shop。
     * 毫无意外，这次执行耗时10秒，因为顺序查询5个商店耗时大约5秒，现在又加上了Discount服务为5个商店返回的价格申请折扣所消耗的5秒钟。
     */
    public List<String> findPricesSequential(String product) {
        return shops.stream()
                // 取得每个shop对象中商品的原始价格（休眠1s）
                .map(shop -> shop.getPrice(product))
                // 在Quote 对象中对shop返回的字符串进行转换
                .map(Quote::parse)
                // 联系Discount服务，为每个Quote申请折扣（休眠1s）
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    /**
     * 采用并行查询所有商店的方式实现的findPrices方法
     * @param product
     * @return
     * 这次执行耗时2秒，因为并行查询5个商店耗时大约1秒，现在又加上了Discount服务为5个商店返回的价格申请折扣所消耗的1秒钟。
     */
    public List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    /**
     * 异步查询
     * @param product
     * @return
     */
    public List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> priceFutures = findPricesStream(product)
                .collect(Collectors.<CompletableFuture<String>>toList());

        return priceFutures.stream()
                // 等待流中的所有Future执行完毕，并提取各自的返回值
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    /**
     * 使用CompletableFuture实现findPrices方法
     * @param product
     * @return
     * 从shop对象中获取价格，接着把价格转换为Quote。
     * 拿到返回的Quote对象，将其作为参数传递给Discount服务，取得最终的折扣价格。
     * Java 8的 CompletableFuture API提供了名为thenCompose的方法，它就是专门为这一目的而设计的，thenCompose方法允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。
     *
     * 使用的thenCompose方法像CompletableFuture类中的其他方法一样，也提供了一个以Async后缀结尾的版本thenComposeAsync。
     * 通常而言，名称中不带Async的方法和它的前一个任务一样，在同一个线程中运行；而名称以Async结尾的方法会将后续的任务提交到一个线程池，所以每个任务是由不同的线程处理的。
     * 就这个例子而言，第二个CompletableFuture对象的结果取决于第一个CompletableFuture，所以无论你使用哪个版本的方法来处理CompletableFuture对象，对于最终的结果，或者大致的时间而言都没有多少差别。我们选择thenCompose方法的原因是因为它更高效一些，因为少了很多线程切换的开销。
     */
    public Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream()
                // 以异步方式取得每个shop中指定产品的原始价格
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice(product), executor))
                // Quote对象存在时，对其返回的值进行转换
                .map(future -> future.thenApply(Quote::parse))
                // 用另一个异步任务构造期望的Future，申请折扣
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor)));
    }

    public void printPricesStream(String product) {
        long start = System.nanoTime();
        CompletableFuture[] futures = findPricesStream(product)
                .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
                .toArray(size -> new CompletableFuture[size]);
        // allOf工厂方法接收一个由CompletableFuture构成的数组，数组中的所有CompletableFuture对象执行完成之后，它返回一个CompletableFuture<Void>对象。
        CompletableFuture.allOf(futures).join();
        // anyOf：比如，你正在查询两个汇率服务器，任何一个返回了结果都能满足你的需求。在这种情况下，你可以使用一个类似的工厂方法anyOf。
        // CompletableFuture.anyOf(futures).join();
        System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
    }

}
