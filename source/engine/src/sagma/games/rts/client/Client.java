package sagma.games.rts.client;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import sagma.core.data.Color4f;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.network.Packet;
import sagma.core.network.connection.PacketReciever;
import sagma.core.render.Render;
import sagma.games.rts.RTS;
import sagma.games.rts.entity.projectile.Bullet;
import sagma.games.rts.entity.ship.CapitalShip;

public class Client implements PacketReciever {
	public int id = -1;
	String ip;
	boolean isHost = false;
	private String userName = System.getProperty("user.name");
	
	@Override
	public void getPacket(Packet p) {
		ArrayList<String> data = p.getLines();
		
		for (String s : data) {
			command(s);
		}
	}
	
	public Client() {
		
	}
	
	public Client(int id, String ip) {
		this.id = id;
		this.ip = ip;
	}
	
	public void command(String line) {
		int splitIndex = line.indexOf(' ');
		if (splitIndex == -1) {
			return;
		}
		String command = line.substring(0, splitIndex);
		String param = line.substring(splitIndex+1, line.length());
		
		if (command.equals("ID")) {
			id = toInt(param);
			Packet whoAmI = new Packet();
			whoAmI.write("LOGIN " + System.getProperty("user.name"));
			RTS.connect.sendPacket(whoAmI);
			System.out.println("Got an ID from the server: " + id);
		}
		else if (command.equals("GAME_ADD_PLAYER")) {
			
		}
		else if (command.equals("GAME_SET_HOST")) {
			int hostID = toInt(param);
			if (hostID == id) {
				makeHost();
			}
		}
		else if (command.equals("PING")) {
			Packet ping = new Packet();
			ping.write("PING 0");
			RTS.connect.sendPacket(ping);
		}
		else if (command.equals("CHAT")) {
			RTS.chat.addLine(param);
		}
		else if (command.equals("UPDATE_POSITION")) {
			StringTokenizer tokey = new StringTokenizer(param, " (),");
			String cName = tokey.nextToken();
			String cID = tokey.nextToken();
			
			float x = toFloat(tokey.nextToken());
			float y = toFloat(tokey.nextToken());
			float z = toFloat(tokey.nextToken());
			
			updatePosition(cName, cID, new Vec3(x, y, z));
		}
		else if (command.equals("UPDATE_ROTATION")) {
			StringTokenizer tokey = new StringTokenizer(param, " (),");
			String cName = tokey.nextToken();
			String cID = tokey.nextToken();
			
			float x = toFloat(tokey.nextToken());
			float y = toFloat(tokey.nextToken());
			float z = toFloat(tokey.nextToken());
			
			updateRotation(cName, cID, new Vec3(x, y, z));
		}
		else if (command.equals("UPDATE_ROTATION_SPEED")) {
			StringTokenizer tokey = new StringTokenizer(param, " (),");
			String cName = tokey.nextToken();
			String cID = tokey.nextToken();
			
			float x = toFloat(tokey.nextToken());
			float y = toFloat(tokey.nextToken());
			float z = toFloat(tokey.nextToken());
			
			updateRotationSpeed(cName, cID, new Vec3(x, y, z));
		}
		else if (command.equals("UPDATE_SPEED")) {
			StringTokenizer tokey = new StringTokenizer(param, " (),");
			String cName = tokey.nextToken();
			String cID = tokey.nextToken();
			
			float x = toFloat(tokey.nextToken());
			float y = toFloat(tokey.nextToken());
			float z = toFloat(tokey.nextToken());
			
			updateSpeed(cName, cID, new Vec3(x, y, z));
		}
		else if (command.equals("PLAYER_LEFT")) {
			removePlayer(param);
		}
		else if (command.equals("SERVER_MESSAGE")) {
			RTS.chat.setColor(new Color4f(1.0f, 1.0f, 0.0f, 1.0f));
			RTS.chat.addLine(param);
			RTS.chat.resetColor();
		}
		else if (command.equals("SERVER_WARNING")) {
			RTS.chat.setColor(new Color4f(1.0f, 0.0f, 0.0f, 1.0f));
			RTS.chat.addLine(param);
			RTS.chat.resetColor();
		}
		else if (command.equalsIgnoreCase("REMOTE")) {
			Packet remote = new Packet();
			remote.write(command.toUpperCase() + " " + param);
			RTS.connect.sendPacket(remote);
		}
		else {
			System.out.println("UNKNOWN MESSAGE " + line);
		}
	}
	
