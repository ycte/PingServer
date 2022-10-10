// SendTest.java
import java.io.*;
import java.net.*;
//import java.util.*;

public class SendTest {
    // Send the packet
    public static void sender() throws IOException {
        //1，创建upd数据报socket，报文接收端的Ip和端口，在数据包中定义，socket不关心
        //这点区别于TCP传输方式
        DatagramSocket socket = new DatagramSocket();
        //2，创建数据包，数据内容，Ip 端口
        //这里使用 127.0.0.1
//        InetAddress inetAddress = InetAddress.getLocalHost();

        String str = "192.168.3.11";
        String[] ipStr = str.split("\\.");
        byte[] ipBuf = new byte[4];
        for(int i = 0; i < 4; i++){
            ipBuf[i] = (byte)(Integer.parseInt(ipStr[i])&0xff);
        }
//        -----------------------------------
//©著作权归作者所有：来自51CTO博客作者magic_180的原创作品，请联系作者获取转载授权，否则将追究法律责任
//        Java InetAddress.getByAddress()的使用
//        https://blog.51cto.com/u_4042309/3598008
        InetAddress inetAddress = InetAddress.getByAddress(ipBuf);
        String str1 = "你好，我是最帅的op——yunxi!";
        //转换为字节发送
        byte[] bytes = str1.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes,0,bytes.length,inetAddress,8081);
        //3，启动upd发送
        socket.send(packet);
        //4，关闭socket资源
        socket.close();
    }

    public static void main(String[] args) throws IOException{
        sender();
    }
}
