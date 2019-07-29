package com.learn.ionio.socketNIO;

import java.nio.ByteBuffer;

public class ByteBufferTest {
	
	/*
	Buffer 的几个属性
		1.capacity——容量，这个值是一开始申请就确定好的。类似c语言申请数组的大小。
		2.limit——剩余，在写模式下初始的时候等于capacity；在读模式下，等于最后一次写入的位置
		3.mark——标记位，标记一下position的位置，可以调用reset()方法回到这个位置。
		4.posistion——位置，写模式下表示开始写入的位置；读模式下表示开始读的位置
	总结来说，NIO的Buffer有两种模式，读模式和写模式。刚上来就是写模式，使用flip()可以切换到读模式。 
	*/
	
	public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(80);
        System.out.println(buffer);

        String value = "Netty权威指南";
        buffer.put(value.getBytes());//写入buffer
        System.out.println(buffer);

        buffer.flip();//读
        System.out.println(buffer);

        byte[] v = new byte[buffer.remaining()];//从buffer中获取
        buffer.get(v);

        System.out.println(buffer);
        System.out.println(new String(v));
    }
}
