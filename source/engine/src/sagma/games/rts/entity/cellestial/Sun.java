package sagma.games.rts.entity.cellestial;

import java.awt.event.MouseEvent;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.event.DelayedEvent;
import sagma.core.event.WildcardEvent;
import sagma.core.render.Render;
import sagma.games.rts.sfx.MeshExploder;

public class Sun extends Celestial { //TODO
	public Sun() {
		super("Sun");
		setPosition(0,0,1.0f);
		setScale(3.0f);
		setFuel(40000);
		this.setPickable(MouseEvent.BUTTON3, true);
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		super.draw(drawable);
	}
	
	@Override
	public void destroy() {
		setSolid(false);
		new DelayedEvent(2000, new WildcardEvent() {
			@Override
			public void eventRecieved() {
				Render.remove(this);
			}
		});
		new MeshExploder().explode(this);
		
	}
	
}
