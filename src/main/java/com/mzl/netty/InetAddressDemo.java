package com.mzl.netty;

import java.net.InetAddress;

public class InetAddressDemo {

    public static void main(String[] args) throws Exception {
        InetAddress local = InetAddress.getLocalHost();
        InetAddress remote = InetAddress.getByName("www.baidu.com");
        System.out.println("本机的IP地址：" + local.getHostAddress());
        System.out.println("baidu的IP地址：" + remote.getHostAddress());
        System.out.println("baidu的主机名为：" + remote.getHostName());
    }

}
