package com.leekin.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * @Author: Li Qing
 * @Create: 2020/9/1 21:37
 * @Version: 1.0
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\QING\\Documents\\test\\abc.txt");
        FileInputStream fis = new FileInputStream(file);
        ByteBuffer byteBuffer = ByteBuffer.allocate(((int) file.length()));
        FileChannel channel = fis.getChannel();
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
    }
}
