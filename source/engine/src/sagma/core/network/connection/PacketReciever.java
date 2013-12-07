package sagma.core.network.connection;

import sagma.core.network.Packet;

public interface PacketReciever {
	public abstract void getPacket(Packet p);
}
