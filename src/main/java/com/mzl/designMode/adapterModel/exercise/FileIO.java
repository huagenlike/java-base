package com.mzl.designMode.adapterModel.exercise;

import java.io.IOException;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public interface FileIO {

    public void readFromFile(String filename) throws IOException;

    public void writeToFile(String filename) throws IOException;

    public void setValue(String key, String value);

    public String getValue(String key);

}
