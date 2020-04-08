package com.example.demo.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 16:40 2020/4/8
 **/
public interface ServerHandlerBs {
    void handleAccept(SelectionKey selectionKey) throws IOException;


    String handleRead(SelectionKey selectionKey) throws IOException;

}
