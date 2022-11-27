import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * UDP Client
 */
public class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public UDPClient() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost"); //127.0.0.1
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sends data to server
     *
     * @param msg
     * @return
     */
    public String sendEcho(String msg) {
        String received = "";
        try {

            buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 5000);
            socket.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            received = new String(packet.getData(), 0, packet.getLength());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return received;
    }

    public void close() {
        socket.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {


        // ############## Test ###################################
        UDPClient client = new UDPClient();
        // 1. Task
        System.out.println("Test");
        client.sendEcho("SetName: John Doe");
        client.sendEcho("SetName: Max Meier");
        client.sendEcho("SetName: Ina Schultz");

        String response = client.sendEcho("GetName: Doe");
        System.out.println("Response from Server: " + response); //-> John

        // 2. Task (Missing  Seq Number)
        String missing = client.sendEcho("Missing: 1,4,5,9,2,6,7,8");//-> 3
        System.out.println(missing);
    }
}
