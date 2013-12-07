package sagma.core.event;

public abstract class PickedObjectListener implements EventListener {

	@Override
	public void recieveEvent(WildcardEvent e) {
		if (e.getClass().equals(PickedObjectEvent.class)) {
			eventRecieved((PickedObjectEvent)e);
		}
	}
	
	public abstract void eventRecieved(PickedObjectEvent e);

}
