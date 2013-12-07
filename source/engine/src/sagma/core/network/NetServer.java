package sagma.core.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.RegisteredClass;
import com.esotericsoftware.kryonet.*;

/**
 * Represents a Network Server
 * @category Networking
 * @author Mitch
 */
public class NetServer extends Listener {
	
	/**
	 * @category Network Message
	 * @author Mitch
	 */
	class ObjectMessage {
		
		public ObjectMessage ( int i, Object o ) {
			objectLookup = i;
			message = o;
		}
		
		int objectLookup;
		Object message;
		
	}
	
	/**
	 * @category Network Message
	 * @author Mitch
	 */
	class ObjectAdded {
		int targetID;
		int classID;
		Object arguments;
	}
	
	/**
	 * @category Network Message
	 * @author Mitch
	 */
	class ObjectRemoved {
		int objectID;
	}
	
	/**
	 * @category Network Message
	 * @author Mitch
	 *
	 */
	class DisconnectedMessage {
		
		public DisconnectedMessage(String string) {
			reason = string;
		}
		
		String reason;
	}
	
	/**
	 * @category Constructor
	 */
	public NetServer ( ) {
		mEndpoint = new Server();
		mListeners = new LinkedList < NetListener > ( );
		mParameters = new HashMap < String, NetParam > ( );
		init( );
	}
	
