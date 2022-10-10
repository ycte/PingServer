
/*
 *
 * PingClient sends 10 ping requests to the server
 *
 * Usage: java PingClient host port passwd
 *
 * Thanks: Instructor Qiao Xiang for the basic code of send and receive
 *         CSDN for Time in the format of Java currentTimeMill, Timer
 *
 */

// PingClient.java
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class PingClient {
    public static void main(String[] args) throws Exception {
        // Get command line argument.
        if (args.length <= 2) {
            System.out.println("Usage: java PingClient host port passwd");
            return;
        }
        String host = "user";
        host = args[0];
        InetAddress serverIPAddress = InetAddress.getByName(host);
        int serverPort = 9876;
        serverPort = Integer.parseInt(args[1]);
        String passwd = "passwd";
        passwd = args[2];
        System.out.println("legal input:" + host + " " + serverPort + " " + passwd);

        // Time in the format of Java currentTimeMill
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("Start time:" + formatter.format(date));

        // Create socket
        DatagramSocket clientSocket = new DatagramSocket();

        // Initialize Timer and TimerTask
        // Finalize for backup
        final int[] timeOutNum = {0};
        long[] delayArray = new long[10];
        final boolean[] waiting = {true};
        Timer timer = new Timer();
        final int[] count = {0,0};
        int finalServerPort = serverPort;
        String finalPasswd = passwd;
        final boolean[] sendFlag = {false, false, false, false, false, false, false, false, false, false,false};
        // Set TimerTask
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                count[0]++;
                // Time in the format of Java currentTimeMill
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                System.out.println(formatter.format(this.scheduledExecutionTime()) + "  Timer " + count[0] + " as below:");
                sendFlag[count[0]] = true;
                if (count[0] == 10) {
                    System.out.println("!!!! Timer ends at " + count[0] + " !!!!");
                    waiting[0] = false;
                    this.cancel();
                }
            }
        };
        /*
         * sendFlag[i]: become true at sec i, and return to false before send
         *
         * waiting[0]: be always true before sec 10
         *
         */
        timer.schedule(task, 0, 1000);

        // Loop for Ping
        int countFlag = 0;
        while (waiting[0]) {
            System.out.print("");
            if(sendFlag[count[0]]) {
                sendFlag[count[0]] = false;
                // Time in the format of Java currentTimeMill
                SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
                Date date1 = new Date(System.currentTimeMillis());
                String sentence = "PING " + (count[0] - 1) + " " + formatter1.format(date1) + " " + finalPasswd + " ";
                // Format sendData
                String urlStr = URLEncoder.encode(sentence, "utf-8");
                byte[] sendData = urlStr.getBytes();
                System.out.println("sendData: " + sentence);
                System.out.println("sendData length = " + sendData.length);
                System.out.println("destination: " + serverIPAddress + ":" + finalServerPort);
                /*
                 * timeSrcFlag: before send
                 *
                 * timeDstFlag: after receive
                 */
                long timeSrcFlag = 0;
                try {
                    // Construct and send datagram

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, finalServerPort);
                    timeSrcFlag = System.currentTimeMillis();
                    clientSocket.send(sendPacket);
                } catch (IOException e) {
                    // throw new RuntimeException(e);
                }
                // Receive datagram and set timeout
                byte[] receiveData = new byte[128];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    clientSocket.setSoTimeout(1000);
                } catch (SocketException e) {
                    // throw new RuntimeException(e);
                }
                try {
                    clientSocket.receive(receivePacket);
                } catch (SocketTimeoutException e) {
                    // throw new RuntimeException(e);
                    System.out.println("No reply is sent From Server!");
                    // System.out.println("----------------------------------");
                    // System.out.println();
                    timeOutNum[0]++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                long timeDstFlag = System.currentTimeMillis();
                // Print delay
                long receiveDelay = timeDstFlag - timeSrcFlag;
                delayArray[countFlag] = receiveDelay;
                System.out.println("delay: " + receiveDelay );
                // Print data received
                String sentenceFromServer = new String(receivePacket.getData());
                String keyWord = URLDecoder.decode(sentenceFromServer, "UTF-8");
                System.out.println("From Server: " + keyWord);
                System.out.println("----------------------------------");
                System.out.println();
                countFlag++;
            }
        }

        // Close client socket
        clientSocket.close();

        // Count RTT and loss information
        long maxRTT = 0, minRTT = 2048, sumRTT = 0;
        double aveRTT;
        for (int i = 0; i < 10; i++) {
            // System.out.println(i + ":" + delayArray[i]);
            if (delayArray[i] <= 1000) {
                sumRTT = sumRTT + delayArray[i];
            }
            if (maxRTT < delayArray[i] && delayArray[i] <= 1000) {
                maxRTT = delayArray[i];
            }
            if (minRTT > delayArray[i] ) {
                minRTT = delayArray[i];
            }
        }
        // Print RTT and loss information
        aveRTT = sumRTT / 10.0;
        System.out.println("RTTs Information:");
        System.out.println("max RTT (except loss): " + maxRTT);
        System.out.println("min RTT: " + minRTT);
        System.out.println("average RTT: " + aveRTT);
        System.out.println("loss rate: " + timeOutNum[0] * 1.0 / 10);
        timer.cancel();
    } // end of main
}
