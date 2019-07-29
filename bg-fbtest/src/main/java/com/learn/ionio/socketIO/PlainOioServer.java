package com.learn.ionio.socketIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class PlainOioServer {
	public void serve(int port) throws IOException {
        // 开启Socket服务器，并监听端口
        final ServerSocket socket = new ServerSocket(port);
        try{
        	System.out.println("等待客户端链接");
            for(;;){
                // 轮训接收监听
                final Socket clientSocket = socket.accept();
                System.out.println("accepted connection from "+clientSocket);
                // 创建新线程处理请求
                new Thread(()->{
                	try {    
                        // 读取客户端数据    
                        DataInputStream input = new DataInputStream(clientSocket.getInputStream());  
                        String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException  
                        // 处理客户端数据    
                        System.out.println("客户端发过来的内容:" + clientInputStr);    
            
                        // 向客户端回复信息    
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());    
                        System.out.print("请输入:\t");    
                        // 发送键盘输入的一行    
                        String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
                        out.writeUTF(s);    
                          
                        out.close();    
                        input.close();    
                    } catch (Exception e) {    
                        System.out.println("服务器 run 异常: " + e.getMessage());    
                    } finally {    
                        if (clientSocket != null) {    
                            try {    
                            	clientSocket.close();    
                            } catch (Exception e) {    
                            	System.out.println("服务端 finally 异常:" + e.getMessage());    
                            }    
                        }    
                    }   
                }).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        PlainOioServer server = new PlainOioServer();
        server.serve(5555);
    }

    /*
    这种阻塞模式的服务器，原理上很简单，问题也容易就暴露出来：
 	1.服务端与客户端的连接相当于1:1，因此如果连接数上升，服务器的压力会很大
	2.如果主线程Acceptor阻塞，那么整个服务器将会阻塞，单点问题严重
	3.线程数膨胀后，整个服务器性能都会下降
    改进的方式可以基于线程池或者消息队列，不过也存在一些问题：
	1.线程池的数量、消息队列后端服务器并发处理数，都是并发数的限制
   	2.仍然存在Acceptor的单点阻塞问题
    */
}
