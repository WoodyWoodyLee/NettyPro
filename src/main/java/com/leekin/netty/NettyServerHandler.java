package com.leekin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Li Qing
 * @Create: 2020/9/7 20:33
 * @Version: 1.0
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取实际数据
     * @param channelHandlerContext 上下文对象，含有pipeline,channel 地址
     * @param o 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("server ctx = " + channelHandlerContext);
        ByteBuf buffer = (ByteBuf) o;
        System.out.println("客户端发送的消息是：" + buffer.toString(StandardCharsets.UTF_8));
        System.out.println("客户端地址是：" + channelHandlerContext.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     *
     * @param ctx 上下文
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)  {
        //写入数据到缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",StandardCharsets.UTF_8));
    }

    //处理异常

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
