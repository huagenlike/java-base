package com.mzl.java8.chap11.v1;

import com.mzl.java8.chap11.ExchangeService;
import com.mzl.java8.chap11.ExchangeService.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 最佳价格查询器（不包含折扣）
 */
public class BestPriceFinder {

    public static void main(String[] args) {
        BestPriceFinder bestPriceFinder = new BestPriceFinder();
        long start = System.nanoTime();
        // 正如你预期的，findPrices方法的执行时间仅比4秒钟多了那么几毫秒，因为对这4个商店的查询是顺序进行的，并且一个查询操作会阻塞另一个，每一个操作都要花费大约1秒左右的时间计算请求商品的价格。
//        System.out.println(bestPriceFinder.findPrices("myPhone27S"));
//        System.out.println(bestPriceFinder.findPricesSequential("myPhone27S"));
        // 对四个不同商店的查询实现了并行，所以完成所有操作的总耗时只有1秒多一点儿。你能做得更好吗？让我们尝试使用刚学过的CompletableFuture，将findPrices方法中对不同商店的同步调用替换为异步调用。
//        System.out.println(bestPriceFinder.findPricesByParallel("myPhone27S"));
//        System.out.println(bestPriceFinder.findPricesParallel("myPhone27S"));
        // 使用CompletableFuture发起异步请，查询所有商店
//        System.out.println(bestPriceFinder.findPricesFuture("myPhone27S"));
        System.out.println(bestPriceFinder.findPricesInUSD("myPhone27S"));

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    // 商家的列表
    private final List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                                                   new Shop("LetsSaveBig"),
                                                   new Shop("MyFavoriteShop"),
                                                   new Shop("BuyItAll"),
                                                   new Shop("ShopEasy"));

    // 创建一个定长线程池
    // 原先是4个商店，定长线程池设定的4，所以因为可以并行运行（通用线程池中处于可用状态的）的四个线程现在都处于繁忙状态，都在对前4个商店进行查询。第五个查询只能等到前面某一个操作完成释放出空闲线程才能继续
    //private final Executor executor = Executors.newFixedThreadPool(4, new ThreadFactory() {
    // 它们内部采用的是同样的通用线程池，默认都使用固定数目的线程，具体线程数取决于Runtime.getRuntime().availableProcessors()的返回值。然而，CompletableFuture具有一定的优势，因为它允许你对执行器（Executor）进行配置，尤其是线程池的大小，让它以更适合应用需求的方式进行配置，满足程序的要求，而这是并行流API无法提供的。让我们看看你怎样利用这种配置上的灵活性带来实际应用程序性能上的提升。
    // 优化方式
    // 创建一个线程池，线程池中线程的数目为100和商店数目二者中较小的一个值
    // 注意，你现在正创建的是一个由守护线程构成的线程池。Java程序无法终止或者退出一个正在运行中的线程，所以最后剩下的那个线程会由于一直等待无法发生的事件而引发问题。
    // 与此相反，如果将线程标记为守护进程，意味着程序退出时它也会被回收。这二者之间没有性能上的差异。现在，你可以将执行器作为第二个参数传递给supplyAsync工厂方法了。
    private final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
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
     */
    public List<String> findPrices(String product) {
        return shops.stream()
                // %s 字符串类型
                // %f 浮点类型
                //      %2f是把float的所有位数输出2位,包括小数点,如果不组2位,补0,如果超过2位,按照实际输出
                //      %.2f是float后的小数只输出两位。
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    /**
     * 使用并行流
     * @param product
     * @return
     */
    public List<String> findPricesByParallel(String product) {
        // 使用并行流并行地从不同的商店获取价格
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

        /**
         * 按顺序查询所有商店
         * @param product
         * @return
         */
    public List<String> findPricesSequential(String product) {
        return shops.stream()
                .map(shop -> shop.getName() + " price is " + shop.getPrice(product))
                .collect(toList());
    }

    /**
     * 使用并行查询所有商店
     * @param product
     * @return
     */
    public List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> shop.getName() + " price is " + shop.getPrice(product))
                .collect(toList());
    }

    /**
     * 　使用CompletableFuture发起异步请，查询所有商店
     * @param product
     * @return
     * 注意到了吗？这里使用了两个不同的Stream流水线，而不是在同一个处理流的流水线上一个接一个地放置两个map操作——这其实是有缘由的。
     * 考虑流操作之间的延迟特性，如果你在单一流水线中处理流，发向不同商家的请求只能以同步、顺序执行的方式才会成功。
     * 因此，每个创建CompletableFuture对象只能在前一个操作结束之后执行查询指定商家的动作、通知join方法返回计算结果。
     */
    public List<String> findPricesFuture(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                // 使用CompletableFuture以异步方式计算每种商品的价格，将执行器 executor 作为第二个参数传递给supplyAsync工厂方法了（使用线程池）
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is " + shop.getPrice(product), executor))
                .collect(toList());

        // 等待所有异步操作结束
        List<String> prices = priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
        return prices;
    }

