package com.mzl.netty.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 文件上传  服务器端
 */
public class FileUploadTCPServer {

    public static void main(String[] args) throws IOException {
        //1,创建服务器，等待客户端连接
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket clientSocket = serverSocket.accept();
        //显示哪个客户端Socket连接上了服务器
        InetAddress ipObject = clientSocket.getInetAddress();//得到IP地址对象
        String ip = ipObject.getHostAddress(); //得到IP地址字符串
        System.out.println("小样，抓到你了，连接我！！" + "IP:" + ip);

        //7,获取Socket的输入流
        InputStream in = clientSocket.getInputStream();
        //8,创建目的地的字节输出流   D:\\upload\\192.168.74.58(1).jpg
        BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Administrator\\Desktop\\img\\蒙牛111.jpg"));
        //9,把Socket输入流中的数据，写入目的地的字节输出流中
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            //写入目的地的字节输出流中
            fileOut.write(buffer, 0, len);
        }

        //-----------------反馈信息---------------------
        //10,获取Socket的输出流, 作用：写反馈信息给客户端
        OutputStream out = clientSocket.getOutputStream();
        //11,写反馈信息给客户端
        out.write("图片上传成功".getBytes());

        out.close();
        fileOut.close();
        in.close();
        clientSocket.close();
        //serverSocket.close();
    }

}
