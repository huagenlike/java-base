package com.mzl.designMode.adapterModel.objectAdapterModel;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description 适配器 - 变换装置（委托）
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class EntrustPrintBanner extends EntrustPrint{

    private EntrustBanner entrustBanner;

    public EntrustPrintBanner(String string) {
        this.entrustBanner = new EntrustBanner(string);
    }

    @Override
    public void printWeak() {
        entrustBanner.showWithParent();
    }

    @Override
    public void printStrong() {
        entrustBanner.showWithAster();
    }

}
