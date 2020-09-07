package com.leekin.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @Author: Li Qing
 * @Create: 2020/9/5 20:43
 * @Version: 1.0
 */
public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;

    //构造初始化
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        //注册channel
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到userName
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(userName + " is OK");
    }

    //发送消息
    public void sendMsg(String msg) {
        msg = userName + "say:" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMsg() {
        try {
            int count = selector.select();
            if (count > 0) {
                //有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        //相关通道
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                        iterator.remove();
                    }
                }
            } else {
                System.out.println("无可用通道");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient client = new GroupChatClient();
        //启动一个线程
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    client.readMsg();
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            client.sendMsg(str);
        }
    }
}
