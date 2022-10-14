---
layout: page-list
files:
- PingServer.java
- PingServer.class
- PingClient.java
- PingClient.class
---

# PingServer
The homework of CNNS in XMU.
Server to process ping requests over UDP and PingClient to send 10 ping requests to the server.
##### 用法：

1. PingServer

```shell
javac PingServer

java PingServer port passwd [-delay delay] [-loss loss]
```

2. PingClient

```shell
javac PingClient

java PingClient host port passwd
```

##### 存在的问题

1. 因为不了解 Java 的线程同步，在处理 Timer 的 TimerTask 的同时，利用了 print() 的中断来保证 receive() 的正确运行，虽暂未发现明显漏洞。

2. Client 在处理中文回复时低概率出现部分汉字的编码问题。
