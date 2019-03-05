public class Peer {
    private static int port = 6666;
    private static String host = "230.0.0.0";

	public static void main (String [] args){
		Receiver receiver = new Receiver(port, host);
        Sender sender = new Sender(port, host);

		receiver.start();
		

	}
}