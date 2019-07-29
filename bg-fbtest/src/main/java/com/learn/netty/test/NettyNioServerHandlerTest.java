package com.learn.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/*
 * 注意：
 * Handler包含了Inbound和Outbound两种，他们统一放在一个双向链表中：
 * 
 * 接收  InboundA OutboundA InboundB OutboundB OutboundC InboundC 发送
 * 		InboundA --> InboundB --> InboundC
 * 		OutboundC --> OutboundB --> OutboundA
 * 		ChannelOutboundHandler要在最后一个Inbound之前
 */
public class NettyNioServerHandlerTest {
	public static void main(String[] args) throws InterruptedException {
		NettyNioServerHandlerTest server = new NettyNioServerHandlerTest();
		server.serve(5555);
	}

	final static ByteBuf buffer = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Hi\r\n", Charset.forName("UTF-8")));

	public void serve(int port) throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast("1", new InboundA());
							pipeline.addLast("2", new OutboundA());
							pipeline.addLast("3", new InboundB());
							pipeline.addLast("4", new OutboundB());
							pipeline.addLast("5", new OutboundC());
							pipeline.addLast("6", new InboundC());
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
	}

	private static class InboundA extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println("InboundA read 可做处理类似拦截器" + buf.toString(Charset.forName("UTF-8")));
			super.channelRead(ctx, msg);
		}
	}

	private static class InboundB extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println("InboundB read 可做处理类似拦截器" + buf.toString(Charset.forName("UTF-8")));
			super.channelRead(ctx, msg);
			// 从pipeline的尾巴开始找outbound
			ctx.channel().writeAndFlush(buffer);
		}
	}

	private static class InboundC extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			System.out.println("InboundC read 可做处理类似拦截器" + buf.toString(Charset.forName("UTF-8")));
			super.channelRead(ctx, msg);
			// 这样会从当前的handler向前找outbound
			// ctx.writeAndFlush(buffer);
		}
	}

	private static class OutboundA extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			System.out.println("OutboundA write 可做返回处理类似拦截器");
			super.write(ctx, msg, promise);
		}
	}

	private static class OutboundB extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			System.out.println("OutboundB write 可做返回处理类似拦截器");
			super.write(ctx, msg, promise);
		}
	}

	private static class OutboundC extends ChannelOutboundHandlerAdapter {
		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			System.out.println("OutboundC write 可做返回处理类似拦截器");
			super.write(ctx, msg, promise);
		}
	}
}