	/**
	 * @category Initalisation
	 */
	public void bind ( int tcp, int udp ) {
		try {
			( (Server) mEndpoint ).bind( tcp, udp );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a class for use in sending information between client -> server and server -> client.
	 * @param c
	 * @category Initalisation
	 */
	public void registerClass ( Class c ) {
		mKryo.register( c );
	}
	
	/**
	 * @category Initalisation
	 */
	protected void init( ) {
		
		mObjects = new HashMap < Integer, NetObject > ( );

		mEndpoint.addListener( this );
		
		// Register the core messages that we use...
		mKryo = mEndpoint.getKryo( );
		mKryo.register( ObjectMessage.class );
		mKryo.register( ObjectAdded.class );
		mKryo.register( ObjectRemoved.class );
		mKryo.register( DisconnectedMessage.class );
		mKryo.register( NetParam.Changed.class );
		mKryo.register( NetParam.LockChanged.class );
		mKryo.register( NetParam.New.class );
		
	}
	
	/**
	 * Starts automatic message polling.
	 */
	public void start ( ) {
		mEndpoint.start( );
	}
	
	/**
	 * Add's an object...
	 * @param o
	 * @category Object Management
	 */
	public void addObject ( NetObject o ) {
		
		Object params = o.getConstructParams( );

		o.mUID = mNewObjectID;
		mObjects.put( mNewObjectID , o );

		ObjectAdded msg = new ObjectAdded( );
		msg.arguments = o;
		msg.classID = mKryo.getRegisteredClass( o.getClass() ).getID( );
		msg.targetID = mNewObjectID++;
		
		// We want to ensure that the create object message get's sent to the client.
		this.sendMessageTCP( msg );
		
	}
	
	/**
	 * @category TCP Message
	 */
	public void sendMessageTCP ( Object o ) {
		( (Server) mEndpoint ).sendToAllTCP ( o );
	}
	
	/**
	 * @category TCP Message
	 */
	void sendMessageTCP ( NetObject obj, Object data ) {
		sendMessageTCP( new ObjectMessage ( obj.mUID, data ) );
	}
	
	public void sendMessageTCP ( Connection c, Object o ) {
		( (Server) mEndpoint ).sendToTCP ( c.getID( ), o );
	}
	
	/**
	 * @category UDP Message
	 */
	public void sendMessageUDP ( Object o ) {
		( (Server) mEndpoint ).sendToAllUDP ( o );
	}
	
	public void sendMessageUDP ( Connection c, Object o ) {
		( (Server) mEndpoint ).sendToUDP ( c.getID( ), o );
	}
	
	/**
	 * @category UDP Message
	 */
	void sendMessageUDP ( NetObject obj, Object data ) {
		sendMessageUDP( new ObjectMessage ( obj.mUID, data ) );
	}
	
	/**
	 * @category Listener Event
	 */
	@Override
	public void received ( Connection connection, Object data ) {
		
		if ( data instanceof ObjectAdded ) { 
			ObjectAdded msg = ( ObjectAdded ) data;
			RegisteredClass c = mKryo.getRegisteredClass( msg.classID );
			NetObject i = null;
			for ( NetListener l : mListeners ) {
				NetObject result = l.createObject( c.getType() );
				if ( result != null ) {
					i = result;
					break;
				}
			}
			if ( i == null ) {
				try {
					i = (NetObject) c.getType().newInstance();
				} catch ( Exception e ) {
				}
				if ( i == null ) {
					// Failed to create.
					return;
				}
			}
			i.mUID = msg.targetID;
			i.createFromNetwork( msg.arguments );
			mObjects.put( msg.targetID, i );
			for ( NetListener l : mListeners ) {
				l.newObject( i );
			}
		}
		else if ( data instanceof ObjectRemoved ) {
			// Remove the object.
			ObjectRemoved msg = ( ObjectRemoved ) data;
			if ( mObjects.containsKey ( msg.objectID ) ) {
				mObjects.get( msg.objectID ).netRemoved ( );
			}
		}
		else if ( data instanceof ObjectMessage ) {
			ObjectMessage msg = ( ObjectMessage )data;
			if ( mObjects.containsKey( msg.objectLookup ) ) {
				mObjects.get( msg.objectLookup ).netUpdate( msg.message );
			}
		}
		else if ( data instanceof NetParam.New ) {
			if ( this.isServer() ) {
				// Should never get here!
				return;
			}
			NetParam.New n = (NetParam.New)data;
			NetParam p = new NetParam ( this, n.key, n.value );
			p.mClientCanChange = n.locked;
			this.mParameters.put( n.key, p );		
			for ( NetListener l : mListeners ) {
				l.parameterCreated ( p );
			}
		}
		else if ( data instanceof NetParam.Changed ) {
			if ( this.isServer ( ) ) {
				return;
			}
			NetParam.Changed c = (NetParam.Changed)data;
			NetParam p = this.mParameters.get( c.key );
			p.mValue = c.newValue;
			for ( NetListener l : mListeners ) {
				l.parameterChanged ( p );
			}
		}
		else if ( data instanceof NetParam.Deleted ) {
			if ( this.isServer ( ) ) {
				return;
			}
			NetParam.Deleted d = (NetParam.Deleted)data;
			this.mParameters.remove( d.key );
			for ( NetListener l : mListeners ) {
				l.parameterDeleted ( d.key );
			}
		}
		else if ( data instanceof NetParam.LockChanged ) {
			if ( this.isServer ( ) ) {
				// -.-
				return;
			}
			NetParam.LockChanged c = (NetParam.LockChanged)data;
			NetParam p = this.mParameters.get( c.key );
			p.mClientCanChange = c.v;
		}
		else if ( data instanceof DisconnectedMessage ) {
			// D:
			// Should probs do more here....
			mConnected = false;
		}
		else {
			// Give the data to the game world and let them decide what to do.
			for ( NetListener l : mListeners ) {
				l.received( connection, data );
			}
		}
		
	}
	
	/**
	 * @category Listener Event
	 */
	@Override
	public void connected (Connection connection) {
		// New connection!
		for ( NetListener i : mListeners ) {
			if ( !i.newConnection( connection ) ) {
				connection.sendTCP( new DisconnectedMessage ( "You were disconnected from the server.\nReason: Connection Request Denied" ) );
				connection.close();
				return;
			}
		}
		// If we get here, all is good!
		// Send the client ALL of the objects that we currently have in our list.
		for ( Entry<Integer, NetObject> o : mObjects.entrySet() ) {
			ObjectAdded a = new ObjectAdded ( );
			a.classID = mKryo.getRegisteredClass( o.getValue().getClass ( ) ).getID( );
			a.targetID = o.getKey();
			a.arguments = o.getValue( ).getConstructParams( );
			this.sendMessageTCP( connection, a );
		}
	}
	
	/**
	 * @category Listener Event
	 */
	@Override
	public void disconnected (Connection connection) {
		// Client has disconnected.
		for ( NetListener i : mListeners ) {
			i.disconnected ( connection );
		}
	}
	
	public void update ( ) {
		
	}
	
	/**
	 * Identifies if the object is running on the client or the server.
	 * @return A boolean indicating if this is a server connection. True if server, false if client.
	 * @category State Determination
	 */
	public boolean isServer ( ) {
		return mIsServer;
	}
	
	public void addListener ( NetListener l ) {
		mListeners.push( l );
	}
	
	public void removeListener ( NetListener l ) {
		mListeners.remove( l );
	}
	
	/**
	 * Indentifies if the connection is active.
	 * @return True if server or connected, false if no connection.
	 * @category State Determination
	 */
	public boolean isConnected ( ) {
		return mConnected;
	}
	
	protected EndPoint mEndpoint = null;
	protected Kryo mKryo = null;
	
	protected HashMap< Integer, NetObject > mObjects;
	private int mNewObjectID = 0;
	
	private LinkedList< NetListener > mListeners;
	private HashMap < String, NetParam > mParameters;
	
	protected boolean mIsServer = true;
	protected boolean mConnected = true;
	
}
