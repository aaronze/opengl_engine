import java.net.*;

public class Server {
	public static void main(String[] args) {
		new Server();
	}
	
	public Server() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(1423);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		while (true) {
			try {
				Socket s = ss.accept();
				new Client(s).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}