package sagma.games.rtsServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.render.Game;

public class Server extends Game {
	private ServerSocket ss;
	private LinkedList<ServerThread> users;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		new Report("Hello. Testing");
		
		try {
			Thread.sleep(1000);
			ss = new ServerSocket(1424);
			System.out.println("Waiting...");
			
			while (true) {
				Socket s = ss.accept();
				ServerThread st = new ServerThread(s);
				st.start();
				users.add(st);
				System.out.println("Connected.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
