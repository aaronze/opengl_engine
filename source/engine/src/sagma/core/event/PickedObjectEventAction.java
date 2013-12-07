package sagma.core.event;

import sagma.core.data.model.Pickable;

public abstract class PickedObjectEventAction {
	public abstract void eventRecieved(Pickable i, PickedObjectEvent e);
}
