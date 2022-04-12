package com.mzl.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDemo {
    public static void main(String[] args) {
        // 构造两个线程组

        // 接收客户端传过来的请求
        NioEventLoopGroup bosserGroup = new NioEventLoopGroup();
        // 接收到请求后将后续操作交由 workerGroup 处理
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 服务端启动辅助类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bosserGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // childHandler 方法用于设置业务职责链
                    .childHandler(new HttpServerInitializer());

            // sync 方法用于阻塞当前 Thread，一直到端口绑定操作完成
            ChannelFuture future = bootstrap.bind(8080).sync();
            // 等待服务端口关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bosserGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
