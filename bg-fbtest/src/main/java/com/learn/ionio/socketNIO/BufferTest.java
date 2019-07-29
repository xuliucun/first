package com.learn.ionio.socketNIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferTest {
	public static void main(String[] args) throws IOException {
        String file = "src/main/resources/io/test.txt";
        @SuppressWarnings("resource")
		RandomAccessFile accessFile = new RandomAccessFile(file,"rw");
        FileChannel fileChannel = accessFile.getChannel();
        // 20个字节
        ByteBuffer buffer = ByteBuffer.allocate(20);
        int bytesRead = fileChannel.read(buffer);
        // buffer.put()也能写入buffer
        while(bytesRead!=-1){
        	System.out.println(bytesRead);
            // 写切换到读
            buffer.flip();

            while(buffer.hasRemaining()){
                System.out.println((char)buffer.get());
            }

            // buffer.rewind()重新读
            // buffer.mark()标记position buffer.reset()恢复

            // 清除缓冲区
            buffer.clear();
            // buffer.compact(); 清除读过的数据
            bytesRead = fileChannel.read(buffer);
        }
    }

}
