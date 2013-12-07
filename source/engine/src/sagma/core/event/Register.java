package sagma.core.event;

import java.util.ArrayList;
import java.util.Iterator;

public class Register {
	EventListener listener;
	ArrayList<WildcardEvent> events;
	
	public Register(EventListener parent) {
		listener = parent;
		events = new ArrayList<WildcardEvent>();
	}
	
	public void addEvent(WildcardEvent e) {
		events.add(e);
	}
	
	public void eventRecieved(WildcardEvent e) {
		if (wantsEvent(e)) {
			listener.recieveEvent(e);
		}
	}
	
	public boolean wantsEvent(WildcardEvent e) {
		Iterator<WildcardEvent> iter = events.iterator();
		while (iter.hasNext()) {
			WildcardEvent i = iter.next();
			if (i.getClass().equals(e.getClass()))
				return true;
		}
		return false;
	}
	
	public EventListener listener() {
		return listener;
	}
}
