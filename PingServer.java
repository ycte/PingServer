
/*
 *
 * Server to process ping requests over UDP
 *
 * Usage: java PingServer port passwd [-delay delay] [-loss loss]
 *
 * Thanks: base on PingServer by instructor Qiao Xiang
 */

// PingServer.java
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class PingServer
{
    private static double LOSS_RATE = 0.3;
    private static int AVERAGE_DELAY = 100; // milliseconds

    public static void main(String[] args) throws Exception
    {
        // Get command line argument.
        if (args.length <= 1) {
            System.out.println("Usage: java PingServer port passwd [-delay delay] [-loss loss]");
            return;
        }
        if (args.length >= 3) {
            if(args[2].matches("-delay(.*)")){
                AVERAGE_DELAY = Integer.parseInt(args[3]);
            }
            if(args[2].matches("-loss(.*)")){
                LOSS_RATE = Double.parseDouble(args[3]);
            }

        }
        if (args.length >= 5) {
            if(args[4].matches("-delay(.*)")){
                AVERAGE_DELAY = Integer.parseInt(args[5]);
            }
            if(args[4].matches("-loss(.*)")){
                LOSS_RATE = Double.parseDouble(args[5]);
            }

        }

        int port = Integer.parseInt(args[0]);
        String passwd = args[1];
        System.out.println("AVERAGE_DELAY: " + AVERAGE_DELAY + " LOSS_RATE: " + LOSS_RATE +
                " port: " + port + " passwd: " + passwd);
        // Create random number generator for use in simulating
        // packet loss and network delay.
        Random random = new Random();

        // Create a datagram socket for receiving and sending
        // UDP packets through the port specified on the
        // command line.
        DatagramSocket socket = new DatagramSocket(port);

        // Processing loop.
        while (true) {

            // Create a datagram packet to hold incomming UDP packet.
            DatagramPacket
                    request = new DatagramPacket(new byte[128], 128);

            // Block until receives a UDP packet.
            // Wait
            socket.receive(request);

            // Print the received data, for debugging
            printData(request);

            // Check passwd
            String sentence = new String(request.getData());
            sentence = URLDecoder.decode(sentence, "UTF-8");
            sentence = sentence.replaceFirst("PING", "PINGECHO");
            String[] clientItem = sentence.split(" ");
            if (passwd.compareTo(clientItem[3]) == 0) {
                System.out.println("Passwd correct!");
            }
            else {
                System.out.println("Wrong passwd!");
            }

            // Decide whether to reply, or simulate packet loss.
            if (random.nextDouble() < LOSS_RATE) {
                System.out.println(" Reply not sent.");
                System.out.println("----------------------------------");
                continue;
            }

            // Simulate prorogation delay.
            Thread.sleep((int) (random.nextDouble() * 2 * AVERAGE_DELAY));

            // Send reply.
            // Time in the format of Java currentTimeMill
            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
            Date date1 = new Date(System.currentTimeMillis());
            sentence = clientItem[0] + " " + clientItem[1] + " " + formatter1.format(date1) + " " + clientItem[3];
            sentence = URLEncoder.encode(sentence, "utf-8");
            InetAddress clientHost = request.getAddress();
            int clientPort = request.getPort();
            byte[] buf = sentence.getBytes();
            DatagramPacket
                    reply = new DatagramPacket(buf, sentence.length(),
                    clientHost, clientPort);

            socket.send(reply);

            System.out.println(" Reply sent.");
            System.out.println("----------------------------------");
        } // end of while
    } // end of main

    /*
     * Print ping data to the standard output stream.
     */
    private static void printData(DatagramPacket request)
            throws Exception

    {
        // Obtain references to the packet's array of bytes.
        byte[] buf = request.getData();

        // Wrap the bytes in a byte array input stream,
        // so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais
                = new ByteArrayInputStream(buf);

        // Wrap the byte array output stream in an input
        // stream reader, so you can read the data as a
        // stream of **characters**: reader/writer handles
        // characters
        InputStreamReader isr
                = new InputStreamReader(bais);

        // Wrap the input stream reader in a bufferred reader,
        // so you can read the character data a line at a time.
        // (A line is a sequence of chars terminated by any
        // combination of \r and \n.)
        BufferedReader br
                = new BufferedReader(isr);

        // The message data is contained in a single line,
        // so read this line.
        String line = br.readLine();
        line = URLDecoder.decode(line, "utf-8");

        // Print host address and data received from it.
        System.out.println("Received from " +
                request.getAddress().getHostAddress() +
                ": " +
                new String(line) );
    } // end of printData
} // end of class
