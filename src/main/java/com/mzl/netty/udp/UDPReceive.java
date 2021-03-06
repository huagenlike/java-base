package com.mzl.netty.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 - UDP接收端
 -
 - 1,创建DatagramSocket对象
 - 2,创建DatagramPacket对象
 - 3,接收数据存储到DatagramPacket对象中
 - 4,获取DatagramPacket对象的内容
 - 5,释放流资源
 */
public class UDPReceive {
    public static void main(String[] args) throws IOException {
        //1,创建DatagramSocket对象,并指定端口号
        DatagramSocket receiveSocket = new DatagramSocket(12306);
        //2,创建DatagramPacket对象, 创建一个空的仓库
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, 1024);
        //3,接收数据存储到DatagramPacket对象中
        receiveSocket.receive(dp);
        //4,获取DatagramPacket对象的内容
        //谁发来的数据  getAddress()
        InetAddress ipAddress = dp.getAddress();
        //获取到了IP地址
        String ip = ipAddress.getHostAddress();
        //发来了什么数据  getData()
        byte[] data = dp.getData();
        //发来了多少数据 getLenth()
        int length = dp.getLength();
        //显示收到的数据
        String dataStr = new String(data,0,length);
        System.out.println("IP地址："+ip+ "数据是"+ dataStr);
        //5,释放流资源
        receiveSocket.close();
    }
}

