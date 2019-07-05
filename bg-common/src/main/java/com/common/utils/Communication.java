package com.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Properties;


import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;


/**
 * 通讯类
 */
public class Communication {

	protected static Logger logger = Logger.getLogger(Communication.class);

	/*
	 * MQ全局变量
	 */
	private static MQQueueManager qManager = null;
	private static MQQueue sendQueue = null;
	private static MQPutMessageOptions sendOption = null;
	private static int sendOpenOption = 0;
	private static MQQueue receiveQueue = null;
	private static MQGetMessageOptions receiveOption = null;
	private static int receiveOpenOption = 0;
	
	/**
	 * Socket通讯发送约束数据
	 * 发送的数据是分批次发送,循环发送1024字节数据。
	 * @param ip ip地址
	 * @param port 端口号
	 * @param byteData 发送报文字节数据
	 * @param constraintByte 约束字节长度
	 * @param encoding 发送与返回的编码格式
	 * @return String 返回String类型返回报文
	 * @throws Exception 抛异常
	 */
	private byte[] sendSocketConstraint(String ip, int port, byte[] byteData, int constraintByte, String encoding) throws Exception {
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		byte[] returnByteContent;
		try {
			socket = new Socket(ip, port);
			socket.setSoLinger(true, 0);
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			
			// 字节输出流
			outputStream = socket.getOutputStream();
			/*
			 * 发送报文字节流处理
			 * 获取发送报文的字节数，因核心一次只接受1024字节
			 */
			//此字节约束发送不完整
			int sendMessageContentLength = byteData.length;
			for (int i = 0; i < byteData.length; i = i + constraintByte) {
				byte[] sendByteItem = new byte[byteData.length < i + constraintByte ? byteData.length - i: constraintByte];
				System.arraycopy(byteData, i, sendByteItem, 0, sendByteItem.length);
				
				byte[] sendByteHead = int2byte(sendMessageContentLength);
				sendMessageContentLength = sendMessageContentLength - sendByteItem.length;
				
				byte[] sendByte = new byte[sendByteItem.length + 4];
				System.arraycopy(sendByteHead, 0, sendByte, 0, 4);
				System.arraycopy(sendByteItem, 0, sendByte, 4, sendByteItem.length);
				
				outputStream.write(sendByte);
			}
			outputStream.flush();
			
			// 获取响应报文
			inputStream = socket.getInputStream();
			byte[] returnHead = new byte[4];
			inputStream.read(returnHead, 0, 4);
			int returnByteContentSumLength = byte2int(returnHead);
			returnByteContent = new byte[returnByteContentSumLength];
			for (int i = returnByteContentSumLength; i > 0; i = i - constraintByte) {
				inputStream.read(returnByteContent, returnByteContentSumLength - i, i > constraintByte ? constraintByte : i);
				if (i < constraintByte) {
					continue;
				}
				inputStream.read(returnHead, 0, 4);
				if (i - constraintByte != byte2int(returnHead)) {
					throw new Exception("over message length is error!");
				}
			}
			//字符转换
			return returnByteContent;
			//return new String(returnByteContent,encoding);
		} catch (UnknownHostException e) {
			logger.error("网络连接异常");
			throw new Exception("SOCKET网络连接异常");
		} catch (IOException e) {
			logger.error("IO异常");
			throw new Exception("SOCKET IO异常");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				logger.error("inputStream流关闭失败！" + e2);
				e2.printStackTrace();
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e3) {
					logger.error("outputStream流关闭失败！" +e3);
					e3.printStackTrace();
				} finally {
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (Exception e4) {
						logger.error("socket流关闭失败！" + e4);
						e4.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Socket通讯发送全部数据
	 * @param ip ip地址
	 * @param port 端口
	 * @param byteData 发送字节数据
	 * @param encoding 发送与返回编码格式
	 * @return String 返回String类型返回报文
	 * @throws Exception 抛异常
	 */
	private byte[] sendSocketAll(String ip, int port, byte[] byteData, String encoding) throws Exception {
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			socket = new Socket(ip, port);
			socket.setSoLinger(true, 0);
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			
			// 字节输出流发送字节
			outputStream = socket.getOutputStream();
			outputStream.write(byteData);
			outputStream.flush();
			// 关闭输出流
			socket.shutdownOutput();

			// 获取字节输入流
			inputStream = socket.getInputStream();
			
			ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			if (inputStream != null) {
				int len = 0;
				while ((len = inputStream.read(data)) != -1) {
					outputStream1.write(data, 0, len);
				}
			}
			return outputStream1.toByteArray();
		} catch (IOException e) {
			logger.error("IO异常");
			throw new Exception("SOCKET IO异常");
		} finally {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				logger.error("inputStream流关闭失败！" + e2);
				e2.printStackTrace();
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e3) {
					logger.error("outputStream流关闭失败！" +e3);
					e3.printStackTrace();
				} finally {
					try {
						if (socket != null) {
							socket.close();
						}
					} catch (Exception e4) {
						logger.error("socket流关闭失败！" + e4);
						e4.printStackTrace();
					}
				}
			}
		}
	}
	
	
	/**
	 * Socket通讯发送同步数据
	 * @param ip ip地址
	 * @param port 端口
	 * @param outTime 
	 * @param bt 报文信息二进制数组
	 * @return Object 返回Object类型返回报文
	 * @throws Exception 抛异常
	 */
	@SuppressWarnings("unused")
	private Object sendSocketSynchronized(String ip, int port, int outTime, byte[] bt) throws Exception {
		logger.info("进入TCP发送：ip" + ip + "port" + port + "outTime:" + outTime);
		Socket client = null;
		int packLength = 256;
		boolean isSyn = true;
		byte synBuf[] = new byte[3];
		synBuf[0] = 0x02;
		synBuf[1] = 0x00;
		synBuf[2] = 0x00;

		String str = "";
		try {
			client = new Socket(ip, port);
			client.setSoTimeout(outTime);
			OutputStream out = client.getOutputStream();
			DataOutputStream dout = new DataOutputStream(out);
			int i = 0;
			while (true) {
				int leftLen = bt.length - i * packLength;
				System.out.println("leftLen==" + leftLen);
				int currLen = leftLen > packLength ? packLength : leftLen;
				System.out.println("currLen==" + currLen);
				if (leftLen <= 0) {
					break;
				}
				// 开始发送 /** 同步头**/
				// if (isSyn) {
				// dout.write(synBuf, 0, 3);
				// }
				/** 包长 */
				// dout.writeShort(leftLen);

				/** 包体 */
				byte[] pkgBody = new byte[currLen];
				System.arraycopy(bt, i * packLength, pkgBody, 0, currLen);
				dout.write(pkgBody);
				i++;
			}
			dout.flush();
			DataInputStream din = new DataInputStream(client.getInputStream());
			int len = din.readShort();
			int readLength = len > packLength ? packLength : len;
			byte[] b = new byte[readLength];
			din.skip(3);
			din.readFully(b);
			din.close();
			dout.close();
			client.close();
			return str + "," + new String(b, 0, b.length, "GB2312");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new Exception("SOCKET网络连接异常");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("SOCKET IO异常！");
		} finally {
			if (client != null)
				client.close();
		}
	}
	
