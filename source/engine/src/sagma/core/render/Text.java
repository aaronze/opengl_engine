package sagma.core.render;

import sagma.core.data.Color4f;

public class Text {
	public Color4f col = Color4f.WHITE;
	private String text;
	private int x;
	private int y;
	
	public Text(String s, int x, int y) {
		this.text = s;
		this.x = x;
		this.y = y;
	}
	
	public String getText() {
		return text;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
