import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class Receiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private Sender send;
    private int port;
    private String host;
    private int id = new Random(System.currentTimeMillis()).nextInt();
    private ArrayList<String> peers = new ArrayList<String>();

    public Receiver(int p, String h){
        id = abs(id);
        port = p;
        host = h;
        send = new Sender(port, host);
        try {
            socket = new MulticastSocket(port);
        } catch (IOException e) {
            System.out.println("System failed to connect to the port");
            e.printStackTrace();
        }
    }

    public void run() {

        InetAddress group = null;
        try {
            group = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            System.out.println("System failed to resolve the group's ip address");
            e.printStackTrace();
        }
        try {
            socket.joinGroup(group);
            send.multicast("Discovery Request From Peer " + id);
        } catch (IOException e) {
            System.out.println("System Failed to join the group");
            e.printStackTrace();
        }
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("Error while receiving packets");
                e.printStackTrace();
            }
            String received = new String(packet.getData(), 0, packet.getLength());

            if ("end".equals(received)) {
                break;
            }
            if (received.contains("Discovery") && Integer.parseInt(received.substring(received.lastIndexOf('r') + 2)) != id) {
                try {
                    send.multicast("Hello Peer " + received.substring(received.lastIndexOf('r') + 2) + " I'm Peer " + id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (received.contains("I'm Peer")){
                peers.add(received.substring(received.lastIndexOf('r') + 2));
            }

            System.out.println(received);
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            System.out.println("System Failed to leave the group");
            e.printStackTrace();
        }
        socket.close();
    }

}
