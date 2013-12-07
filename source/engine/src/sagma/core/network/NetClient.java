package sagma.core.network;

import java.io.IOException;

import sagma.core.network.NetServer.ObjectMessage;

import com.esotericsoftware.kryonet.*;

/**
 * @category Networking
 * @author Mitch
 *
 */
public class NetClient extends NetServer {

	public NetClient ( ) {
		mEndpoint = new Client();
		mConnected = false;
		mIsServer = false; // Set this to false...
		init( );
	}
	
	@Override
	public void bind ( int tcp, int udp ) {
		throw new UnsupportedOperationException( );
	}
	
	/**
	 * @category TCP Message
	 */
	@Override
	public void sendMessageTCP ( Object o ) {
		( (Client) mEndpoint ).sendTCP( o );
	}
	
	@Override
	/**
	 * DO NOT USE
	 */
	public void sendMessageTCP ( Connection c, Object o ) {
		throw new UnsupportedOperationException( );
	}
	
	/**
	 * DO NOT USE
	 */
	@Override
	void sendMessageTCP ( NetObject obj, Object data ) {
		throw new UnsupportedOperationException( );
	}
	
	/**
	 * @category UDP Message
	 */
	@Override
	public void sendMessageUDP ( Object o ) {
		( (Client) mEndpoint ).sendUDP ( o );
	}

	/**
	 * DO NOT USE
	 */
	@Override
	public void sendMessageUDP ( Connection c, Object o ) {
		throw new UnsupportedOperationException( );
	}
	
	/**
	 * DO NOT USE
	 */
	void sendMessageUDP ( NetObject obj, Object data ) {
		throw new UnsupportedOperationException( );
	}
	
	public void disconnect ( ) {
		disconnect ( "" );
	}
	
	public void disconnect ( String reason ) {
		//TODO: Send disconnect reason to the server.
		((Client)mEndpoint).close();
	}
	
	public void connect ( int x, String server, int tcp, int udp ) {
		try {
			((Client)mEndpoint).connect( x, server, tcp, udp );
			mConnected = true;
		} catch (IOException e) {
			mConnected = false;
		}
	}
	
}
