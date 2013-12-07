package sagma.core.network.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import sagma.core.network.Packet;

public class ServerConnection implements Runnable {
	String address;
	int port = 1424;
	
	private Socket socket;
	private ArrayList<PacketReciever> listeners;
	private boolean validated = false;
	private boolean keepGoing = true;
	
	private PrintWriter os;
	private BufferedReader is;
	
	public final static String PACKET_START = "1";
	public final static String PACKET_END = "0";
	public final static int RECOVERIES = 10;
	public final static int TIME_OUT = 100;
	
	private int recoveriesLeft = RECOVERIES;
	
	public ServerConnection(String address) {
		this.address = address;
		init();
	}
	
	public ServerConnection(PacketReciever listener, String address) {
		this.address = address;
		init();
		listeners.add(listener);
	}
	
	public ServerConnection(String address, int port) {
		this.address = address;
		this.port = port;
		init();
	}
	
	public ServerConnection(Socket s) {
		socket = s;
		init();
	}
	
	public ServerConnection(PacketReciever listener, Socket s) {
		socket = s;
		init();
		listeners.add(listener);
	}
	
	public ServerConnection(PacketReciever listener, String address, int port) {
		this.address = address;
		this.port = port;
		init();
		listeners.add(listener);
	}
	
	private void init() {
		listeners = new ArrayList<PacketReciever>();
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			if (socket == null) {
				try {
					socket = new Socket(address, port);
				} catch (Exception e) {
					try {
						socket = new Socket("192.168.1.10", port);
					} catch (Exception v) {
						socket = new Socket("localhost", port);
					}
				}
			} else {
				address = socket.getInetAddress().toString();
				port = socket.getPort();
			}
			socket.setSoTimeout(TIME_OUT);
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			System.out.println("Something is wrong with the server at " + address + " : " + port);
			e.printStackTrace();
		}
		
		while (keepGoing) {
			try {
				if (is.ready()) {
					readPacket();
					recoveriesLeft = RECOVERIES;
				}
			} catch (IOException e) {
				recoverConnection();
			}
		}
	}
	
	private void recoverConnection() {
		if (recoveriesLeft <= 0) {
			System.out.println("Yea, pretty much gonna assume linkdead.");
			System.out.println("Giving up permanently...");
			
			keepGoing = false;
		}
		if (recoveriesLeft < RECOVERIES) {
			try {
				Thread.sleep(TIME_OUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Connection dropped!");
			System.out.println("Attempting to reconnect");
		}
		
		recoveriesLeft--;
		
		try {
			socket = new Socket(address, port);
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			System.out.println(".");
		}
	}
	
	public void addPacketListener(PacketReciever listener) {
		listeners.add(listener);
	}
	
	public void removePacketListener(PacketReciever listener) {
		listeners.remove(listener);
	}
	
	public void clearPacketListeners() {
		listeners = new ArrayList<PacketReciever>();
	}
	
	public void sendPacket(Packet p) {
		validated = false;
		try {
			writeLine(PACKET_START);
			for (String s : p.getLines()) {
				writeLine(s);
			}
			writeLine(PACKET_END);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private final void writeLine(String s) {
		os.println(s + "\n");
		os.flush();
	}
	
	public boolean wasValidated() {
		return validated;
	}
	
	public void sendGuaranteedPacket(Packet p) {
		validated = false;
		
		recoveriesLeft = RECOVERIES;
		
		while (!validated) {
			sendPacket(p);
			
			try {
				Thread.sleep(TIME_OUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				readPacket();
			} catch (IOException e) {
				recoverConnection();
			}
		}
	}
	
	public void getPacket(Packet p) {
		if (p.equals(Packet.OK)) {
			validated = true;
			return;
		}
		
		sendPacket(Packet.OK);
		
		for (PacketReciever r : listeners) {
			r.getPacket(p);
		}
	}
	
	private void readPacket() throws IOException {
		String s = readLine();
		
		if (!s.equals(PACKET_START)) {
			return;
		}
		
		Packet packet = new Packet();
		
		while (!(s = readLine()).equals(PACKET_END)) {
			packet.write(s);
		}
		
		getPacket(packet);
	}
	
	private String readLine() throws IOException {
		return is.readLine();
	}
	
	public void close() {
		try {
			is.close();
			os.flush();
			os.close();
			socket.close();
		} catch (Exception e) {
			
		}
	}
}
