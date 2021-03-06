### NIO
- 什么是NIO
    + 是为所有的原始类型（boolean类型除外）提供缓存支持的数据容器，使用它可以提供非阻塞式的高伸缩性网络。
- NIO于BIO的区别
    + BIO是阻塞IO
    + NIO是非阻塞IO
    + 共同点：两者都是同步操作，必须先进行IO操作才能进行下一步操作
    + 不同点：BIO多线程对某资源进行IO操作时会出现阻塞，即一个线程进行IO操作完才会通知另外的IO操作线程，必须等待。<br>
    NIO多线程对某资源进行IO操作时会把资源先操作至内存缓冲区。然后询问是否IO操作就绪，是则进行IO操作，否则进行下一步操作，然后不断的轮询是否IO操作就绪，直到iIO操作就绪后进行相关操作。
- NIO是怎么工作的
    + 调用Selector的静态工厂创建一个选择器，创建一个服务端的Channel，绑定到一个Socket对象，并把这个通信信道注册到选择器上，把这个通信信道设置为非阻塞模式。然后就可以调用Selector的selectedKeys方法来检查已经注册在这个选择器上的所有通信信道是否有需要的事件发生，如果有某个事件发生，将会返回所有的selectedKeys，通过这个对象的Channel方法就可以取得这个通信信道对象，从而读取通信的数据，而这里读取的数据是Buffer，这个Buffer是我们可以控制的缓冲器。



