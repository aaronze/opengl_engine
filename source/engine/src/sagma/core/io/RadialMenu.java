package sagma.core.io;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.event.PickedObjectEvent;
import sagma.core.event.PickedObjectListener;
import sagma.core.event.WildcardEvent;
import sagma.core.material.ShaderTools;
import sagma.core.material.Texture;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.render.Game;
import sagma.core.render.Render;
import static sagma.core.math.Math.*;

public class RadialMenu extends RadialMenuItem {
	private boolean HAS_GRAVITY = true;
	public final static float DEFAULT_RADIUS = 0.5f;
	RadialMenuItem selected = null;
	
	private ArrayList<RadialMenuItem> items = new ArrayList<RadialMenuItem>(16);
	private float radius = DEFAULT_RADIUS;
	private Instance parent;
	
	public float direction = 180;
	public float spread = 360;
	
	public RadialMenu() {
		init();
	}
	
	public RadialMenu(ArrayList<RadialMenuItem> items) {
		this.items = items;
		init();
	}
	
	public RadialMenu(RadialMenuItem[] items) {
		for (RadialMenuItem item : items) {
			add(item);
		}
		init();
	}
	
	public void setParent(Instance p) {
		parent = p;
	}
	
	private void init() {
		Render.register(new PickedObjectListener() {
			@Override
			public void recieveEvent(WildcardEvent e) {
				if (selected != null) selected.triggerEvent();
			}
			@Override
			public void eventRecieved(PickedObjectEvent e) {
			}
			
		}, new PickedObjectEvent());
	}
	
	public void add(RadialMenuItem item) {
		items.add(item);
	}
	
	public void setGravity(boolean b) {
		HAS_GRAVITY = b;
	}
	
	public void show() {
		setVisible(true);
	}
	
	public void hide() {
		setVisible(false);
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		if (isVisible()) {
			GL2 gl = drawable.getGL().getGL2();
			gl.glPushMatrix();
			gl.glTranslatef(getPosition().x, getPosition().y, getPosition().z);
			
			Vec2 v = Game.getMousePositionNormalized();
			
			float deg = spread / items.size();
			float angle = direction - spread/2;
			
			float bestDist = Float.MAX_VALUE;
			RadialMenuItem bestItem = null;
			
			for (RadialMenuItem item : items) {
				if (item instanceof RadialMenu) {
					RadialMenu menu = (RadialMenu)item;
					menu.spread = 145;
					menu.direction = angle;
				}
				
				gl.glPushMatrix();
				float x = (sin(angle) * radius);
				float y = (cos(angle) * radius);
				
				Vec2 p = Render.getScreenCoordOfPoint(new Vec3(x, y, 0));
				if (parent != null) p.m_add(parent.getPosition());
				
				float dist = sqrt(pow(v.x-p.x, 2)+pow(v.y-p.y, 2));
				if (dist < bestDist) {
					bestDist = dist;
					bestItem = item;
				}
				
				gl.glTranslatef(x, y, 0);
				if (!HAS_GRAVITY) gl.glRotatef(-angle, 0, 0, 1);
				
				if (selected == item) {
					ShaderTools.BRIGHTER.activate(gl);
					item.draw(drawable);
					ShaderTools.BRIGHTER.deactivate(gl);
				} else {
					item.draw(drawable);
				}
				
				gl.glPopMatrix();
				
				angle += deg;
			}
			
			if (bestItem != null) selected = bestItem;
			
			gl.glPopMatrix();
		}
	}
	
	public RadialMenuItem getSelected() {
		return selected;
	}
	
	public void add(Model m) {
		add(new RadialMenuItem(m));
	}
	
	public void add(String s) {
		add(new RadialMenuItem(s));
	}
	
	public void add(Texture t) {
		add(new RadialMenuItem(t));
	}
}
