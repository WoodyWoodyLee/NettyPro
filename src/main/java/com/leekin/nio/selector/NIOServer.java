package com.leekin.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Li Qing
 * @Create: 2020/9/5 15:23
 * @Version: 1.0
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        //服务端邦定并监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        //注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //如果有链接,获得key集合
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                //获取selectionkey
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    //OP_ACCEPT
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //将socketchannel注册到selector，带上一个bytebuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (selectionKey.isReadable()) {
                    //发生OP_READ
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    //获取该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("from client:" + new String(buffer.array()));
                }
                //手动从集合中删除当前的selectorKey
                iterator.remove();

            }
        }
    }
}
