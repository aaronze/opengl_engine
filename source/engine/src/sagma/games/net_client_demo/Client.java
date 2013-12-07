package sagma.games.net_client_demo;

import javax.media.opengl.GLAutoDrawable;

import com.esotericsoftware.kryonet.Connection;

import sagma.core.network.NetClient;
import sagma.core.network.NetServer;
import sagma.games.net_server_demo.PingMessage;
import sagma.games.net_server_demo.PongMessage;
import sagma.games.net_server_demo.Server;

public class Client extends Server {

	NetClient mClient;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		
		mClient = new NetClient ( );
		registerTypes ( mClient );
		
		mClient.addListener( this );
		mClient.start ( ); // This causes the client to run threaded.
		
		mClient.connect( 5000, "localhost", SERVER_TCP, SERVER_UDP );
	}
	
	@Override
	public void received(Connection c, Object o) {
		// TODO Auto-generated method stub
		if ( o instanceof PingMessage ) {
			System.out.println( "Recieved Ping #" + ( (PingMessage) o ).i + " from server. Sending Pong back" );
			PongMessage pong = new PongMessage ( (( PingMessage ) o).i );
			mClient.sendMessageTCP ( pong );
		}
	}
	
	@Override
	public void heartbeat ( ) {
	}
	
}
