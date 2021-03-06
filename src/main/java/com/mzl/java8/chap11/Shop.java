package com.mzl.java8.chap11;

import java.util.Random;

import static com.mzl.java8.chap11.Util.delay;
import static com.mzl.java8.chap11.Util.format;

public class Shop {

    private final String name;
    private final Random random;

    public Shop(String name) {
        this.name = name;
        random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
    }

    /**
     * 依据指定产品名称返回价格的方法
     * @param product
     * @return
     */
    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return name + ":" + price + ":" + code;
        // return String.format("%s:%.2f:%s", name, price, code);

    }

    /**
     * 计算价格
     * @param product
     * @return
     */
    public double calculatePrice(String product) {
        delay();
        return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
    }

    public String getName() {
        return name;
    }
}
