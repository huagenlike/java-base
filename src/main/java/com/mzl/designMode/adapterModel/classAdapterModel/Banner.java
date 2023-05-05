package com.mzl.designMode.adapterModel.classAdapterModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 交流100伏特 - 实际情况
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class Banner {

    private String string;

    public Banner(String string) {
        this.string = string;
    }

    public void showWithParent() {
        System.out.println("(" + string + ")");
    }

    public void showWithAster() {
        System.out.println("*" + string + "*");
    }
}
