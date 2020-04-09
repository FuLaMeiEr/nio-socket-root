package com.example.demo.server;

import org.springframework.boot.web.servlet.server.Session;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 16:59 2020/4/8
 **/
public class NioSocketServer {

    private int bufferSize = 1024;
    private String localCharset = "UTF-8";

    private volatile byte flag = 1;

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    private Set<SocketChannel> keylist = Collections
            .synchronizedSet(new HashSet<SocketChannel>());


    public void start() {
        //创建serverSocketChannel，监听8888端口
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.socket().bind(new InetSocketAddress(8888));
            //设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //为serverChannel注册selector
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务端开始工作：");

            while (flag == 1) {
                selector.select();
                //获取selectionKeys并处理
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    try {
                        //连接请求
                        if (key.isAcceptable()) {
                            handleAccept(key);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleAccept(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();

        socketChannel.configureBlocking(false);

        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));

        keylist.add(socketChannel);

        System.out.println("已连接");

    }
}
