package sagma.core.event;

import sagma.core.model.Instance;

public class CollisionAction extends CollisionListener {
	public Class<? extends Instance> typeA;
	public Class<? extends Instance> typeB;
	public CollisionEventAction listener;
	
	public CollisionAction(Class<? extends Instance> a, Class<? extends Instance> b, CollisionEventAction event) {
		typeA = a;
		typeB = b;
		listener = event;
	}

	@Override
	public void eventRecieved(CollisionEvent e) {
		Object a = e.getObjectA();
		Object b = e.getObjectB();
		
		if (typeA.isAssignableFrom(a.getClass()) && typeB.isAssignableFrom(b.getClass())) {
			listener.actionEvent(a, b);
		}
		if (typeA.isAssignableFrom(b.getClass()) && typeB.isAssignableFrom(a.getClass())) {
			listener.actionEvent(b, a);
		}
	}

}
