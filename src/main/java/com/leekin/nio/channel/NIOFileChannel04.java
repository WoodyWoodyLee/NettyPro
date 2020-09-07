package com.leekin.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * @Author: Li Qing
 * @Create: 2020/9/1 21:37
 * @Version: 1.0
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        File fileDst = new File("C:\\Users\\QING\\Documents\\test\\abc.txt");
        FileInputStream fis = new FileInputStream(fileDst);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\QING\\Documents\\test\\hahah.txt");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();
       fisChannel.transferTo(0,fisChannel.size(),fosChannel);
    }
}
