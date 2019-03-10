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
    public Queue<String> strs = new LinkedList<>();
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    private Sender send;
    private InetAddress group;
    private int port;
    private int check;
    private int c;
    private RandomString rand = new RandomString();
    private int id = abs(new Random(System.currentTimeMillis()).nextInt());
    private ArrayList<String> peers = new ArrayList<>();

    public Receiver(int port, InetAddress group) {
        this.group = group;
        this.port = port;
        check = 0;
        c = 0;
    }

    public void run() {
        send = new Sender(port, group);
        try {
            socket = new MulticastSocket(port);
        } catch (IOException e) {
            System.out.println("System failed to connect to the port");
            e.printStackTrace();
        }
        try {
            socket.joinGroup(group);
            if (check == 0) {
                send.multicast(id + " Discovery");
                check++;
            }
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
            String resp = new String(packet.getData(), 0, packet.getLength());

            String new_id = resp.substring(0, resp.indexOf(' '));
            if ((Integer.parseInt(new_id)) != this.id) {
                strs.add(resp);
                if (strs.size() == 20) {
                    try {
                        send.multicast(this.id + " Wait");
                        for (int i = 0; i < 20; i++)
                            strs.remove();
                        send.multicast(this.id + " Continue");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (resp.contains("Discovery") && !peers.contains(new_id)) {
                    try {
                        send.multicast(id + " Response");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (resp.contains("Response") && !peers.contains(new_id)) {
                    peers.add(new_id);
                }


                if (!resp.contains("Wait") || resp.contains("Continue") || !resp.isBlank()) {
                    try {
                        if (c < 100) {
                            send.multicast(id + " " + rand.fake());
                            c++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(resp);
            }
        }
/*        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            System.out.println("System Failed to leave the group");
            e.printStackTrace();
        }
        socket.close();*/

    }

    public void new_thread() throws IOException {
        new Thread(this).start();
    }

}