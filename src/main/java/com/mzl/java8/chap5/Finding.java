package com.mzl.java8.chap5;

import com.mzl.java8.chap4.*;

import java.util.Optional;

import static com.mzl.java8.chap4.Dish.menu;

/**
 * 过滤的用法
 */
public class Finding{

    public static void main(String...args){
        if(isVegetarianFriendlyMenu()){
            System.out.println("Vegetarian friendly");
        }

        System.out.println(isHealthyMenu());
        System.out.println(isHealthyMenu2());
        
        Optional<Dish> dish = findVegetarianDish();
        dish.ifPresent(d -> System.out.println(d.getName()));

        Optional<Dish> dishFirst = findFirstVegetarianDish();
        dishFirst.ifPresent(d -> System.out.println(d.getName()));
    }

    /**
     * 匹配一个就行
     */
    private static boolean isVegetarianFriendlyMenu(){
        return menu.stream().anyMatch(Dish::isVegetarian);
    }

    /**
     * 都匹配
     */
    private static boolean isHealthyMenu(){
        return menu.stream().allMatch(d -> d.getCalories() < 1000);
    }

    /**
     * 都不匹配
     */
    private static boolean isHealthyMenu2(){
        return menu.stream().noneMatch(d -> d.getCalories() >= 1000);
    }

    /**
     * 找到了就返回
     */
    private static Optional<Dish> findVegetarianDish(){
        return menu.stream().filter(Dish::isVegetarian).findAny();
    }
    /**
     * 找第一个
     */
    private static Optional<Dish> findFirstVegetarianDish(){
        return menu.stream().filter(Dish::isVegetarian).findFirst();
    }
}
