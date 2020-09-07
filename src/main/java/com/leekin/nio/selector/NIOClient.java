package com.leekin.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: Li Qing
 * @Create: 2020/9/5 15:41
 * @Version: 1.0
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器IP和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(address)){
            while (!socketChannel.finishConnect()){
                System.out.println("正在连接，客户端不会阻塞，可以做其他工作");
            }
        }
        //接连成功
        String str = "hello,I am a java coder~";
        //写入buffer
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
