import java.io.IOException;

public class Test {
    private static int port = 6666;
    private static String host = "230.0.0.0";

    public static void main (String [] args) throws IOException {
        Sender sender = new Sender(port, host);
        sender.multicast("Hello");
        sender.multicast("World");
        sender.multicast("!");
        sender.multicast("end");
    }
}
