package com.leekin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Li Qing
 * @Create: 2020/9/7 21:35
 * @Version: 1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当通道就绪时出发该方法
     * @param ctx 发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello server:喵~~", StandardCharsets.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器回复的消息是：" + byteBuf.toString(StandardCharsets.UTF_8));
        System.out.println("服务器的地址是：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
