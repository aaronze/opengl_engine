package sagma.core.ui;

import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.ui.layout.LayoutManager;

public class UIContainer extends UIComponent {
	private ArrayList<UIComponent> children = new ArrayList<UIComponent>();
	
	private LayoutManager layout;
	
	public UIContainer(){
		children = new ArrayList<UIComponent>();
	}
	
	public UIContainer(float x, float y, float width, float height){
		setLocation(x, y);
		setSize(width, height);
		children = new ArrayList<UIComponent>();
	}
	
	@Override
	public void draw(GLAutoDrawable d) {
		for(int i = 0; i < children.size(); i++){
			children.get(i).draw(d);
		}
	}
	
	public void add(UIComponent component){
		children.add(component);
		update();
	}
	
	public boolean remove(UIComponent comp){
		boolean u = children.remove(comp);
		update();
		return u;
	}
	
	public void setLayout(LayoutManager l){
		this.layout = l;
	}
	
	@Override
	public void update(){
		if(layout != null){
			layout.update();
		}
		else {
			float divisor = (float)Math.sqrt(children.size());
			float xMax = (float)Math.floor(divisor);
			float yMax = (float)Math.ceil(divisor);
			int xPos = 0;
			int yPos = 0;
			for(int i = 0; i< children.size(); i++){
						children.get(i).setSize(getSize().x / xMax, getSize().y / yMax);
						children.get(i).setLocation((getSize().x/xMax)*xPos, (getSize().y / yMax)*yPos);
						
						if(xPos >= xMax-1){
							yPos ++;
							xPos = 0;
						}
						else{
							xPos++;
						}
			}
		}
	}
	
	public ArrayList<UIComponent> getChildren() {return children;}
}
