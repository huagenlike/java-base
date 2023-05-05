package com.mzl.designMode.adapterModel.classAdapterModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 适配器模式-继承
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 * 将交流电100伏特电压转换成直流12伏特电压的适配器
 * 这就好像笔记本只要在直流12伏特的电压下就能正常工作，但他并不知道这12伏特的电压是由适配器将100伏特交流电压转换而成的
 */
public class classAdapterModelDemo {

    public static void main(String[] args) {
        Print p = new PrintBanner("Hello");
        p.printWeak();
        p.printStrong();
    }

}
