package sagma.core.network;

/**
 * Represents an object that will be replicated across the network, with whatever value is defined on the server (or client).
 * There are no guarantees about the value of the object, it is up to the developer to find out what value the object should be.
 * @author Mitch
 */
public class NetParam extends NetObject {

	public class New {
		public String key;
		public Object value;
		boolean locked;
	}
	
	public class Deleted {
		public String key;
	}
	
	public class Changed {
		public String key;
		public Object newValue;
	}
	
	public class LockChanged {
		public String key;
		boolean v;
	}
	
	String mKey;
	Object mValue;
	NetServer mInstance;
	boolean mClientCanChange;
	
	public NetParam ( NetServer server, String key ) {
		this.mInstance = server;
		this.mClientCanChange = false;
		
		this.mKey = key;
		this.mValue = null;
	}
	
	public NetParam ( NetServer server, String key, Object value ) {
		this.mInstance = server;
		this.mClientCanChange = false;
		
		this.mKey = key;
		this.mValue = value;
	}
	
	public Object getValue( ) {
		return mValue;
	}
	
	public void setValue ( Object v ) {
		if ( !mInstance.isServer( ) && this.mClientCanChange == false ) {
			return;
		}
		mValue = v;
		// Tell the server to update this value!
		Changed changed = new Changed ();
		changed.newValue = v;
		changed.key = mKey;
		mInstance.sendMessageTCP( changed );
	}
	
}
