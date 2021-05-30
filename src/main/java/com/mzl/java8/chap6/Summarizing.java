package com.mzl.java8.chap6;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.*;
import static com.mzl.java8.chap6.Dish.menu;

public class Summarizing {

    public static void main(String ... args) {
        System.out.println("Nr. of dishes: " + howManyDishes());
        System.out.println("The most caloric dish is: " + findMostCaloricDish());
        System.out.println("The most caloric dish is: " + findMostCaloricDishUsingComparator());
        System.out.println("Total calories in menu: " + calculateTotalCalories());
        System.out.println("Average calories in menu: " + calculateAverageCalories());
        System.out.println("Menu statistics: " + calculateMenuStatistics());
        System.out.println("Short menu: " + getShortMenu());
        System.out.println("Short menu comma separated: " + getShortMenuCommaSeparated());
    }

    /**
     * 有几道菜
     */
    private static long howManyDishes() {
        return menu.stream().collect(counting());
    }

    /**
     * 找到热量最高的菜
     */
    private static Dish findMostCaloricDish() {
        return menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)).get();
    }

    /**
     * 使用比较器找到热量最高的菜
     */
    private static Dish findMostCaloricDishUsingComparator() {
        // 创建比较器
        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        BinaryOperator<Dish> moreCaloricOf = BinaryOperator.maxBy(dishCaloriesComparator);
        return menu.stream().collect(reducing(moreCaloricOf)).get();
    }

    /**
     * 计算总卡路里
     */
    private static int calculateTotalCalories() {
        return menu.stream().collect(summingInt(Dish::getCalories));
    }

    /**
     * 计算平均卡路里
     */
    private static Double calculateAverageCalories() {
        return menu.stream().collect(averagingInt(Dish::getCalories));
    }

    /**
     * 计算菜单统计（菜总数、总卡路里、最低卡路里、平均卡路里、最高卡路里）
     */
    private static IntSummaryStatistics calculateMenuStatistics() {
        return menu.stream().collect(summarizingInt(Dish::getCalories));
    }

    /**
     * 获取所有菜名称，拼接
     */
    private static String getShortMenu() {
        return menu.stream().map(Dish::getName).collect(joining());
    }

    /**
     * 获取所有菜名称，拼接（，隔开）
     */
    private static String getShortMenuCommaSeparated() {
        return menu.stream().map(Dish::getName).collect(joining(", "));
    }
}
