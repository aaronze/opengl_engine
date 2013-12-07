package sagma.core.io;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.event.WildcardEvent;
import sagma.core.material.Texture;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.model.ModelConstructor;
import sagma.core.render.Game;

public class RadialMenuItem extends Instance {
	public final static int DEFAULT_WIDTH = 32;
	public final static int DEFAULT_HEIGHT = 32;
	private WildcardEvent event;
	
	private Model icon;
	
	public RadialMenuItem() {
		init();
	}
	
	/**
	 * @param text Not yet implemented 
	 */
	public RadialMenuItem(String text) {
		this(new Texture(Game.savedDrawable.getGL().getGL2(), 
				DEFAULT_WIDTH, DEFAULT_HEIGHT));
	}
	
	public RadialMenuItem(Texture icon) {
		this(ModelConstructor.makeSprite(Game.savedDrawable, icon));
	}
	
	public RadialMenuItem(Model icon) {
		this.icon = icon;
		setScale(0.1f);
		init();
	}
	
	public void setTriggeredEvent(WildcardEvent event) {
		this.event = event;
	}
	
	private void init() {
		setSolid(false);
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glScalef(scale.x, scale.y, scale.z);
		icon.draw(drawable);
	}
	
	public void triggerEvent() {
		if (event != null) event.eventRecieved();
	}
}
