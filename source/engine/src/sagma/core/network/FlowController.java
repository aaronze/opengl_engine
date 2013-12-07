package sagma.core.network;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.PriorityBlockingQueue;

import sagma.core.network.connection.PacketReciever;
import sagma.core.network.shared.Shared;

public class FlowController implements Runnable, PacketReciever {
	public final static int EMERGENCY = 8;
	public final static int DONT_MAKE_ME_PULL_THIS_CAR_OVER = 7;
	public final static int IM_PUTTING_MY_FOOT_DOWN = 6;
	public final static int THIS_IS_YOUR_LAST_WARNING = 5;
	public final static int SEND_IT_OR_ELSE = 4;
	public final static int SERIOUSLY_I_NEED_IT = 3;
	public final static int KIND_OF_NEED_IT = 2;
	public final static int WOULD_BE_NICE = 1;
	public final static int WHO_CARES = 0;
	
	private PriorityBlockingQueue<Shared> priorityQueue;
	private boolean keepGoing = true;
	
	public FlowController() {
		new Thread(this).start();
	}
	
	public void addDelegate(Shared obj) {
		priorityQueue.add(obj);
	}

	@Override
	public void run() {
		priorityQueue = new PriorityBlockingQueue<Shared>();
		
		while (keepGoing) {
			Packet p = new Packet();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ArrayList<Shared> drain = new ArrayList<Shared>();
			priorityQueue.drainTo(drain);
			
			for (Shared obj : drain) {
				if (!p.isFull()) {
					obj.writePacket(p);
					obj.written();
				}
				
				priorityQueue.add(obj);
			}
			
			sendPacket(p);
		}
	}
	
	private void sendPacket(Packet p) {
	}
	
	public void stop() {
		keepGoing = false;
	}
	
	@Override
	public void getPacket(Packet p) {
		String classPath = "";
		String name = "";
		float value = 0;
		
		for (String s : p.getLines()) {
			StringTokenizer tokey = new StringTokenizer(s, ":");
			classPath = new StringTokenizer(tokey.nextToken(), " @").nextToken();
			name = tokey.nextToken();
			value = new Float(tokey.nextToken()).floatValue();
			
			for (Shared obj : priorityQueue) {
				if (obj.isContainerOf(classPath, name)) {
					obj.setValue(new Float(value));
					break;
				}
			}
		}
	}
}
