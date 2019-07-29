package com.learn.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufTest {
    public static void main(String[] args) {
        //创建bytebuf
        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
        System.out.println(buf);

        // 读取一个字节
        buf.readByte();
        System.out.println(buf);

        // 读取一个字节
        buf.readByte();
        System.out.println(buf);

        // 丢弃无用数据
        buf.discardReadBytes();
        System.out.println(buf);

        // 清空
        buf.clear();
        System.out.println(buf);

        // 写入
        buf.writeBytes("123".getBytes());
        System.out.println(buf);

        buf.markReaderIndex();
        System.out.println("mark:"+buf);

        buf.readByte();
        buf.readByte();
        System.out.println("read:"+buf);

        buf.resetReaderIndex();
        System.out.println("reset:"+buf);
    }
}
