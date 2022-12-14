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

        UDPClient client = new UDPClient();

        // Task 1
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of Full Names :"); // user define
        int n = sc.nextInt();
        sc.nextLine();
        String[] names = new String[n];
        System.out.println("Enter Full Names :");  //

        for (int i = 0; i < names.length; ++i) {
            names[i] = sc.nextLine();

            String cs = client.sendEcho("SetName:" + names[i]);
            System.out.println(cs);
        }

        System.out.println("LastName:");
        String lastname = sc.nextLine();
        System.out.println("Client.lastname: "+lastname);
        String response = client.sendEcho("GetName: " + lastname);
        System.out.println("Response from Server: " + response);


        // Task 2
        // find missing number from the given sequence
        System.out.println("Client sends -Missing: ");
        sc.reset();
        String missingNum = sc.next();
        String numSeqMissing = client.sendEcho("Missing: " + missingNum);//-> 3
        System.out.println("server sends: " + numSeqMissing);


        // ############## Test ###################################
        //UDPClient client = new UDPClient();
        // 1. Task
        System.out.println("Test Scenarios ");
        client.sendEcho("SetName: John Doe");
        client.sendEcho("SetName: Max Meier");
        client.sendEcho("SetName: Ina Schultz");

        String responseTest = client.sendEcho("GetName: Doe");
        System.out.println("Response from Server: " + responseTest); //-> John

        // 2. Task (Missing  Seq Number)
        String missingTest = client.sendEcho("Missing: 4,9,2,3,8,1,5,7");//-> 3
        System.out.println(missingTest);
    }
}
