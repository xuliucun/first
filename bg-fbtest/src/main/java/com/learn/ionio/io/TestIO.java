package com.learn.ionio.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class TestIO {
	@SuppressWarnings("unused")
	public void test01() throws Exception{
		// 字节输入流操作
		InputStream input = new ByteArrayInputStream("abcd".getBytes());
		int data = input.read();
		while(data != -1){
		    System.out.println((char)data);
		    data = input.read();
		}

		// 字节输出流
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write("12345".getBytes());
		byte[] ob = output.toByteArray();
	}
	
	
	
	
	@SuppressWarnings("unused")
	public void test02() throws Exception{
		// 字符输入流操作
		Reader reader = new CharArrayReader("abcd".toCharArray());
		int data = reader.read();
		while(data != -1){
		    System.out.println((char)data);
		    data = reader.read();
		}
		// 字符输出流
		CharArrayWriter writer = new CharArrayWriter();
		writer.write("12345".toCharArray());
		char[] wc = writer.toCharArray();
	}
	
	public void test03(){
		//流的关闭
		InputStream input = null;
		try {
		    input = new ByteArrayInputStream("abcd".getBytes());
		    int data = input.read();
		    while (data != -1) {
		        System.out.println((char) data);
		        // todo
		        data = input.read();
		    }
		}catch(Exception e){
		    // todo
		}finally {
		    if(input != null) {
		        try {
		            input.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	}

}
