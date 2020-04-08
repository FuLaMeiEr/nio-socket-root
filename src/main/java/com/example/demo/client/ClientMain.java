package com.example.demo.client;

/**
 * @ClassName
 * @Description
 * @Author WangHaiQiang
 * @Date Created in 17:06 2020/4/8
 **/
public class ClientMain {
    public static void main(String[] args) {
        new NioSocketClient().start();
    }
}
