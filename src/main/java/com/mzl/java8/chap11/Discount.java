package com.mzl.java8.chap11;

import static com.mzl.java8.chap11.Util.delay;
import static com.mzl.java8.chap11.Util.format;

/**
 * 对多个异步任务进行流水线操作
 *
 * 我们还假设所有的商店都同意修改getPrice方法的返回格式。
 * getPrice现在以Shop-Name:price:DiscountCode的格式返回一个String类型的值。
 * 我们的示例实现中会返回一个随机生成的Discount.Code，以及已经计算得出的随机价格：
 */
public class Discount {

    // 以枚举类型定义的折扣代码
    public enum Code {
        NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);

        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                Discount.apply(quote.getPrice(), quote.getDiscountCode()); // 将折扣代码应用于商品最初的原始价格
    }
    private static double apply(double price, Code code) {
        // 模拟Discount服务响应的延迟
        delay();
        return format(price * (100 - code.percentage) / 100);
    }
}
