import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * UDP Server
 */
public class UDPServer {
    private DatagramSocket socket;
    private boolean running;
    //private byte[] buf = new byte[512];

    private List<String> names = new ArrayList<>();

    public UDPServer() {
        try {
            socket = new DatagramSocket(5000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    public void createAndListenSocket() {
        running = true;
        while (running) {
            try {
                byte[] buf = new byte[512];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength()).trim();
                System.out.println("Server come in: " + received);

                if (received.startsWith("SetName:")) {
                    received = received.replaceFirst("SetName:", "").trim();//replace SetName:
                    names.add(received);
                } else if (received.startsWith("GetName:")) {
                    String lastname = received.split(" ")[1];// extract Lastname
                    String firstname = findFirstname(lastname);
                    received = firstname;

                } else if (received.startsWith("Missing:")) {
                    received = received.replaceFirst("Missing:", "").trim();
                    received = findMissingNumber(received);

                }

                System.out.println("Server List: " + names);

                byte[] b = received.getBytes();
                packet = new DatagramPacket(b, b.length, address, port);
                socket.send(packet);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        socket.close();
    }

    /**
     *
     * @param lastname
     * @return
     */
    public String findFirstname(String lastname) {

        for (String name : names) {
            if (name.endsWith(lastname)) {
                System.out.println("find:" + name);
                return name.split(" ")[0];
            }
        }
        return "";
    }

    /**
     *
     * @param numbers
     * @return
     */
    public String findMissingNumber(String numbers) {
        int[] ALL_NUMBERS = IntStream.range(1, 10).toArray();//1-9
        List<String> strList = Arrays.asList(numbers.split(","));//split Numbers 1,4,5,9,2,6,7,8
        for (int n : ALL_NUMBERS) {
            if (!strList.contains(n + "")) { // to string conversion
                return n + ""; // if not in List return
            }
        }
        return "";
    }


    public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.createAndListenSocket();
        //System.out.println(server.findMissingNumber("1,4,5,9,2,6,7,8"));
        //System.out.println(server.findFirstname(""));
    }
}
