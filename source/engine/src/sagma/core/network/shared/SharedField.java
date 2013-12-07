package sagma.core.network.shared;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import sagma.core.network.Packet;

public class SharedField extends Shared {
	private Field data;
	private Object parent;
	private String parentName;
	private String valueName;
	
	public SharedField(Object caller, String valueName, int priority) {
		super(priority);
		try {
			data = caller.getClass().getField(valueName);
			parent = caller;
			
			StringTokenizer tokey = new StringTokenizer(parent.getClass().toString(), " @");
			tokey.nextToken();
			parentName = tokey.nextToken();
			
			this.valueName = valueName;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writePacket(Packet p) {
		try {
			String line = parent + ":" + data.getName() + ":" + data.getDouble(parent);
			p.write(line);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public String getParentName() {
		return parentName;
	}
	
	public String getValueName() {
		return valueName;
	}
	
	public boolean isContainerOf(String cName, String vName) {
		return cName.equals(parentName) && vName.equals(valueName);
	}
	
	public void setValue(Object o) {
		float f = ((Float)o).floatValue();
		try {
			data.setFloat(parent, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
