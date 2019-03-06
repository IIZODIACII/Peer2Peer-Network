import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private int port;

    public Sender (int port, InetAddress group){
        this.port = port;
        this.group = group;

    }

    public void multicast(String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        buf = multicastMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, port);
        socket.send(packet);
        socket.close();
    }

}