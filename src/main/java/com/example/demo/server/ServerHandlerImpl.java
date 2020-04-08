package com.example.demo.server;

import com.example.demo.server.ServerHandlerBs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 16:43 2020/4/8
 **/
public class ServerHandlerImpl implements ServerHandlerBs {

    private int bufferSize = 1024;
    private String localCharset = "UTF-8";

    public ServerHandlerImpl() {
    }

    public ServerHandlerImpl(int bufferSize) {
        this(bufferSize, null);
    }

    public ServerHandlerImpl(String localCharset) {
        this(-1, localCharset);
    }

    public ServerHandlerImpl(int bufferSize, String localCharset) {
        this.bufferSize = bufferSize > 0 ? bufferSize : this.bufferSize;
        this.localCharset = localCharset == null ? this.localCharset : localCharset;
    }


    @Override
    public void handleAccept(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();

        socketChannel.configureBlocking(false);

        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
    }

    @Override
    public String handleRead(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

        String receivedStr = "";


        if (socketChannel.read(buffer) == -1) {

            socketChannel.shutdownOutput();
            socketChannel.shutdownInput();
            socketChannel.close();

            System.out.println("连接断开");
        } else {
            buffer.flip();
            receivedStr = Charset.forName(localCharset).newDecoder().decode(buffer).toString();
            buffer.clear();

            //返回数据给客户端
            buffer = buffer.put(("received string : " + receivedStr).getBytes(localCharset));
            //读取模式
            buffer.flip();
            socketChannel.write(buffer);
            //注册selector 继续读取数据
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));

        }

        return receivedStr;
    }
}
