package com.example.demo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 23:40 2020/4/9
 **/
public class NioServer {

    private int port;
    private Selector selector;

    public static void main(String[] args) {
        new NioServer(8080).start();
    }

    public NioServer(int port) {
        this.port = port;
    }

    public void init() {
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(port));
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NioServer started ......");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void accept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            System.out.println("accept a client : " + sc.socket().getInetAddress().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.init();
        while (true) {
            try {
                int events = selector.select();
                if (events > 0) {
                    Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                    while (selectionKeys.hasNext()) {
                        SelectionKey key = selectionKeys.next();
                        if (key.isValid()) {
                            selectionKeys.remove();
                            if (key.isAcceptable()) {
                                accept(key);
                            } else {
                                nioServerHandler(key);
                            }
                        } else {
                            continue;
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void nioServerHandler(SelectionKey selectionKey) {
        int count = 0;
        try {
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
                    count = socketChannel.read(buffer);
                } catch (IOException e) {
                    selectionKey.cancel();
                    socketChannel.socket().close();
                    socketChannel.close();
                    return;
                }
                buffer.flip();
                System.out.println("收到客户端" + socketChannel.socket().getInetAddress().getHostName() + "的数据：" + new String(buffer.array()));
                //将数据添加到key中
                ByteBuffer outBuffer = ByteBuffer.wrap(buffer.array());
                socketChannel.write(outBuffer);// 将消息回送给客户端
                selectionKey.cancel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

