package sagma.core.event;

public class CollisionEvent extends WildcardEvent {
	private Object colliderA;
	private Object colliderB;
	
	public CollisionEvent() {
		
	}
	
	public CollisionEvent(Object a, Object b) {
		colliderA = a;
		colliderB = b;
	}
	
	public Object getObjectA() {
		return colliderA;
	}
	
	public Object getObjectB() {
		return colliderB;
	}
	
	
	
}
