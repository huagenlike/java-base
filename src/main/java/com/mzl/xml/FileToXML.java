package com.mzl.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @Author lihuagen
 * @Description kettle文件夹内查找出包含表的文件，并打印出文件名（TODO 选择DOM还是选择SAX？优劣是什么）
 * @Date 10:33 2023/5/5
 **/
public class FileToXML {

    public static void main(String[] args) throws DocumentException {
        String path = "C:\\Users\\Administrator\\Desktop\\qdp-polaris-kettle - 副本";
        String table = "patrol_region_user_everyday_statistics";

        File file = new File(path);
//        toXML(file);
        readXML(file, table);
    }

    /**
     * @Author lihuagen
     * @Description 将文件夹内的.ktr文件，生成.xml的文件，TODO 只需要初始化时执行一次
     * @Date 10:10 2023/5/5
     * @param file
     * @return
     **/
    public static void toXML(File file) {
        for (File tempFile : file.listFiles()) {
            if (tempFile.isDirectory()) {
                toXML(tempFile);
                continue;
            }
            if (tempFile.getName().endsWith(".ktr")) {
                File newFile = new File(tempFile.getParent(), tempFile.getName().replace(".ktr", ".xml"));
                try {
                    Files.copy(tempFile.toPath(), newFile.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @Author lihuagen
     * @Description 去xml文件中寻找包含 table 的表明
     * @Date 10:19 2023/5/5
     * @param xmlFile 文件
     * @param table 需要找的表名
     * @return
     **/
    public static void readXML(File xmlFile, String table) throws DocumentException {
        for (File tempFile : xmlFile.listFiles()) {
            if (tempFile.isDirectory()) {
                readXML(tempFile, table);
                continue;
            }
            if (tempFile.getName().endsWith(".xml")) {
                parseXML(tempFile, table);
            }
        }
    }

    /**
     * @Author lihuagen
     * @Description 解析xml文件，并找出包含 table 表名的文件
     * @Date 10:21 2023/5/5
     * @param xmlFile 文件
     * @param table 需要找的表名
     * @return
     **/
    public static void parseXML(File xmlFile, String table) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(xmlFile);
        Element rootElement = document.getRootElement();

        List<Element> elementList = rootElement.elements("step");
        for (Element element : elementList) {
            String elementName = element.element("type").getStringValue();
            if ("TableOutput".equals(elementName) && table.equals(element.element("table").getStringValue())) {
                System.out.println(">> 表名:" + table + ",path:" + xmlFile.getPath());
            }
        }
    }

}
