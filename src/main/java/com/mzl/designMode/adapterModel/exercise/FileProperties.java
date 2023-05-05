package com.mzl.designMode.adapterModel.exercise;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lihuagen
 * @version 1.0.0
 * @description
 * @createTime 2022/11/04
 * @copyright Copyright ©️ 2022 北京魔马科技
 */
public class FileProperties extends Properties implements FileIO{

    @Override
    public void readFromFile(String filename) throws IOException {
        load(new FileInputStream(filename));
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        store(new FileOutputStream(filename), "written by FileProperties");
    }

    @Override
    public void setValue(String key, String value) {
        setProperty(key, value);
    }

    @Override
    public String getValue(String key) {
        return getProperty(key, null);
    }
}
