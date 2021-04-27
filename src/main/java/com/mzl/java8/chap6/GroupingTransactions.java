package com.mzl.java8.chap6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mzl.java8.chap6.Dish.menu;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-04-27 20:11
 **/
public class GroupingTransactions {
    public static List<Transaction> transactions = Arrays.asList( new Transaction(Currency.EUR, 1500.0),
            new Transaction(Currency.USD, 2300.0),
            new Transaction(Currency.GBP, 9900.0),
            new Transaction(Currency.EUR, 1100.0),
            new Transaction(Currency.JPY, 7800.0),
            new Transaction(Currency.CHF, 6700.0),
            new Transaction(Currency.EUR, 5600.0),
            new Transaction(Currency.USD, 4500.0),
            new Transaction(Currency.CHF, 3400.0),
            new Transaction(Currency.GBP, 3200.0),
            new Transaction(Currency.USD, 4600.0),
            new Transaction(Currency.JPY, 5700.0),
            new Transaction(Currency.EUR, 6800.0) );

    public static void main(String[] args) {
        // 用指令式风格对交易按照货币分组
        Map<Currency, List<Transaction>> transactionByCurrencies = new HashMap<>();
        for (Transaction transaction : transactions) {
            Currency currency = transaction.getCurrency();
            List<Transaction> transactionForCurrency = transactionByCurrencies.get(currency);
            if (transactionForCurrency == null) {
                transactionForCurrency = new ArrayList<>();
                transactionByCurrencies.put(currency, transactionForCurrency);
            }
            // 将当前遍历的Transaction加入同一货币的Transaction的List
            transactionForCurrency.add(transaction);
        }
        System.out.println(transactionByCurrencies);

        // 用Stream中collect方法的一个更通用的Collector参数，你就可以用一句话实现完全相同的结果，而用不着使用上一章中那个toList的特殊情况了：
        Map<Currency, List<Transaction>> collect = transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency));
        System.out.println(collect);

        // 利用counting工厂方法返回的收集器，数一数菜单里有多少种菜
        long howManyDishes = menu.stream().collect(Collectors.counting());
        System.out.println(howManyDishes);
        // 还可以写得更为直接
        howManyDishes = menu.stream().count();

        // 查找流中的最大值和最小值
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCaloriesDish = menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));
        System.out.println(mostCaloriesDish.isPresent());
        if (mostCaloriesDish.isPresent()) {
            System.out.println(mostCaloriesDish.get());
        }
        Optional<Dish> leastCaloriesDish = menu.stream().collect(Collectors.minBy(dishCaloriesComparator));
        System.out.println(leastCaloriesDish.isPresent());
        if (leastCaloriesDish.isPresent()) {
            System.out.println(leastCaloriesDish.get());
        }
        // 汇总（所有菜的卡路里）
        Integer totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        System.out.println(totalCalories);

        // 计算数值的平均数
        Double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println(avgCalories);

        // 例如，通过一次summarizing操作你可以就数出菜单中元素的个数，并得到菜肴热量总和、平均值、最大值和最小值
        IntSummaryStatistics menuStatistics = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(menuStatistics);

        // joining工厂方法返回的收集器会把对流中每一个对象应用toString方法得到的所有字符串连接成一个字符串
        String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
        System.out.println(shortMenu);
        // String shortMenu = menu.stream().collect(Collectors.joining());
        // joining工厂方法有一个重载版本可以接受元素之间的分界符，这样你就可以得到一个逗号分隔的菜肴名称列表：
        String shortMenu1 = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
        System.out.println(shortMenu1);

        /**
         * 事实上，我们已经讨论的所有收集器，都是一个可以用reducing工厂方法定义的归约过程的特殊情况而已。Collectors.reducing工厂方法是所有这些特殊情况的一般化。
         * 第一个参数是归约操作的起始值，也是流中没有元素时的返回值，所以很显然对于数值和而言0是一个合适的值。
         * 第二个参数就是你在6.2.2节中使用的函数，将菜肴转换成一个表示其所含热量的int。
         * 第三个参数是一个BinaryOperator，将两个项目累积成一个同类型的值。这里它就是对两个int求和。
         */
        Integer totalCalories1 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
        System.out.println(totalCalories1);

        // 你可以使用下面这样单参数形式的reducing来找到热量最高的菜
        Optional<Dish> mostCalorieDish = menu.stream().collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));

        // 你可以像下面这样使用reduce方法来实现toListCollector所做的工作
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        List<Integer> numbers = stream.reduce(new ArrayList<Integer>(),(List<Integer> l, Integer e) -> {
            l.add(e);
            return l;
        },(List<Integer> l1, List<Integer> l2) -> {
            l1.addAll(l2);
            return l1;
        });
        System.out.println(numbers);

        int totalCalories2 = menu.stream().collect(Collectors.reducing(0, // ←─初始值
                Dish::getCalories, //←─转换函数
                Integer::sum)); // ←─累积函数
        System.out.println(totalCalories2);

        // 将菜肴流映射为每一道菜的热量，然后用前一个版本中使用的方法引用来归约得到的流
        // 就像流的任何单参数reduce操作一样，reduce(Integer::sum)返回的不是int而是Optional<Integer>，以便在空流的情况下安全地执行归约操作
        Integer totalCalories3 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
        System.out.println(totalCalories3);

        // 更简洁的方法是把流映射到一个IntStream，然后调用sum方法，你也可以得到相同的结果：
        int totalCalories4 = menu.stream().mapToInt(Dish::getCalories).sum();
        System.out.println(totalCalories4);

        // 用reducing连接字符串
        String shortMenu2 = menu.stream().map(Dish::getName).collect(Collectors.joining());
        System.out.println(shortMenu2);
        String shortMenu3 = menu.stream().map(Dish::getName).collect( Collectors.reducing((s1, s2) -> s1 + s2 )).get();
        System.out.println(shortMenu3);

        // 这无法编译，因为reducing接受的参数是一个BinaryOperator<t>，也就是一个BiFunction<T,T,T>。这就意味着它需要的函数必须能接受两个参数，然后返回一个相同类型的值，但这里用的Lambda表达式接受的参数是两个菜，返回的却是一个字符串
        // String shortMenu = menu.stream().collect( Collectors.reducing( (d1, d2) -> d1.getName() + d2.getName() ) ).get();

        String shortMenu4 = menu.stream().collect( Collectors.reducing( "",Dish::getName, (s1, s2) -> s1 + s2 ) );
        System.out.println(shortMenu4);
    }

    public static class Transaction {
        private final Currency currency;
        private final double value;

        public Transaction(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " " + value;
        }
    }

    public enum Currency {
        EUR, USD, JPY, GBP, CHF
    }
}
