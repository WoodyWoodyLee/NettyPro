package com.leekin.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Li Qing
 * @Create: 2020/9/5 20:14
 * @Version: 1.0
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    // 初始化
    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            while (true) {

                int count = selector.select(2000);
                //有事件处理
                if (count > 0) {
                    Set<SelectionKey> keySet = selector.keys();
                    Iterator<SelectionKey> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }
                        if (key.isReadable()) {
                            //处理读
                            handleReadOP(key);
                        }
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待中....");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读操作处理
    private void handleReadOP(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("form 客户端：" + msg);
                //转发消息给其他客户端
                noticeAllClient(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "下线了");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }


    }

    private void noticeAllClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && !targetChannel.equals(self)) {
                SocketChannel channel = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                channel.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }
}
