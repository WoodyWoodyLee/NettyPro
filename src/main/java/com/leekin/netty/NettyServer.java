package com.leekin.netty;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 服务端
 *
 * @Author: Li Qing
 * @Create: 2020/9/7 20:19
 * @Version: 1.0
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建Boss和WorkGroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //使用链式编程来设置
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)              //拦截通道
                    .option(ChannelOption.SO_BACKLOG, 128)        //线程连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());         //设置对应管道的处理器
                        }
                    })
            ;
            System.out.println("Server is Ready...");
            //启动服务器，绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
