import java.io.*;
import java.net.Socket;

public class MQTTBinaryStreamClient {
    public static void main(String[] args) {
        // connection via sockets
        try (Socket socket = new Socket("localhost", 1883)) {

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();


            byte[] CONNECT = {
                    0x10, // message type is 1:0001, set the DUP flag, Qos level and RETAIN flags to 0:0000
                    0x13, // remaining length 13 hexa = 19 in decimal
                    0x00, 0x04, // protocol name length
                    'M', 'Q', 'T', 'T', // protocol name
                    0x04, // protocol version
                    0x02, // connect flags: all are zeros excpet for cleanSession is 1 : true as asked
                    0x00, 0x3c, // alive timer 60 s
                    0x00, 0x07, // client ID length
                    'M', 'o', 'h', 'a', 'm', 'a', 'd' // client ID
            };

            output.write(CONNECT);

            byte[] CONNACK = new byte[4];
            input.read(CONNACK);

            System.out.println("CONNACK message: ");
            /*
            for (int i = 0; i < CONNACK.length; i++) {
                System.out.println(CONNACK[i]);
            }
            */
            for (byte b : CONNACK) {
                System.out.printf("0x%02x ", b);
            }

            byte[] PUBLISH = {
                    0x30, // message type is 3:0011, set the DUP flag, Qos level and RETAIN flags to 0:0000
                    0x0e, // remaining length e hexa = 14 in decimal
                    0x00, 0x06, // topic length
                    'e', 'x', 'e', 'r', '6', '/', // topic name
                    0x00, 0x04, // message length
                    'd', 'a', 't', 'a', // message to be published
            };

            output.write(PUBLISH);

            byte[] PUBACK = new byte[4];
            // message type + flags
            // remaining length
            // varaible header
            input.read(PUBACK);
            System.out.println("PUBACK message: ");
            /*
            for (int i = 0; i < PUBACK.length; i++) {
                System.out.println(PUBACK[i]);
            }
            */
            for (byte b : PUBACK) {
                System.out.printf("0x%02x ", b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
