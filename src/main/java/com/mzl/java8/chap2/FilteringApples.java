package com.mzl.java8.chap2;

import java.util.*;

/**
 * @author may
 * @version 1.0
 * @className: FilteringApples
 * @description: TODO
 * @date 2021/4/23 13:49
 */
public class FilteringApples{

    public static void main(String ... args){

        List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));

        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples = filterApplesByColor(inventory, "green");
        System.out.println(greenApples);

        // [Apple{color='red', weight=120}]
        List<Apple> redApples = filterApplesByColor(inventory, "red");
        System.out.println(redApples);

        // [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
        List<Apple> greenApples2 = filter(inventory, new AppleColorPredicate());
        System.out.println(greenApples2);

        // [Apple{color='green', weight=155}]
        List<Apple> heavyApples = filter(inventory, new AppleWeightPredicate());
        System.out.println(heavyApples);

        // []
        List<Apple> redAndHeavyApples = filter(inventory, new AppleRedAndHeavyPredicate());
        System.out.println(redAndHeavyApples);

        // [Apple{color='red', weight=120}]
        List<Apple> redApples2 = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple a){
                return a.getColor().equals("red");
            }
        });
        System.out.println(redApples2);

        // 你已经看到，可以把行为抽象出来，让你的代码适应需求的变化，但这个过程很啰嗦，因为你需要声明很多只要实例化一次的类。让我们来看看可以怎样改进
        prettyPrintApple(inventory, new AppleFancyFormatter());
        prettyPrintApple(inventory, new AppleSimpleFormatter());

        /**
         * @Author lhg
         * @Description 第五次尝试：使用匿名类
         * @Date 14:43 2021/4/23
         * @Param [args]
         * @return void
         * 第一，它往往很笨重，因为它占用了很多空间。还拿前面的例子来看，如下面高亮的代码所示：
         * 第二，很多程序员觉得它用起来很让人费解。比如，测验2.2展示了一个经典的Java谜题，它让大多数程序员都措手不及。你来试试看吧。
         **/
        List<Apple> redApples1 = filter(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });

        /**
         * @Author lhg
         * @Description 第六次尝试：使用Lambda表达式
         * @Date 14:57 2021/4/23
         * @Param [args]
         * @return void
         **/
        List<Apple> result = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));

        /**
         * @Author lhg
         * @Description 第七次尝试：将List类型抽象化
         * @Date 15:04 2021/4/23
         * @Param [args]
         * @return void
         **/
        List<Apple> redApples3 = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));
        //List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 ==0);

        // 你可以使用匿名类，按照重量升序对库存排序：
        inventory.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        // 你可以使用匿名类，按照重量升序对库存排序：使用Lambda表达式
        inventory.sort((Apple o1, Apple o2) -> o1.getWeight().compareTo(o2.getWeight()));
    }

    /**
     * @Author lhg
     * @Description 初试牛刀：筛选绿苹果
     * @Date 12:48 2021/4/23
     * @Param [inventory]
     * @return java.util.List<com.mzl.java8.chapter2.Apple>
     **/
    public static List<Apple> filterGreenApples(List<Apple> inventory){
        // 积累苹果列表
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            // 仅选出绿苹果
            if("green".equals(apple.getColor())){
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * @Author lhg
     * @Description 再展身手：把颜色作为参数
     * @Date 12:48 2021/4/23
     * @Param [inventory, color]
     * @return java.util.List<com.mzl.java8.chapter2.Apple>
     **/
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getColor().equals(color)){
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * @Author lhg
     * @Description 分轻的苹果和重的苹果
     * @Date 12:47 2021/4/23
     * @Param [inventory, weight]
     * @return java.util.List<com.mzl.java8.chapter2.Apple>
     *     解决方案不错，但是请注意，你复制了大部分的代码来实现遍历库存，并对每个苹果应用筛选条件。
     *     这有点儿令人失望，因为它打破了DRY（Don't Repeat Yourself，不要重复自己）的软件工程原则。
     *     如果你想要改变筛选遍历方式来提升性能呢？那就得修改所有方法的实现，而不是只改一个。从工程工作量的角度来看，这代价太大了。
     **/
    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getWeight() > weight){
                result.add(apple);
            }
        }
        return result;
    }

    /**
     * @Author lhg
     * @Description 第三次尝试：对你能想到的每个属性做筛选
     * @Date 12:52 2021/4/23
     * @Param [inventory, color, weight, flag]
     * @return java.util.List<com.mzl.java8.chapter2.Apple>
     *     你可以将颜色和重量结合为一个方法，称为filter。不过就算这样，你还是需要一种方式来区分想要筛选哪个属性。你可以加上一个标志来区分对颜色和重量的查询（但绝不要这样做！我们很快会解释为什么）。
     *     这个解决方案再差不过了。首先，客户端代码看上去糟透了。true和false是什么意思？
     *     此外，这个解决方案还是不能很好地应对变化的需求。如果这位农民要求你对苹果的不同属性做筛选，比如大小、形状、产地等，又怎么办？
     *     而且，如果农民要求你组合属性，做更复杂的查询，比如绿色的重苹果，又该怎么办？
     *     你会有好多个重复的filter方法，或一个巨大的非常复杂的方法。到目前为止，你已经给filterApples方法加上了值（比如String、Integer或boolean）的参数。
     *     这对于某些确定性问题可能还不错。但如今这种情况下，你需要一种更好的方式，来把苹果的选择标准告诉你的filterApples方法。在下一节中，我们会介绍了如何利用行为参数化实现这种灵活性。
     **/
    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (flag && apple.getColor().equals(color) || (!flag && apple.getWeight() > weight)) {
                result.add(apple);
            }
        }
        return result;
    }
    /*你可以这么用（但真的很笨拙）：
    List<Apple> greenApples = filterApples(inventory, "green", 0, true);
    List<Apple> heavyApples = filterApples(inventory, "", 150, false);*/

    /**
     * @Author lhg
     * @Description 第四次尝试：根据抽象条件筛选
     * @Date 13:02 2021/4/23
     * @Param [inventory, p]
     * @return java.util.List<com.mzl.java8.chapter2.Apple>
     *     这段代码比我们第一次尝试的时候灵活多了，读起来、用起来也更容易！
     *     现在你可以创建不同的ApplePredicate对象，并将它们传递给filterApples方法。免费的灵活性！
     *     比如，如果农民让你找出所有重量超过150克的红苹果，你只需要创建一个类来实现ApplePredicate就行了。你的代码现在足够灵活，可以应对任何涉及苹果属性的需求变更了：
     **/
    public static List<Apple> filter(List<Apple> inventory, ApplePredicate p){
        List<Apple> result = new ArrayList<>();
        for(Apple apple : inventory){
            if(p.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }

    public static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
        for (Apple apple : inventory) {
            String output = formatter.accept(apple);
            System.out.println(output);
        }
    }

    /* filterApples方法的行为取决于你通过ApplePredicate对象传递的代码。换句话说，你把filterApples方法的行为参数化了！
     * 但令人遗憾的是，由于该filterApples方法只能接受对象，所以你必须把代码包裹在ApplePredicate对象里。你的做法就类似于在内联“传递代码”，因为你是通过一个实现了test方法的对象来传递布尔表达式的。
     * 你将在2.3节（第3章中有更详细的内容）中看到，通过使用Lambda，你可以直接把表达式"red".equals(apple.getColor()) &&apple.getWeight() > 150传递给filterApples方法，而无需定义多个ApplePredicate类，从而去掉不必要的代码。
     * List<Apple> redAndHeavyApples = filterApples(inventory, new AppleRedAndHeavyPredicate());*/

    public static class Apple {
        private int weight = 0;
        private String color = "";

        public Apple(int weight, String color){
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "color='" + color + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * @Author lhg
     * @Description 根据苹果标准建模
     * @Date 12:56 2021/4/23
     * @Param
     * @return
     * 策略设计模式
     * 你考虑的是苹果，需要根据Apple的某些属性（比如它是绿色的吗？重量超过150克吗？）来返回一个boolean值。我们把它称为谓词（即一个返回boolean值的函数）。让我们定义一个接口来对选择标准建模：
     **/
    interface ApplePredicate{
        boolean test(Apple a);
    }

    static class AppleWeightPredicate implements ApplePredicate{
        @Override
        public boolean test(Apple apple){
            return apple.getWeight() > 150;
        }
    }

    /**
     * @author may
     * @version 1.0
     * @className: AppleGreenColorPredicate
     * @description: 可以用ApplePredicate的多个实现代表不同的选择标准
     * @date 2021/4/23 12:58
     * 策略设计模式
     */
    static class AppleColorPredicate implements ApplePredicate{
        @Override
        public boolean test(Apple apple){
            return "green".equals(apple.getColor());
        }
    }

    /**
     * @author may
     * @version 1.0
     * @className: AppleHeavyWeightPredicate
     * @description: 可以用ApplePredicate的多个实现代表不同的选择标准
     * @date 2021/4/23 12:57
     * 策略设计模式
     */
    static class AppleHeavyWeightPredicate  implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    }

    static class AppleRedAndHeavyPredicate implements ApplePredicate{
        @Override
        public boolean test(Apple apple){
            return "red".equals(apple.getColor())
                    && apple.getWeight() > 150;
        }
    }

    /**
     * @Author lhg
     * @Description 接受Apple并返回一个格式String值的方法
     * @Date 14:30 2021/4/23
     * @Param 
     * @return 
     **/
    interface AppleFormatter {
        String accept(Apple a);
    }

    static class AppleFancyFormatter implements AppleFormatter {
        @Override
        public String accept(Apple apple) {
            String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
            return "A" + characteristic + " " + apple.getColor() + "apple";
        }
    }

    static class AppleSimpleFormatter implements AppleFormatter {
        @Override
        public String accept(Apple apple) {
            return "An apple of" + apple.getWeight() + "g";
        }
    }

    /**
     * @Author lhg
     * @Description 第七次尝试：将List类型抽象化
     * @Date 15:00 2021/4/23
     * @Param
     * @return
     **/
    interface Predicate<T> {
        boolean test(T t);
    }

    /**
     * @Author lhg
     * @Description 第七次尝试：将List类型抽象化
     * @Date 15:02 2021/4/23
     * @Param
     * @return
     * 引入类型参数T
     * 现在你可以把filter方法用在香蕉、桔子、Integer或是String的列表上了。这里有一个使用Lambda表达式的例子：
     **/
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T e : list) {
            result.add(e);
        }
        return result;
    }
}