    /**
     * 合并两个独立的CompletableFuture对象
     * @param product
     * @return
     * 这里整合的操作只是简单的乘法操作，用另一个单独的任务对其进行操作有些浪费资源，所以你只要使用thenCombine方法，无需特别求助于异步版本的thenCombineAsync方法。
     */
    public List<String> findPricesInUSD(String product) {
        List<CompletableFuture<Double>> priceFutures = new ArrayList<>();
        for (Shop shop : shops) {
            // Start of Listing 10.20.
            // Only the type of futurePriceInUSD has been changed to
            // CompletableFuture so that it is compatible with the
            // CompletableFuture::join operation below.
            CompletableFuture<Double> futurePriceInUSD =
                // 创建第一个任务查询商店取得商品的价格
                CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                .thenCombine(
                    CompletableFuture.supplyAsync(
                        // 创建第二个独立任务，查询美元和欧元之间的转换汇率
                        () ->  ExchangeService.getRate(Money.EUR, Money.USD)),
                    // 通过乘法整合得到的商品价格和汇率
                    (price, rate) -> price * rate
                );
            priceFutures.add(futurePriceInUSD);
        }
        // Drawback: The shop is not accessible anymore outside the loop,
        // so the getName() call below has been commented out.
        List<String> prices = priceFutures
                .stream()
                .map(CompletableFuture::join)
                .map(price -> /*shop.getName() +*/ " price is " + price)
                .collect(toList());
        return prices;
    }

    /**
     * 利用Java 7的方法合并两个Future对象
     * @param product
     * @return
     * 这里整合的操作只是简单的乘法操作，用另一个单独的任务对其进行操作有些浪费资源，所以你只要使用thenCombine方法，无需特别求助于异步版本的thenCombineAsync方法。
     */
    public List<String> findPricesInUSDJava7(String product) {
        // 创建一个ExecutorService将任务提交到线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Double>> priceFutures = new ArrayList<>();
        for (Shop shop : shops) {
            final Future<Double> futureRate = executor.submit(new Callable<Double>() { 
                public Double call() {
                    // 创建一个查询欧元到美元转换汇率的Future
                    return ExchangeService.getRate(Money.EUR, Money.USD);
                }
            });
            Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() { 
                public Double call() {
                    try {
                        // 在第二个Future中查询指定商店中特定商品的价格
                        double priceInEUR = shop.getPrice(product);
                        // 在查找价格操作的同一个Future中，将价格和汇率做乘法计算出汇后价格
                        return priceInEUR * futureRate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            });
            priceFutures.add(futurePriceInUSD);
        }
        List<String> prices = new ArrayList<>();
        for (Future<Double> priceFuture : priceFutures) {
            try {
                prices.add(/*shop.getName() +*/ " price is " + priceFuture.get());
            }
            catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return prices;
    }

    public List<String> findPricesInUSD2(String product) {
        List<CompletableFuture<String>> priceFutures = new ArrayList<>();
        for (Shop shop : shops) {
            // Here, an extra operation has been added so that the shop name
            // is retrieved within the loop. As a result, we now deal with
            // CompletableFuture<String> instances.
            CompletableFuture<String> futurePriceInUSD = 
                CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                .thenCombine(
                    CompletableFuture.supplyAsync(
                        () -> ExchangeService.getRate(Money.EUR, Money.USD)),
                    (price, rate) -> price * rate
                ).thenApply(price -> shop.getName() + " price is " + price);
            priceFutures.add(futurePriceInUSD);
        }
        List<String> prices = priceFutures
                .stream()
                .map(CompletableFuture::join)
                .collect(toList());
        return prices;
    }

    public List<String> findPricesInUSD3(String product) {
        // Here, the for loop has been replaced by a mapping function...
        Stream<CompletableFuture<String>> priceFuturesStream = shops
            .stream()
            .map(shop -> CompletableFuture
                .supplyAsync(() -> shop.getPrice(product))
                .thenCombine(
                    CompletableFuture.supplyAsync(() -> ExchangeService.getRate(Money.EUR, Money.USD)),
                    (price, rate) -> price * rate)
                .thenApply(price -> shop.getName() + " price is " + price));
        // However, we should gather the CompletableFutures into a List so that the asynchronous
        // operations are triggered before being "joined."
        List<CompletableFuture<String>> priceFutures = priceFuturesStream.collect(toList());
        List<String> prices = priceFutures
            .stream()
            .map(CompletableFuture::join)
            .collect(toList());
        return prices;
    }

}