	public void makeHost() {
		isHost = true;
		System.out.println("I was made the host.");
	}
	
	public void chat(String line) {
		Packet chat = new Packet();
		chat.write("CHAT " + line);
		RTS.connect.sendPacket(chat);
	}
	
	public int toInt(String s) {
		return new Integer(s).intValue();
	}
	
	public float toFloat(String s) {
		return new Float(s).floatValue();
	}
	
	public void update(Instance i) {
		if (id != -1 && i.networkName.equals(userName)) {
			updatePosition(i);
			updateRotation(i);
			updateRotationSpeed(i);
			updateSpeed(i);
		}
	}
	
	public void updatePosition(Instance i) {
		Packet update = new Packet();
		update.write("UPDATE_POSITION " + i.getClass().getSimpleName() + " " + getInfo(i) + 
				i.getPosition());
		RTS.connect.sendPacket(update);
	}
	
	public void updateRotation(Instance i) {
		Packet update = new Packet();
		update.write("UPDATE_ROTATION " + i.getClass().getSimpleName() + " " + getInfo(i) +
				i.getRotation());
		RTS.connect.sendPacket(update);
	}
	
	public void updateSpeed(Instance i) {
		Packet update = new Packet();
		update.write("UPDATE_SPEED " + i.getClass().getSimpleName() + " " + getInfo(i) +
				i.getSpeed());
		RTS.connect.sendPacket(update);
	}
	
	public void updateRotationSpeed(Instance i) {
		Packet update = new Packet();
		update.write("UPDATE_ROTATION_SPEED " + i.getClass().getSimpleName() + " " + getInfo(i) +
				i.getRotationalSpeed());
		RTS.connect.sendPacket(update);
	}
	
	private void create(String cName, String name, Vec3 position, Vec3 rotation) {
		Instance i = null;
		if (cName.equals("CapitalShip")) {
			i = new CapitalShip(null, null);
		}
		else if (cName.equals("Bullet")) {
			i = new Bullet(null);
		}
		if (i != null) {
			i.setPosition(position);
			i.setRotation(rotation);
			i.networkName = name;
			Render.add(i);
		}
	}
	
	private void updatePosition(String cName, String name, Vec3 vec) {
		Instance i = getInstanceWithName(cName, name);
		if (i == null) {
			create(cName, name, vec, new Vec3(0,0,0));
		}
		else {
			i.setPosition(vec);
		}
	}
	
	private void updateRotation(String cName, String name, Vec3 vec) {
		Instance i = getInstanceWithName(cName, name);
		if (i == null) {
			create(cName, name, new Vec3(0,0,0), new Vec3(0,0,0));
		}
		else {
			i.setRotation(vec);
		}
	}
	
	private void updateRotationSpeed(String cName, String name, Vec3 vec) {
		Instance i = getInstanceWithName(cName, name);
		if (i == null) {
			create(cName, name, new Vec3(0,0,0), new Vec3(0,0,0));
		}
		else {
			i.getState().setRotationalSpeed(vec);
		}
	}
	
	private void updateSpeed(String cName, String name, Vec3 vec) {
		Instance i = getInstanceWithName(cName, name);
		if (i == null) {
			create(cName, name, new Vec3(0,0,0), new Vec3(0,0,0));
		}
		else {
			i.setSpeed(vec);
		}
	}
	
	private Instance getInstanceWithName(String cName, String name) {
		Instance[] list = Render.instanceManager.toArray(new Instance[1]);
		for (Instance i : list) {
			if (i != null && i.getClass().getSimpleName().equals(cName)) {
				if (name.equals(i.networkName)) {
					return i;
				}
			}
		}
		return null;
	}
	
	public void removePlayer(String id) {
		Instance[] list = Render.instanceManager.toArray(new Instance[1]);
		for (Instance i : list) {
			if (i.networkName.equals(id)) {
				Render.instanceManager.remove(i);
			}
		}
	}
	
	public static String getInfo(Instance i) {
		String s = "";
		
		s += i.getInfo();
		
		return s;
	}
}
