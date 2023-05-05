package com.mzl.designMode.templateMethod;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class TemplateMethodDemo {

    public static void main(String[] args) {
        CharDisplay d1 = new CharDisplay('H');
        StringDisplay d2 = new StringDisplay("Hello,world.");
        StringDisplay d3 = new StringDisplay("你好，世界。");

        d1.display();
        d2.display();
        d3.display();
    }
}
