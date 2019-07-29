package com.learn.ionio.socketNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {

	public void serve(int port) throws IOException {

		//打开服务端 Socket 监听port端口, 并配置为非阻塞模式
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		ServerSocket ssocket = serverSocketChannel.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		ssocket.bind(address);

		// 创建selector
		// 将 channel 注册到 selector中.
        Selector selector = Selector.open();
        // 通常我们都是先注册一个 OP_ACCEPT 事件, 然后在 OP_ACCEPT 到来时, 再将这个 Channel的 OP_READ注册到 Selector 中.
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		final ByteBuffer msg = ByteBuffer.wrap("Helloworld".getBytes());

		for (;;) {
			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			//获取 I/O操作就绪的 SelectionKey,通过 SelectionKey可以知道哪些 Channel的哪类 I/O操作已经就绪.
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				//当获取一个 SelectionKey 后, 就要将它删除, 表示我们已经对这个 IO 事件进行了处理.
				iterator.remove();

				try {
					//连接请求
					if (key.isAcceptable()) {
						//当 OP_ACCEPT事件到来时,我们就有从 ServerSocketChannel中获取一个 SocketChannel代表客户端的连接
	                    //注意, 在 OP_ACCEPT 事件中,从 key.channel()返回的 Channel是 ServerSocketChannel.
	                    //而在 OP_WRITE 和 OP_READ 中,从 key.channel()返回的是SocketChannel.
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						client.configureBlocking(false);
						//在 OP_ACCEPT 到来时,再将这个 Channel的 OP_READ 注册到 Selector 中.
	                    //注意,这里我们如果没有设置 OP_READ的话,即 interest set仍然是 OP_CONNECT的话,那么 select方法会一直直接返回.
						client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
						System.out.println("连接的客户端：" + client);
					}
					
					//读请求
                    if (key.isReadable()) {
                        System.out.println();
                    }

					if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						while (buffer.hasRemaining()) {
							if (client.write(buffer) == 0) {
								break;
							}
						}
						client.close();
					}
				} catch (IOException e) {
					key.cancel();
					try {
						key.channel().close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}

		}
	}

	public static void main(String[] args) throws IOException {
		PlainNioServer server = new PlainNioServer();
		server.serve(6666);
	}

}
