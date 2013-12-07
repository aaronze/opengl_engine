import java.net.*;
import java.io.*;
import java.util.*;

public class Client extends Thread {
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	
	public final static int UNKNOWN_OS = 0;
	public final static int WINDOWS_32 = 1;
	public final static int WINDOWS_64 = 2;
	public final static int LINUX_32 = 3;
	public final static int LINUX_64 = 4;
	public final static int MAC = 5;
	
	String root;

	public Client(Socket socket) {
		sock = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void run() {
		System.out.println("Client connected");
		
		String osName = readLine();

		int os = new Integer(osName.substring(3, osName.length())).intValue();
		
		// Build manifest for given os
		Manifest manifest = null;
		if (os == WINDOWS_32) {
			manifest = new Manifest("C:/EngineBuilds/builds/win32/");
			root = "C:/EngineBuilds/builds/win32";
		}
		if (os == WINDOWS_64) {
			manifest = new Manifest("C:/EngineBuilds/builds/win64/");
			root = "C:/EngineBuilds/builds/win64";
		}
		if (os == LINUX_32) {
			manifest = new Manifest("C:/EngineBuilds/builds/linux32/");
			root = "C:/EngineBuilds/builds/linux32";
		}
		if (os == LINUX_64) {
			manifest = new Manifest("C:/EngineBuilds/builds/linux64/");
			root = "C:/EngineBuilds/builds/linux64";
		}
		if (os == MAC) {
			manifest = new Manifest("C:/EngineBuilds/builds/mac32/");
			root = "C:/EngineBuilds/builds/mac32";
		}
		sendLine(manifest.toString());

		System.out.println("waiting...");
		try {
			while (true) {
				Thread.sleep(40);
				if (sock.getInputStream().available() > 1) command();
			}
		} catch (Exception e) {
			System.out.println("Client terminated");
			return;
		}
	}
	
	private void command() {
		String line = readLine();
		if (line.equals("")) return;
		
		StringTokenizer tokey = new StringTokenizer(line, " ");
		String command = tokey.nextToken();
		
		if (command.equalsIgnoreCase("GET")) {
			String filename = tokey.nextToken();
			while (tokey.hasMoreTokens()) 
				filename += " " + tokey.nextToken(); 
			sendFile(new File(root + filename));
			System.out.println("SENT " + filename);
		}
		
		if (command.equalsIgnoreCase("ECHO")) {
			System.out.println(line);
		}
	}
	
	public String readLine() {
		try {
			return in.readLine();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return "";
	}
	
	public void sendLine(String s) {
		try {
			out.println(s + "\n");
			out.flush();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void sendFile(File file) {
		try {
			byte[] buf = new byte[512];
			FileInputStream fis = new FileInputStream(file);
			OutputStream out = sock.getOutputStream();
			int len;
			
			while ((len = fis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}

			out.flush();
			fis.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}