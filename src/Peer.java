import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer {
    private static int port = 6666;
    private static String host = "230.0.0.0";
    private static InetAddress group;

    public static void main(String[] args) throws UnknownHostException {

        group = InetAddress.getByName(host);

        Receiver receiver = new Receiver(port, group);
        Sender sender = new Sender(port, group);

        receiver.start();

    }
}