package sagma.core.network;

import com.esotericsoftware.kryonet.Connection;

/**
 * Represents an interface for communicating with the network environment.
 * @author Mitch
 * @category Networking
 */
public interface NetListener {
	
	/**
	 * Event that is fired when a packet that is not destined for a object arrives.
	 * @category Messages
	 */
	void received ( Connection c, Object o );
	
	/**
	 * Event that is fired when a new connection attempt is made to the server.
	 * @param c The connection.
	 * @return True if connection attempt is successful, or false if connection attempt is rejected.
	 * @category Connection Management
	 */
	boolean newConnection ( Connection c );
	
	/**
	 * Event that is fired when a connection closes.
	 * @param c The connection.
	 * @category Connection Management
	 */
	void disconnected ( Connection c );
	
	/**
	 * Event that is fired when a new object needs to be created.
	 * @param targetClass
	 * @return This method should return either a class that matches what is requested. OR null if it can't.
	 * @category Object Management
	 */
	NetObject createObject ( Class targetClass );
	
	/**
	 * Event that is fired when a new object has been sent from the server.
	 * @param o
	 * @category Object Management
	 */
	void newObject ( NetObject o );
	
	/**
	 * 
	 * @param v
	 */
	void parameterCreated ( NetParam v );
	
	/**
	 * Event that is fired when a parameter gets its value changed.
	 * @param v The parameter in question.
	 */
	void parameterChanged ( NetParam v );
	
	/**
	 * Event that is fired when a parameter is deleted.
	 * @param key
	 */
	void parameterDeleted ( String key );
	
}
