package sagma.core.io;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import sagma.core.data.Color4f;
import sagma.core.material.Material;
import sagma.core.model.Instance;
import sagma.core.render.Render;
/**
 * This is a UIComponent abstract to extend into any components intended to be used
 * in OpenGL.
 * It has a backgroundColour, borderColour and texture. It is assumed most components
 * can make use of these.
 * It has an x, y, width and height stored as a percentage of the screen between
 * -1 and 1 and uses the parent width and height to get a pixel value for these.
 * They can be enabled for input or disabled. They can be hovered and activated and
 * the class supplies method blueprints for click and hover tests to be inserted in a
 * mouse or mouse motion listener.
 * Activation will send an event to all actionlisteners associated
 * 
 * TODO
 * - Slider - percentage value or exact number of options on horizontal or vertical bar
 * - TextArea - disabled for label, enabled for interactive
 * - ScrollPanel - scroll area to contain other components
 * - Dropdown List - Combination of buttons and a scroll panel
 * - May consider changing to EventListener for polymorphism but this is dangerous
 * - Will have an aadditional event listener for subtypes instead.
 * 
 * PROBLEMS
 * - Dropdown List will probably use an item listener or similar
 * - Slider will use a state change listener
 * - so on..
 * - Layout manager should be separated from the object
 * - Object should not rely on parent width and height
 * - Display code may be contained in a extendable glcanvas with UIComponent list instead
 * so that its easier to do it completely custom.
 * 
 */
public abstract class OldUIComponent extends Instance {
	public float x, y, width, height;
	public Material texture;
	public Color4f bgColour;
	public Color4f borderColour;

	protected boolean enabled = true;
	protected boolean hovered = false;
	protected boolean activated = false;

	public Component parent;

	private ArrayList<ActionListener> listeners;

	private int actionID;
	private String actionCommand;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height positive float between 0 and 2
	 * @param parent
	 */
	public OldUIComponent(float x, float y, float width, float height, Component parent){
		this.parent = parent;
		this.listeners = new ArrayList<ActionListener>();
		actionID = 0;
		actionCommand = "";
		
		this.bgColour = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.borderColour = new Color4f(0f, 1.0f, 0f, 1.0f);

		this.x = x;
		this.height = -height;
		this.width = width;
		this.y = y;
	}
	
	public OldUIComponent(float x, float y, Component parent){
		this(x, y, 0.1f, 0.1f, parent);
	}
	
	public OldUIComponent(int x, int y, Component parent){
		this(x, y, 5, 5, parent);
	}
	
	public OldUIComponent(int x, int y, int width, int height, Component parent){
		this.parent = parent;
		this.listeners = new ArrayList<ActionListener>();
		actionID = 0;
		actionCommand = "";
		this.bgColour = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.borderColour = new Color4f(0f, 1.0f, 0f, 1.0f);
		setAbsoluteCoords(x, y, width, height);
	}


	public void enable(boolean b) {
		this.enabled = b;
	}

	public boolean activate(){		
		if(enabled == true){
			ActionEvent ae = new ActionEvent(this, actionID, actionCommand);
			actionID++;
			for(int i=0; i<listeners.size();i++){
				listeners.get(i).actionPerformed(ae);
			}
			return true;
		}
		return false;
	}

	
	public abstract void display(GL2 gl);

	public abstract void resize();
	
	public abstract int testClick(MouseEvent e);
	
	public abstract int testHover(MouseEvent e);
	
	/**
	 * Sets the coords to the current window size. If the window is resized,
	 * the button will also resize. Requires a frame parent to position. Will return
	 * false otherwise.
	 * 
	 * @param x top left vertice x
	 * @param y top left vertice y
	 * @param width the overall width of the button
	 * @param height the overall height of the button
	 */
	public void setAbsoluteCoords(int x, int y, int width, int height){
		if(parent != null){
			this.x = (x*2.0f/parent.getWidth())-1f;
			this.y = -(y*2.0f/parent.getHeight())+1f;
			this.width = (width*2.0f/parent.getWidth());
			this.height = -(height*2.0f/parent.getHeight());
		}
		else{
			this.x = (x*2.0f/Render.WIDTH)-1f;
			this.y = -(y*2.0f/Render.HEIGHT)+1f;
			this.width = (width*2.0f/Render.WIDTH);
			this.height = -(height*2.0f/Render.HEIGHT);
		}
	}
	
	/**
	 * Sets the background colour with floats between 1 and 0
	 */
	public void setColour(float r, float b, float g, float a){
		bgColour.set(r, g, b, a);
	}
	
	/**
	 * Sets the background colour with a given colour
	 */
	public void setColour(Color4f colour) {
		bgColour.set(colour);
	}
	
	/**
	 * Sets the hover colour with floats between 1 and 0
	 */
	public void setHoverColour(float r, float b, float g, float a){
		borderColour.set(r, g, b, a);
	}
	
	/**
	 * Sets the hover colour with a given colour
	 */
	public void setHoverColour(Color4f colour) {
		borderColour.set(colour);
	}
	
	/**
	 * Sets the current texture to a given texture or shader
	 */
	public void setTexture(Material t){
		this.texture = t;
	}

	/**
	 * Sets the current texture to a texture or shader at the given filename
	 * Returns false if the texture was not found
	 */
	public boolean setTexture(String filename) {	
		return (texture = Material.getMaterial(filename)) != null;
	}
	
	/**
	 * Reverse of the absolute coordinate operations
	 * Returns absolute coordinate value with origin at top left
	 * @return
	 */
	public int getXCoord(){
		if(parent != null){
			return (int)((this.x+1)*parent.getWidth()/2f);
		}
		return (int)((this.x+1)*Render.WIDTH/2f);
	}

	/**
	 * Reverse of the absolute coordinate operations
	 * Returns absolute coordinate value with origin at top left
	 * @return
	 */
	public int getYCoord(){
		if(parent != null){
			return -(int)((this.y-1)*parent.getHeight()/2f);
		}
		return (int)((this.x+1)*Render.HEIGHT/2f);
		
	}

	/**
	 * Reverse of the absolute coordinate operations
	 * Will return slightly greater than the text width
	 * if too skinny
	 * @return
	 */
	public int getPixelWidth(){
		if(parent != null){
			return (int)((this.width)*parent.getWidth()/2f);
		}
		return (int)((this.width)*Render.WIDTH/2f);
	}

	/**
	 * Reverse of the absolute coordinate operations
	 * Will return slightly greater than the text height
	 * if too short
	 * @return
	 */
	public int getPixelHeight(){
		if(parent != null){
			return -(int)((this.height)*parent.getHeight()/2f);
		}
		return (int)((this.width)*Render.HEIGHT/2f);
	}
	
	public boolean isEnabled(){
		return enabled;
	}

	public boolean isActivated(){
		return activated;
	}

	public boolean isHovered(){
		return hovered;
	}

	public void addActionListener(ActionListener l){
		listeners.add(l);
	}

	public void removeActionListener(ActionListener l){
		listeners.remove(l);
	}

	public void setActionCommand(String command){
		this.actionCommand = command;
	}

	
}
