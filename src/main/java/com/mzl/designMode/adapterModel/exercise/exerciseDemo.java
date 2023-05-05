package com.mzl.designMode.adapterModel.exercise;

import java.io.IOException;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 * 请求者（main） 使用对象（FileIO） 的适配（FileProperties） 去适配 (Properties)
 */
public class exerciseDemo {

    public static void main(String[] args) {
        FileIO f = new FileProperties();
        try {
            f.readFromFile("C:\\Users\\Administrator\\Desktop\\file.txt");
            f.setValue("year", "2022");
            f.setValue("month", "11");
            f.setValue("day", "4");
            f.writeToFile("C:\\Users\\Administrator\\Desktop\\newFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