	/**
	 * Http通讯(POST/GET)
	 * @param url
	 *            请求地址
	 * @param sendMessage
	 *            请求的数据
	 * @param requestMethod
	 *            请求的方法 GET、POST、HEAD、OPTIONS、PUT、DELETE、TRACE, 默认为GET
	 * @param contentType
	 *            1.application/x-www-form-urlencoded:这应该是最常见的 POST提交数据的方式了。浏览器的原生form表单（默认）
	 *            2.multipart/form-data:使用表单上传文件时，必须让form 的 enctyped 等于这个值。 
	 *            3.application/json:用来告诉服务端消息主体是序列化后的JSON 字符串。
	 *            4.text/xml:XML 作为编码方式的远程调用规范。
	 * @param outTime 连接超时时间
	 * 
	 * @param encoding
	 *            发送报文与返回报文的编码格式
	 * @return String 
	 * 			    返回String类型返回报文
	 */
	private String sendHttpProtocol(Object url, String sendMessage,String requestMethod, String contentType, int outTime, String encoding) {
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		PrintWriter printWriter = null;
		try {
			URL realUrl = new URL(url.toString());
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setConnectTimeout(outTime); // 表示连接超时
			if (!requestMethod.equals("") && requestMethod != null) {
				httpURLConnection.setRequestMethod(requestMethod); // 表示请求方式
			}else{
				httpURLConnection.setRequestMethod("POST");
			}
			httpURLConnection.setDoInput(true);// 表示从服务器获取数据
			httpURLConnection.setDoOutput(true);// 表示向服务器写数据
			httpURLConnection.setRequestProperty("accept", "*/*"); // 接受什么介质类型*/*表示任何类型，type/*表示该类型下的所有子类型，type/sub-type。
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			if (!"".equals(contentType) && contentType != null) {
				httpURLConnection.setRequestProperty("Content-type",contentType);
			}else{
				httpURLConnection.setRequestProperty("Content-type","application/x-www-form-urlencoded");
			}
			if(!encoding.equals("") && encoding != null){
				httpURLConnection.setRequestProperty("Content-encoding",encoding);
			}else{
			    httpURLConnection.setRequestProperty("Content-encoding","GBK");
			    encoding = "GBK";
			}
			logger.info("Http客户端开始进行连接");
			// 建立实际的连接
			httpURLConnection.connect();
			// 获得输出流,向服务器输出数据
			printWriter = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), encoding));
			printWriter.println(sendMessage);
			printWriter.flush();
			// 获取输入流
			inputStream = httpURLConnection.getInputStream();
			// 获得服务器响应的结果和状态码
			// int responseCode = httpURLConnection.getResponseCode();
			return changeInputStream(inputStream, encoding);
		} catch (Exception e) {
			logger.info("发送" + requestMethod + "请求方式出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e3) {
				logger.error("inputStream流关闭失败!");
				e3.printStackTrace();
			} finally {
				try {
					if (printWriter != null) {
						printWriter.close();
					}
				} catch (Exception e2) {
					logger.error("printWriter流关闭失败!");
					e2.printStackTrace();
				} finally {
					try {
						if (httpURLConnection != null) {
							httpURLConnection.disconnect();
						}
					} catch (Exception e4) {
						logger.error("httpURLConnection流关闭失败!");
						e4.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 发送IBMMQ
	 * @param ip MQ所在主机的ip
	 * @param port MQ所在主机的端口
	 * @param sendMessage 发送报文
	 * @param _mqManager 队列管理器
	 * @param _sendQueue 发送队列
	 * @param _receiveQueue 接收队列
	 * @param channel 渠道
	 * @param ccsid 编码字符集
	 * @return String 返回String类型返回报文
	 */
	private String sendIBMMq(String ip, int port, String sendMessage, String _mqManager, String _sendQueue, String _receiveQueue, String channel, int ccsid, String enconding) {
		try {
			MQEnvironment.hostname = ip;
			MQEnvironment.port = port;
			MQEnvironment.CCSID = ccsid;
			MQEnvironment.channel = channel;
			// QueueManager openOptions
			qManager = new MQQueueManager(_mqManager);
			sendOpenOption = MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INQUIRE;
			sendQueue = qManager.accessQueue(_sendQueue, sendOpenOption);
			sendOption = new MQPutMessageOptions(true);

			receiveOpenOption = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT | MQC.MQOO_FAIL_IF_QUIESCING | MQC.MQOO_INQUIRE;
			receiveQueue = qManager.accessQueue(_receiveQueue, receiveOpenOption);
			receiveOption = new MQGetMessageOptions(true);
			receiveOption.waitInterval = 100000;
			receiveOption.options = MQC.MQGMO_WAIT;
			
			// MQ发送方法
			byte[] mesId = mqsend(sendMessage, _mqManager, _receiveQueue, enconding);
			// MQ返回方法
			String string = returnMessage(mesId);
			return string;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关流
			destory();
		}
		return null;
	}
	
	/**
	 * MQ关流
	 */
	private void destory() {
		try {
			if (sendQueue != null) {
				sendQueue.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (receiveQueue != null) {
				receiveQueue.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (qManager != null) {
				qManager.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * MQ发送方法
	 * @param sendMessage 发送报文
	 * @param _mqManager 队列管理器
	 * @param _receiveQueue 接受队列
	 * @param encoding 发送报文编码格式
	 * @return byte[] 返回byte[]
	 */
	private byte[] mqsend(String sendMessage, String _mqManager, String _receiveQueue, String encoding) {
		try {
			byte[] sendData;
			if ("".equals(encoding) || encoding == null) {
				sendData = sendMessage.getBytes();
			} else {
				sendData = sendMessage.getBytes(encoding);
			}
			MQMessage msgSend = new MQMessage();
			msgSend.replyToQueueManagerName = _mqManager;
			msgSend.replyToQueueName = _receiveQueue;
			msgSend.messageType = 1;
			// System.out.println(">>>"+new String(reqFile));
			msgSend.write(sendData);
			msgSend.persistence = 0;
			sendQueue.put(msgSend, sendOption);
			return msgSend.messageId;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MQException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * MQ返回方法
	 * @param mesId messageId
	 * @return String 返回String类型返回报文
	 */
	private String returnMessage(byte[] mesId) {
		try {
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = gmo.options + MQC.MQGMO_SYNCPOINT;
			gmo.options = gmo.options + MQC.MQGMO_WAIT;
			gmo.options = gmo.options + MQC.MQGMO_FAIL_IF_QUIESCING;
			MQMessage inMsg = new MQMessage();
			String resMsg = "";
			if (sendQueue.isOpen()) {
				sendQueue.get(inMsg, gmo);
				mesId = inMsg.correlationId;
				qManager.commit();
				resMsg = inMsg.readStringOfByteLength(inMsg.getDataLength());
			}
			return resMsg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送webService
	 * @param url
	 *            ws服务方路径
	 * @param callMethod
	 *            调用的方法
	 * @param param
	 *            以数组形式传递的参数
	 * @return Object 
	 * 			   服务方返回Object类型返回报文
	 */
	private Object sendWebSeviceByAxis(String url, String callMethod, Object[] param) {
		Object result = null;
		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(url));
			call.setOperationName(callMethod);// 调用的方法名
			result = call.invoke(param);// 传递参数
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	 
	
	/**
	 * 发送Http的POST请求方式
	 * @param url url地址
	 * @param sendMessage 发送数据
	 * @param encoding 发送与返回编码格式
	 * @return String 服务方返回String类型返回报文
	 */
	@SuppressWarnings("unused")
	private String sendHttpPost(Object url, String sendMessage, String encoding) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		String returnCoontentMessage = "";
        try {
        	URL realUrl = new URL(url.toString());
            // 打开和URL之间的连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            httpURLConnection.setConnectTimeout(3000); // 表示连接超时
            httpURLConnection.setRequestMethod("POST"); // 表示请求方式
            httpURLConnection.setDoInput(true);// 表示从服务器获取数据 
            httpURLConnection.setDoOutput(true);// 表示向服务器写数据
            httpURLConnection.setRequestProperty("accept", "*/*");
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            /*
             * 如果不设置Content-type属性，那么最终就会以 application/x-www-form-urlencoded 方式提交数据。
             * Http四种常见的POST提交数据方式
             * 1.application/x-www-form-urlencoded:这应该是最常见的 POST 提交数据的方式了。浏览器的原生form表单
             * 2.multipart/form-data:使用表单上传文件时，必须让 form 的 enctyped 等于这个值。
             * 3.application/json:用来告诉服务端消息主体是序列化后的 JSON 字符串。
             * 4.text/xml:XML 作为编码方式的远程调用规范。
             */
            httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("Content-encoding", encoding);
			logger.info("Http客户端开始进行连接");
			// 建立实际的连接
			httpURLConnection.connect();
			// 获得输出流,向服务器输出数据
			printWriter=new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),encoding));
			printWriter.println(sendMessage);
			printWriter.flush();
			
			// 获取Http服务端返回报文
			// 获取输入流
			inputStream = httpURLConnection.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				returnCoontentMessage = returnCoontentMessage + line;
			}
		    // 获得服务器响应的结果和状态码 
//		    int responseCode = httpURLConnection.getResponseCode(); 
//		    if(responseCode == 200) {
//		    	return changeInputStream(inputStream, encoding); 
//		     }
		    // 断开连接
		    httpURLConnection.disconnect();
		} catch (Exception e) {
			logger.error("发送POST请求出现异常!" + e);
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e3) {
				logger.error("inputStream流关闭失败!");
				e3.printStackTrace();
			} finally {
				try {
					if (printWriter != null) {
						printWriter.close();
					}
				} catch (Exception e2) {
					logger.error("printWriter流关闭失败!");
					e2.printStackTrace();
				}
			}
		}
		return returnCoontentMessage;
	}
	
	/**
	 * 发送Http的GET请求方式
	 * @param url url地址
	 * @param sendMessage 发送数据
	 * @param encoding 发送与返回报文编码格式
	 * @return String 返回String类型的报文数据
	 */
	@SuppressWarnings("unused")
	private String sendHttpGet(Object url, String sendMessage, String encoding) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		String returnCoontentMessage = "";
		try {
			// Get请求url地址
			String urlNameString = url + "?" + sendMessage;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			httpURLConnection.setConnectTimeout(3000); // 表示连接超时
			httpURLConnection.setRequestMethod("GET"); // 表示请求方式
			httpURLConnection.setDoInput(true);// 表示从服务器获取数据
			httpURLConnection.setDoOutput(true);// 表示向服务器写数据
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpURLConnection.setRequestProperty("Content-encoding", encoding);
			logger.info("Http客户端开始进行连接");
			// 建立实际的连接,但是实际上GET请求要在下一句的httpURLConnection.getInputStream()函数中才会真正发到服务器
			httpURLConnection.connect();
			// 获取所有响应头字段
//            Map<String, List<String>> map = httpURLConnection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            printWriter=new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),encoding));
			printWriter.println(sendMessage);
			printWriter.flush();
			// 获取Http服务端返回报文
			// 获取输入流
			inputStream = httpURLConnection.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				returnCoontentMessage = returnCoontentMessage + line;
			}
			// 获得服务器响应的结果和状态码
//			int responseCode = httpURLConnection.getResponseCode();
//			if (responseCode == 200) {
//				return changeInputStream(inputStream);
//			}
			// 断开连接
			httpURLConnection.disconnect();
		} catch (Exception e) {
			logger.error("发送GET请求出现异常!" + e);
			e.printStackTrace();
		} finally {
			// 关闭资源,先用后关
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e3) {
				logger.error("inputStream流关闭失败!");
				e3.printStackTrace();
			} finally {
				try {
					if (printWriter != null) {
						printWriter.close();
					}
				} catch (Exception e2) {
					logger.error("printWriter流关闭失败!");
					e2.printStackTrace();
				}
			}
		}
		return returnCoontentMessage;
	}

	/**
	 * 字节转int
	 * @param b 字节数组
	 * @return int 返回int类型的数字
	 */
	private static int byte2int(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = 0; i < 4; i++) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

	/**
	 * int转字节
	 * @param n int类型数字
	 * @return byte[] 返回byte[]类型字节数组
	 */
	private static byte[] int2byte(int n) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}
	
	/**
	 * 将一个输入流转换成指定编码的字符串
	 * @param inputStream 输入流
	 * @param encoding 发送与返回报文编码格式
	 * @return String 返回String类型数据
	 */
	private String changeInputStream(InputStream inputStream, String encoding) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = null;
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
//				result = outputStream.toByteArray();
				result = new String(outputStream.toByteArray(), encoding);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
   
	
	/**
	 * 发送通讯方法
	 * @param sendMessage 发送报文
	 * @return String 返回String类型报文数据
	 * @throws Exception 抛异常
	 */
	@SuppressWarnings("static-access")
	//TODO   sendMessage 如何定义类型？？？？？
	public Object send(Object sendMessage, Properties properties) throws Exception {
		// 返回报文
		Object returnMessage = null;
			
		// 判断通讯方式
		String type = properties.getProperty("connectType");
		// 发送报文与返回报文的编码格式
		String encoding = properties.getProperty("encoding");
		
		if ("Http".equalsIgnoreCase(type)) {
			Object url = properties.getProperty("url");
			// 判断发送方式
			String requestMethod = properties.getProperty("requestMethod");
			// 提交数据方式
			String contentType = properties.getProperty("contentType");
			// 超时时间
			String strOutTime = properties.getProperty("outTime");
			// 毫秒
			int outTime;
			if (!"".equals(strOutTime) && strOutTime != null) {
				outTime = Integer.parseInt(properties.getProperty("outTime"));
			} else {
				outTime = 3000;
			}
			// 调用发送Http方法
			returnMessage = this.sendHttpProtocol(url, (String)sendMessage, requestMethod, contentType, outTime, encoding);
		} else if ("Tcp".equalsIgnoreCase(type)) {
			// ip地址
			String ip = properties.getProperty("ip");
			// 端口
			int port = Integer.parseInt(properties.getProperty("port"));
			
			// 判断Socket通讯发送数据的方式
			boolean containsKey = properties.containsKey("constraintByte");
			if (containsKey) {
				// 约束字节长度
				int constraintByte = Integer.parseInt(properties.getProperty("constraintByte"));
				returnMessage = this.sendSocketConstraint(ip, port, (byte[])sendMessage, constraintByte, encoding);
			} else {
				returnMessage = this.sendSocketAll(ip, port, (byte[])sendMessage, encoding);
			}
		} else if ("Mq".equalsIgnoreCase(type)) {
			// ip地址
			String ip = properties.getProperty("ip");
			// 端口
			int port = Integer.parseInt(properties.getProperty("port"));
			// 队列管理器
			String _mqManager = properties.getProperty("mqManager");
			//发送队列
			String _sendQueue = properties.getProperty("sendQueue");
			//接受队列
			String _receiveQueue = properties.getProperty("receiveQueue");
			//编码字符集
			int ccsid = Integer.parseInt(properties.getProperty("cssid"));
			//渠道
			String channel = properties.getProperty("channel");
			
			// 发送IBMMQ
			returnMessage = this.sendIBMMq(ip, port, (String)sendMessage, _mqManager, _sendQueue, _receiveQueue, channel, ccsid, encoding);
		} else if ("WebService".equalsIgnoreCase(type)) {
			// ip地址
			String url = properties.getProperty("url");
			//
			String callMethod = properties.getProperty("callMethod");
			
			returnMessage = (this.sendWebSeviceByAxis(url, callMethod, new Object[]{sendMessage})).toString();
		} else {
			throw new Exception("错误：[" + type +"]未知的type类型!!");
		}
		return returnMessage;
	}
}