# PingServer
Server to process ping requests over UDP and PingClient to send 10 ping requests to the server.
---
layout: page-list
files:
- PingServer.java
- PingServer.class
- PingClient.java
- PingClient.class
---
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

