import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.abs;

public class Receiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private Sender send;
    private InetAddress group;
    private int id = new Random(System.currentTimeMillis()).nextInt();
    private ArrayList<String> peers = new ArrayList<>();
    public Queue<String> strs = new LinkedList<>();
    public Receiver(int port, InetAddress group){
        id = abs(id);

        this.group = group;
        send = new Sender(port, group);
        try {
            socket = new MulticastSocket(port);
        } catch (IOException e) {
            System.out.println("System failed to connect to the port");
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket.joinGroup(group);
            send.multicast("Discovery Request From Peer " + id);
        } catch (IOException e) {
            System.out.println("System Failed to join the group");
            e.printStackTrace();
        }
        while (strs.size()<50) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("Error while receiving packets");
                e.printStackTrace();
            }
            String resp = new String(packet.getData(), 0, packet.getLength());
            strs.add(resp);
            if ("end".equals(resp)) {
                break;
            }
            String new_id = resp.substring(resp.lastIndexOf('r') + 2);
            if (resp.contains("Discovery") && Integer.parseInt(new_id) != id) {
                try {
                    send.multicast("Hello Peer " + new_id + " I'm Peer " + id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resp.contains("I'm Peer")){
                peers.add(new_id);
            }
            System.out.println(strs.remove());
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
