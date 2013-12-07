package sagma.core.ui;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.math.Vec2;

public abstract class UIComponent {
	private float x;
	private float y;
	private float width;
	private float height;
	private boolean isVisible;
	private boolean hasFocus;
	private boolean isMouseOver;
	
	
	public abstract void draw(GLAutoDrawable d);
	
	public void mousePressed(float x, float y){
		
	}
	public void mouseReleased(float x, float y){
		
	}
	public void getFocus(){
		hasFocus = true;
	}
	public void loseFocus(){
		hasFocus = false;
	}
	
	public void setVisible(boolean vis){
		isVisible = vis;
	}
	public void keyPressed(int key){
		
	}
	public void keyReleased(int key){
		
	}
	public void mouseOver(){
		getFocus();
	}
	public void mouseExit(){
		loseFocus();
	}
	public void update(){
		
	}
	
	public Vec2 getLocation() { return new Vec2(x, y);}
	public Vec2 getSize() { return new Vec2(width, height);}
	public void setLocation(float x, float y) {this.x = x; this.y = y;}
	public void setSize(float width, float height) { this.width = width; this.height = height;}
	
}
