package sagma.core.event.system;

public class MemoryLowEvent extends SystemEvent {
	private String message;
	public MemoryLowEvent(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
