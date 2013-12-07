package sagma.core.network;

/**
 * Represents an object that is persistent between the client and server (Server is responsible for managing the state).
 * @category Networking
 * @author Mitchell Clode
 */
public abstract class NetObject {

	
	public Object getConstructParams( ) {
		return null;
	}
	
	public void createFromNetwork ( Object args ) {
	}
	
	public void netUpdate ( Object o ) {
	}
	
	/**
	 * Function that is called when the object is removed from the network.
	 */
	protected void  netRemoved ( ) {
	}
	
	/**
	 * Sends a message to clients using TCP.
	 */
	protected void sendTCP ( Object message ) {
		// Can't send messages if we aren't the server.
		if ( !isNetwork ( ) || !isServer ( ) ) {
			return;
		}
		mServer.sendMessageTCP( this, message );
	}
	
	/**
	 * Sends a message to clients using UDP.
	 */
	protected void sendUDP ( Object message ) {
		// Can't send messages if we aren't the server.
		if ( !isNetwork ( ) || !isServer ( ) ) {
			return;
		}
		mServer.sendMessageUDP( this, message );
	}
	
	public boolean isServer ( ) {
		return mServer.isServer();
	}
	
	public boolean isNetwork ( ) {
		return ( mServer != null );
	}
	
	/**
	 * Reference to the server/client.
	 */
	private NetServer 	mServer = null;
	
	int 				mUID = 0;
	
}
