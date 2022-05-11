package com.mzl.dataStructure.sparsearray;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 稀疏数组
 * 现在第二行第三个为篮子，第三行第四个为黑子。要进行数据存盘
 */
public class SparseArray {
    private static final String filePath = "D:\\work\\java\\study\\java-base\\src\\main\\java\\com\\mzl\\dataStructure\\chess.txt";

    public static void main(String[] args) throws IOException {
        // 假设棋盘是大小11*11
        // 0表示没有棋子，1表示黑子，2表示篮子
        int chessArr1[][] = new int[11][11];
        chessArr1[1][2] = 2;
        chessArr1[2][3] = 1;
        chessArr1[1][3] = 2;
        System.out.println("原始的二维数组~~");
        arrPrint(chessArr1);

        // 将二维数组 转 稀疏数组的思
        // 1. 先遍历二维数组 得到非0数据的个数
        int sum = getCountByNonZeroNumber(chessArr1);

        // 2. 创建对应的稀疏数组
        int chessArr2[][] = new int[sum + 1][3];
        // 给稀疏数组赋值
        chessArr2[0][0] = 11;
        chessArr2[0][1] = 11;
        chessArr2[0][2] = sum;

        // 遍历二维数组，将非0的值存放到 sparseArr中
        int count = 1; //count 用于记录是第几个非0数据
        for (int i = 0; i < chessArr1.length; i++) {
            for (int j = 0; j < chessArr1[i].length; j++) {
                if (chessArr1[i][j] != 0) {
                    chessArr2[count][0] = i;
                    chessArr2[count][1] = j;
                    chessArr2[count][2] = chessArr1[i][j];
                    count++;
                }
            }
        }
        System.out.println("遍历稀疏数组~~~~");
        arrPrint(chessArr2);
        // 存盘到本地
        dataSave(chessArr2);

        // 从本地磁盘读取存档
        int[][] readData = readData();
        //将稀疏数组 --》 恢复成 原始的二维数组
        //1. 先读取稀疏数组的第一行，根据第一行的数据，创建原始的二维数组\
        int[][] chessArr3 = new int[readData[0][0]][readData[0][1]];
        //2. 在读取稀疏数组后几行的数据(从第二行开始)，并赋给 原始的二维数组 即可

        for (int i = 1; i < readData.length; i++) {
            chessArr3[readData[i][0]][readData[i][1]] = readData[i][2];
        }

        System.out.println("还原的二维数组~~~~");
        arrPrint(chessArr3);
    }

    /**
     * 获取非0数字的个数
     */
    public static int getCountByNonZeroNumber(int[][] arr) {
        int sum = 0;
        for (int[] ints : arr) {
            for (int i = 0; i < 11; i++) {
                if (ints[i] != 0) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /**
     * 数字遍历
     *
     * @param arr
     */
    public static void arrPrint(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * 将数据保存到本地
     *
     * @param arr
     * @throws IOException
     */
    public static void dataSave(int[][] arr) throws IOException {
        //将数组传输到本地txt文件
        File file = new File(filePath);

        //创建字符写入流对象
        if (!file.exists()) {
            file.createNewFile();
        }
        //创建字符写入流对象
        FileWriter fileWriter = new FileWriter(filePath);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                fileWriter.write(arr[i][j] + "\t");
            }
            fileWriter.write("\r\n");
        }
        fileWriter.close();
    }

    /**
     * 从本地的稀疏数组TXT文件中读取稀疏数组
     *
     * @return
     */
    public static int[][] readData() throws IOException {
        //将数组传输到本地txt文件
        File file = new File(filePath);

        //判断文件是否存在
        if (!file.exists()) {
            throw new RuntimeException("文件不存在！");
        }

        BufferedReader in = new BufferedReader(new FileReader(filePath));

        //存储文件中的每一行数据
        String line;
        //因为不知道文件中的数据大小，所以使用集合存储数据
        List<String> list = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            //将数据添加在集合中
            list.add(line);
        }

        int row2 = 0;
        int sparseArr2[][] = new int[list.size()][3];

        for (String str : list) {
            String[] temp = str.split("\t");
            for (int i = 0; i < temp.length; i++) {
                sparseArr2[row2][i] = Integer.parseInt(temp[i]);
            }
            row2++;
        }
        /*for(int row3[] : sparseArr2) {
            for(int data : row3) {
                System.out.printf("%d\t",data);
            }
            System.out.printf("\n");
        }*/
        return sparseArr2;
    }
}
