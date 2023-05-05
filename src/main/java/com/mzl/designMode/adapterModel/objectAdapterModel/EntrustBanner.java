package com.mzl.designMode.adapterModel.objectAdapterModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 交流100伏特 - 实际情况（委托）
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class EntrustBanner {

    private String string;

    public EntrustBanner(String string) {
        this.string = string;
    }

    public void showWithParent() {
        System.out.println("(" + string + ")");
    }

    public void showWithAster() {
        System.out.println("*" + string + "*");
    }

}
