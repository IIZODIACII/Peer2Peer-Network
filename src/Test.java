import java.io.IOException;
import java.net.InetAddress;

public class Test {
    private static int port = 6666;
    private static String host = "230.0.0.0";
    private static InetAddress group;

    public static void main(String[] args) throws IOException {
        group = InetAddress.getByName(host);

        Sender sender = new Sender(port, group);
        sender.multicast("Hello");
        sender.multicast("World");
        sender.multicast("!");
        sender.multicast("end");
    }
}
