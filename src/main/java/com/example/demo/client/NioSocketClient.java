package com.example.demo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 17:05 2020/4/8
 **/
public class NioSocketClient {
    public void start() {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            //连接服务端socket
            SocketAddress socketAddress = new InetSocketAddress("localhost", 8888);
            socketChannel.connect(socketAddress);

            int sendCount = 0;

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (sendCount < 10) {
                buffer.clear();
                //向服务端发送消息
                buffer.put(("发送连接" + sendCount).getBytes());
                //读取模式
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();

                //从服务端读取消息
                int readLenth = socketChannel.read(buffer);
                //读取模式
                buffer.flip();
                byte[] bytes = new byte[readLenth];
                buffer.get(bytes);
                System.out.println(new String(bytes, "UTF-8"));
                buffer.clear();


                sendCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
