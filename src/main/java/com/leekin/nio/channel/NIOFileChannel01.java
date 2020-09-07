package com.leekin.nio.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Li Qing
 * @Create: 2020/9/1 21:16
 * @Version: 1.0
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "庆神要去阿里摸鱼";
        FileOutputStream fos = new FileOutputStream("C:\\Users\\QING\\Documents\\test\\abc.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel channel = fos.getChannel();
        byteBuffer.put(str.getBytes(StandardCharsets.UTF_8));
        //通道反转
        byteBuffer.flip();
        channel.write(byteBuffer);
    }
}
