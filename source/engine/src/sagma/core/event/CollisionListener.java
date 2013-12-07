package sagma.core.event;

public abstract class CollisionListener implements EventListener {
	@Override
	public void recieveEvent(WildcardEvent e) {
		try {
			CollisionEvent col = (CollisionEvent) e;
			eventRecieved(col);
		} catch (Exception f) {
		}
	}
	public abstract void eventRecieved(CollisionEvent e);
}
