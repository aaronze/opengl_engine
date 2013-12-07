package sagma.games.net_server_demo;

import javax.media.opengl.GLAutoDrawable;

import com.esotericsoftware.kryonet.Connection;

import sagma.core.network.NetListener;
import sagma.core.network.NetObject;
import sagma.core.network.NetParam;
import sagma.core.network.NetServer;
import sagma.core.render.Game;

public class Server extends Game implements NetListener {
	
	public final int SERVER_TCP = 34000;
	public final int SERVER_UDP = 34001;
	
	private int mPingID;
	private int mTicks;
	
	NetServer mServer = null;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		mServer = new NetServer ( );
		registerTypes ( mServer );

		mServer.addListener( this );
		mServer.start ( ); // This causes the server to run threaded.
		
		mServer.bind( SERVER_TCP, SERVER_UDP );		
		
		mPingID = 0;
		mTicks = 0;
	}
	
	public void heartbeat ( ) {
		
		mTicks++;
		if ( mTicks > 24 ) {
			mTicks -= 24;
			System.out.println( "Sending Ping #" + ++mPingID + " to clients..." );
			PingMessage ping = new PingMessage ( mPingID );
			mServer.sendMessageTCP( ping );
		}
		
	}
	
	protected void registerTypes ( NetServer s ) {
		s.registerClass( PingMessage.class );
		s.registerClass( PongMessage.class );
	}

	@Override
	public void received(Connection c, Object o) {
		// TODO Auto-generated method stub
		if ( o instanceof PongMessage ) {
			System.out.println( "Recieved Pong #" + ( (PongMessage) o ).i + " from " + c.getRemoteAddressTCP().toString() );
		}
	}

	@Override
	public boolean newConnection(Connection c) {
		System.out.println( "New Client connected!" );
		return true;
	}

	@Override
	public void disconnected(Connection c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NetObject createObject(Class targetClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newObject(NetObject o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterCreated(NetParam v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterChanged(NetParam v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parameterDeleted(String key) {
		// TODO Auto-generated method stub
		
	}

	
	
}
