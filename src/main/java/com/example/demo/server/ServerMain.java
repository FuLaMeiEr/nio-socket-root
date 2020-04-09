package com.example.demo.server;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 17:04 2020/4/8
 **/
public class ServerMain {

    public static void main(String[] args) {
        NioSocketServer server = new NioSocketServer();
        server.start();
    }
}