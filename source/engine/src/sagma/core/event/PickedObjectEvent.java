package sagma.core.event;

import sagma.core.data.model.Pickable;
import sagma.core.math.Vec2;

public class PickedObjectEvent extends WildcardEvent {
	private Pickable pickedObject;
	private Vec2 mousePosition;
	private int buttonPressed;
	
	public PickedObjectEvent(Pickable object, Vec2 mousePos, int mouseButton) {
		pickedObject = object;
		mousePosition = mousePos;
		buttonPressed = mouseButton;
	}
	
	public PickedObjectEvent() {
		
	}
	
	public Pickable getObject() {
		return pickedObject;
	}
	
	public Vec2 getMousePosition() {
		return mousePosition;
	}
	
	public int getButton(){
		return buttonPressed;
	}

}
