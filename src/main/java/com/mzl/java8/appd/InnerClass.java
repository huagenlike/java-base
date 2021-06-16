package com.mzl.java8.appd;

import java.util.function.Function;

/**
 * @Author lhg
 * @Description 内部类
 * @Date 14:13 2021/6/16
 * @Param
 * @return
 **/
public class InnerClass {
    Function<Object, String> f = new Function<Object, String>() {
        @Override
        public String apply(Object obj) {
            return obj.toString();
        }
    };
}
