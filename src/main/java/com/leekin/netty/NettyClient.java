package com.leekin.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: Li Qing
 * @Create: 2020/9/7 21:29
 * @Version: 1.0
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端时间循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端启动对象 注意客户端使用的是bootstrap
        Bootstrap bootstrap = new Bootstrap();
        try {
            //设置相关参数
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("客户端OK...");
            //启动客户端去链接服务端
            bootstrap.connect("127.0.0.1", 6668).sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
