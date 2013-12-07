package sagma.games.rtsServer;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;

import sagma.core.io.FileIO;
import sagma.core.network.Packet;
import sagma.core.network.connection.PacketReciever;
import sagma.core.network.connection.ServerConnection;
import sagma.core.security.Encrypt;

public class ServerThread extends Thread {
	private Socket socket;
	private ServerConnection connection;
	
	public ServerThread(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		connection = new ServerConnection(new PacketListener(), socket);
	}
	
	class PacketListener implements PacketReciever {
		@Override
		public void getPacket(Packet p) {
			command(p);
		}
	}
	
	public void sendPacket(Packet p) {
		connection.sendGuaranteedPacket(p);
	}
	
	public void command(Packet p) {
		String[] lines = p.getLines().toArray(new String[p.getLines().size()]);
		
		String com = lines[0];
		
		if (com.equals(CommandCodes.HELP_BUG_REPORT)) {
			bugReport(lines);
		}
		else if (com.equals(CommandCodes.HELP_PETITION)) {
			petition(lines);
		}
		else if (com.equals(CommandCodes.HELP_URGENT)) {
			urgentPetition(lines);
		}
		else if (com.equals(CommandCodes.AUTHENTICATION)) {
			authentication(lines);
		}
		else if (com.equals(CommandCodes.PING)) {
			ping();
		}
		else if (com.equals(CommandCodes.REQUEST_VERSION)) {
			version();
		}
		else if (com.equals(CommandCodes.VERSION)) {
			version(lines[1]);
		}
		else if (com.equals(CommandCodes.NEW_USER)) {
			newUser(lines);
		}
	}
	
	public void bugReport(String[] lines) {
		
	}
	
	public void petition(String[] lines) {
		
	}
	
	public void urgentPetition(String[] lines) {
		
	}
	
	public void authentication(String[] lines) {
		String username = lines[1];
		String password = lines[2];
		
		String encrypt = Encrypt.encrypt(username, password);
		
		File database = new File("src/sagma/games/rtsServer/database/" + username);
		if (!database.exists()) {
			badAuthentication();
			return;
		}
		
		ArrayList<String> contents = FileIO.read(database);
		
		String userPassword = contents.get(1);
		if (!userPassword.equals(encrypt)) {
			int attempts = new Integer(contents.get(2)).intValue();
			if (attempts >= 10) {
				connection.sendGuaranteedPacket(new Packet().write(CommandCodes.ERROR_DENIAL_OF_SERVICE));
				connection.sendGuaranteedPacket(new Packet().write(CommandCodes.ADMIN_EXIT));
			}
			contents.set(2, ""+(attempts+1));
			FileIO.writeOver(database, contents);
			badAuthentication();
			return;
		}
		
		contents.set(2, ""+0);
		FileIO.writeOver(database, contents);
		goodAuthentication();
	}
	
	public void badAuthentication() {
		connection.sendGuaranteedPacket(new Packet().write(CommandCodes.ERROR_BAD_AUTHENTICATION));
		
	}
	
	public void goodAuthentication() {
		connection.sendGuaranteedPacket(new Packet().write(CommandCodes.AUTHENTICATION));
	}
	
	public void ping() {
		connection.sendGuaranteedPacket(new Packet().write(CommandCodes.PING));
	}
	
	public void version() {
		connection.sendGuaranteedPacket(new Packet().write(CommandCodes.VERSION).write("0.0.0.1"));
	}
	
	public void version(String v) {
		// TODO send a list of files that need to be updated.
	}
	
	public void newUser(String[] lines) {
		String username = lines[0];
		String password = lines[1];
		
		// Enforce username does not exist
		File file = new File("src/sagma/games/rtsServer/database/username");
		if (file.exists()) {
			connection.sendGuaranteedPacket(new Packet().write(CommandCodes.ERROR_BAD_AUTHENTICATION));
		}
		
		// Write username and password into database, with default attempts of 0.
		ArrayList<String> data = new ArrayList<String>(3);
		data.add(username);
		data.add(Encrypt.encrypt(username, password));
		data.add("0");
		
		FileIO.writeOver(file, data);
	}

}
