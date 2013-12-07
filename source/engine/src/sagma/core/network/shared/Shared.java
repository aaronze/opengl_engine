package sagma.core.network.shared;

import sagma.core.network.Packet;

public abstract class Shared implements Comparable<Object> {
	private int priority;
	private long lastSent = System.nanoTime();
	
	public Shared(int basePriority) {
		priority = basePriority;
	}
	
	public void written() {
		lastSent = System.nanoTime();
	}

	@Override
	public final int compareTo(Object o) {
		if (getPriority() > ((Shared)o).getPriority())
			return -1;
		else if (getPriority() < ((Shared)o).getPriority())
			return 1;
		return 0;
	}
	
	public final int getPriority() {
		int base = priority;
		int time = (int)((System.nanoTime() - lastSent)/1000);
		
		return base*time;
	}
	
	public abstract void writePacket(Packet p);
	public abstract void setValue(Object o);
	public abstract boolean isContainerOf(String cName, String vName);
}
