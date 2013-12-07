package sagma.core.input;

public abstract class KeyBind {
	private boolean keyDown;
	
	public void setKeyDown(boolean val) {keyDown = val;}
	public boolean isKeyDown() {return keyDown;}
	
	public abstract boolean isKey(int key);
	public abstract void keyPressed();
	public abstract void keyReleased();
}
