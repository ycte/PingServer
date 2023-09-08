🚀After you went through the socket programing which tastes bitter and astringent，you will find it interesting. As the saying goes,
> "The Only Easy Day Was Yesterday." - Navy Seal
# PingServer
**Tips** Computer Network and Network Assignment 2

The homework of CNNS in XMU.
Server to process ping requests over UDP and PingClient to send 10 ping requests to the server.


##### usage：

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


##### Client principle

1. 定时器：Timer 与 TimerTask 配合修改变量调控 Ping 操作。

| 变量 | waiting[0]       | sendFlag[i]（第 i 秒时为 true，其他 false） |
| ---- | ---------------- | ------------------------------------------- |
| 功能 | 程序主功能的开关 | 控制第 i 次 Ping 操作在第 i 秒执行          |

2. Ping：send、setTimeOut、receive


##### issue

1. 因为不了解 Java 的线程同步，在处理 Timer 的 TimerTask 的同时，利用了 print() 的中断来保证 receive() 的正确运行，虽暂未发现明显漏洞。

2. Client 在处理中文回复时低概率出现部分汉字的编码问题。
