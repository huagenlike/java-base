package com.mzl.designMode.adapterModel.classAdapterModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 适配器 - 变换装置
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class PrintBanner extends Banner implements Print {

    public PrintBanner(String string) {
        super(string);
    }

    @Override
    public void printWeak() {
        showWithParent();
    }

    @Override
    public void printStrong() {
        showWithAster();
    }

}
