package com.mzl.netty.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * JDK 原生的 NIO 来实现服务端:
 * 相对于IOServer而言，这里就用了两个线程来处理，减少了线程的开销
 */
public class JDKNIOServer {

    public static void main(String[] args) throws IOException {
        // Selector：轮询器
        // 创建选择器，负责轮询是否有新的连接
        Selector serverSelector = Selector.open();
        // 创建选择器，负责轮询是否有数据刻度
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                // 对应IO编程中服务端启动
                // 打开监听信道
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                // 与本地端口绑定
                listenerChannel.socket().bind(new InetSocketAddress(8000));
                // 设置为非阻塞模式
                listenerChannel.configureBlocking(false);
                // 将选择器绑定到监听信道,只有非阻塞信道才可以注册选择器.并在注册过程中指出该信道可以进行Accept操作
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                // 反复循环,等待IO
                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为 1ms
                    if (serverSelector.select(1) > 0) {
                        // 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        // Selected-key Iterator 代表了所有通过 select() 方法监测到可以进行 IO 操作的 channel，这个集合可以通过 selectedKeys() 拿到
                        Iterator<SelectionKey> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isAcceptable()) {
                                try {
                                    // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                } finally {
                                    // 移除处理过的键
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {

            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为 1ms
                    if (clientSelector.select(1) > 0) {
                        Set<SelectionKey> set = clientSelector.selectedKeys();
                        Iterator<SelectionKey> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isReadable()) {
                                try {
                                    SocketChannel clientChannel = (SocketChannel) key.channel();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    // (3) 面向 Buffer
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer)
                                            .toString());
                                } finally {
                                    iterator.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {

            }
        }).start();
    }

}
