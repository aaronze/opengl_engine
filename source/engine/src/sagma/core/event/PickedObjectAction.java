package sagma.core.event;

import sagma.core.data.model.Pickable;
import sagma.core.model.Instance;

public class PickedObjectAction extends PickedObjectListener {
	private Class<? extends Instance> filter;
	private PickedObjectEventAction event;
	
	public PickedObjectAction(Class<? extends Instance> instance, PickedObjectEventAction action) {
		filter = instance;
		event = action;
	}

	@Override
	public void eventRecieved(PickedObjectEvent e) {
		Pickable i = e.getObject();
		if (filter == null || e.getObject() == null) event.eventRecieved(i, e);
		else if (filter.isAssignableFrom(i.getClass())) {
			event.eventRecieved(i, e);
		}
	}

}
