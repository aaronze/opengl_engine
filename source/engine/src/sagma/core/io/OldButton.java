package sagma.core.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.material.Material;
import sagma.core.material.Texture;
import sagma.core.render.Render;

import com.jogamp.opengl.util.awt.TextRenderer;

import static sagma.core.material.ShaderTools.*;




/**
 * This is a button intended to be used in OpenGL. 
 * It resizes according to its width, height and contents. It will not shrink smaller than any text it contains.
 * If it contains an icon, the icon will not grow larger than the iconHeight but the button will grow around it. 
 * It will activate similar to buttons in swing, firing an actionevent to every actionlistener associated.
 * It can be textured and have an icon and text or any combination in between.
 * This class is preliminary until the UI engine is conceptualized.
 * 
 * TODO
 * - Have them keep a preferred width
 * - Have them aware of each other's bounds so they don't overlap - may require button container
 * - Hover should select only the top button if overlapped
 * - Circular buttons
 * - buttons that keep the correct distance between each other unless forced together
 * - Bevelled edges for up and sunken edges for down
 * - Greyscale texture when disabled
 * 
 * PROBLEMS
 * - Layout manager should be separated from the object
 * - Object should not rely on parent width and height
 * - Display code is messy and inefficient - should be contained in a extendable glcanvas with UIComponent list instead
 * 
 * @author Michael
 *
 */
public class OldButton extends OldUIComponent{
	private int minWidth;
	private int minHeight;

	public String text;
	private Color textColour;
	
	public Texture icon;
	public int iconHeight;
	
	private boolean tooShort = false;
	private boolean tooSkinny = false;

	private TextRenderer textRenderer;
	

	/**
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height - enter a positive float between 0 and 2
	 * @param frame
	 */
	public OldButton(String text, float x, float y, float width, float height, Component parent){
		super(x, y, width, height, parent);
		this.text = text;
		this.textColour = new Color(0f, 0f, 0f, 1.0f);
		this.textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

		setMins();
		iconHeight = -1;
	}

	public OldButton(String text, float x, float y, Component parent){
		super(x, y, parent);
		this.text = text;
		this.textColour = new Color(0f, 0f, 1.0f, 1.0f);
		this.textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

		setMins();
		if (parent != null) {
			this.height = -(minHeight+5) * 2f / parent.getHeight();
			this.width = (minWidth+5) * 2f / parent.getWidth();
		} else {
			this.height = -(minHeight+5) * 2f / Render.HEIGHT;
			this.width = (minWidth+5) * 2f / Render.WIDTH;
		}
		iconHeight = -1;
	}

	public OldButton(String text, int x, int y, Component parent){
		super(x,y,parent);
		this.text = text;
		this.textColour = new Color(0f, 0f, 1.0f, 1.0f);
		this.textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

		setMins();
		setAbsoluteCoords(x, y, minWidth +5, minHeight +5);
		iconHeight = -1;
	}

	public OldButton(String text, int x, int y, int width, int height, Component parent){
		super(x, y, width, height, parent);
		this.text = text;
		this.textColour = new Color(0f, 0f, 1.0f, 1.0f);
		textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 24));

		setMins();
		iconHeight = -1;
	}
	
	@Override
	public void draw(GLAutoDrawable drawable) {
		display(drawable.getGL().getGL2());
	}

	/**
	 * To be called in the main display method. Encapsulates drawing code.
	 * THIS IS VERY POORLY CODED
	 * @param gl
	 */
	@Override
	public void display(GL2 gl){
		// Grey out disabled buttons
		if (!enabled) GRAYSCALE.activate(gl);
		
		float w = width;
		float h = height;
		if(tooSkinny){
			w =  (minWidth*2.0f/parent.getWidth());
		}
		if(tooShort){
			h = -(minHeight*2.0f/parent.getHeight());
		}

		if(icon != null && text != "" && text != null){
			bgColour.apply(gl);

			if(texture!=null){        
				texture.activate(gl);

				gl.glBegin(GL2.GL_QUADS);
				gl.glColor3f(1.0f, 1.0f, 1.0f);

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w, y, 0f);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w, y+h, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);

				gl.glEnd();
				texture.deactivate(gl);
			}
			else{
				gl.glRectf(x, y, x+w, y+h);
			}
        
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

			icon.activate(gl);

			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(1.0f, 1.0f, 1.0f);

			if(iconHeight > getPixelHeight() || iconHeight < 0){
				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);
				gl.glTexCoord2f(1, 0);
				//THIS MATHS IS TERRIBLE
				gl.glVertex3f(-1+(getXCoord()+(getPixelHeight()*icon.getAspectRatio()))*2f/parent.getWidth(), y, 0f);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(-1+(getXCoord()+(getPixelHeight()*icon.getAspectRatio()))*2f/parent.getWidth(), y+h, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);
			}
			else{
				float iw = (iconHeight*icon.getAspectRatio())*2.0f/parent.getWidth();
				float ih =  - (float)iconHeight* 2.0f / parent.getHeight();

				//BAD MATHS
				gl.glTexCoord2f(0f, 0f);
				gl.glVertex3f(x+(((getPixelHeight()*icon.getAspectRatio())*2f/parent.getWidth())/2)-(iw/2), y+(h/2)-ih/2, 0f);
				gl.glTexCoord2f(1f, 0f);
				gl.glVertex3f(x+(((getPixelHeight()*icon.getAspectRatio())*2f/parent.getWidth())/2)+(iw/2), y+(h/2)-ih/2, 0f);
				gl.glTexCoord2f(1f, 1f);
				gl.glVertex3f(x+(((getPixelHeight()*icon.getAspectRatio())*2f/parent.getWidth())/2)+(iw/2), y+(h/2)+ih/2, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x+(((getPixelHeight()*icon.getAspectRatio())*2f/parent.getWidth())/2)-(iw/2), y+(h/2)+ih/2, 0f);
			}
			gl.glEnd();

			icon.deactivate(gl);
			gl.glFlush();

			Rectangle2D textBounds = textRenderer.getBounds(text);
			int textWidth = (int)textBounds.getMaxX();
			int textHeight = (int)textBounds.getMinY();

			textRenderer.beginRendering(parent.getWidth(), parent.getHeight());
			textRenderer.setColor(textColour);
			int start = (int)(getXCoord() +(getPixelHeight()*icon.getAspectRatio()));
			textRenderer.draw(text, start + ((getPixelWidth()-(int)(getPixelHeight()*icon.getAspectRatio()))  >> 1) - (textWidth>>1), 
					parent.getHeight()-getYCoord() - ((getPixelHeight()>>1) - (textHeight>>1)));
			textRenderer.endRendering();
		}
		else if(icon != null){

			bgColour.apply(gl);

			if(texture!=null){
				if(getPixelHeight() < iconHeight){
					h = -iconHeight*2f/parent.getHeight();
				}
				if(getPixelWidth() < iconHeight*icon.getAspectRatio()){
					w = iconHeight*2f/parent.getWidth();
				}
				
				texture.activate(gl);

				gl.glBegin(GL2.GL_QUADS);
				gl.glColor3f(1.0f, 1.0f, 1.0f);

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w, y, 0f);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w, y+h, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);

				gl.glEnd();
				texture.deactivate(gl);
			}
			else{
				gl.glRectf(x, y, x+w, y+h);
			}

			gl.glEnable(GL.GL_TEXTURE_2D);	        
			icon.activate(gl);
			gl.glBegin(GL2.GL_QUADS);    
			gl.glColor3f(1.0f, 1.0f, 1.0f);

			if(iconHeight > 0){
				float hiw = (iconHeight*icon.getAspectRatio()*2f/parent.getWidth())/2;
				float hih = (iconHeight*2f/parent.getHeight())/2;

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x+w/2-hiw, y+h/2+hih, 0f);    // Top Left Of The Quad (Front)
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w/2+hiw, y+h/2+hih, 0f);   // Top Right Of The Quad (Front)
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w/2+hiw, y+h/2-hih, 0f);  // Bottom Right Of The Quad (Front)
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x+w/2-hiw, y+h/2-hih, 0f);   // Bottom Left Of The Quad (Front)

			}
			else if(getPixelHeight()*icon.getAspectRatio() < getPixelWidth()){
				float hiw = (getPixelHeight()*icon.getAspectRatio()*2f/parent.getWidth())/2;

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x+w/2-hiw, y, 0f);    // Top Left Of The Quad (Front)
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w/2+hiw, y, 0f);   // Top Right Of The Quad (Front)
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w/2+hiw, y+h, 0f);  // Bottom Right Of The Quad (Front)
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x+w/2-hiw, y+h, 0f);   // Bottom Left Of The Quad (Front)
			}
			else{
				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);    // Top Left Of The Quad (Front)
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w, y, 0f);   // Top Right Of The Quad (Front)
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w, y+h, 0f);  // Bottom Right Of The Quad (Front)
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);   // Bottom Left Of The Quad (Front)


			}
			gl.glEnd();

			icon.deactivate(gl);
			gl.glFlush();
		}
		else if(text != null && text != ""){
			bgColour.apply(gl);

			if(texture!=null){
				texture.activate(gl);

				gl.glBegin(GL2.GL_QUADS);
				gl.glColor3f(1.0f, 1.0f, 1.0f);

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w, y, 0f);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w, y+h, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);

				gl.glEnd();
				texture.deactivate(gl);
			}
			else{
				gl.glRectf(x, y, x+w, y+h);
			}

			if (parent != null)
				textRenderer.beginRendering(parent.getWidth(), parent.getHeight());
			else 
				textRenderer.beginRendering(Render.WIDTH, Render.HEIGHT);
			Rectangle2D textBounds = textRenderer.getBounds(text);
			int textWidth = (int)textBounds.getMaxX();
			int textHeight = (int)textBounds.getMinY();

			textRenderer.setColor(textColour);
			int parentHeight = Render.HEIGHT;
			if (parent != null) parentHeight = parent.getHeight();
			textRenderer.draw(text, getXCoord() + ((getPixelWidth()>>1) - (textWidth>>1)), 
					parentHeight-getYCoord() - ((getPixelHeight()>>1) - (textHeight>>1)));
			textRenderer.endRendering();
		}
		else{
			bgColour.apply(gl);

			if(texture!=null){
				texture.activate(gl);

				gl.glBegin(GL2.GL_QUADS);
				gl.glColor3f(1.0f, 1.0f, 1.0f);

				gl.glTexCoord2f(0, 0);
				gl.glVertex3f(x, y, 0f);
				gl.glTexCoord2f(1, 0);
				gl.glVertex3f(x+w, y, 0f);
				gl.glTexCoord2f(1, 1);
				gl.glVertex3f(x+w, y+h, 0f);
				gl.glTexCoord2f(0, 1);
				gl.glVertex3f(x, y+h, 0f);

				gl.glEnd();
				texture.deactivate(gl);
			}
			else{
				gl.glRectf(x, y, x+w, y+h);
			}
		}

		if(isHovered()){
			if(isEnabled()){
				borderColour.apply(gl);
			}
			else{
				gl.glColor3f(0.2f, 0.2f, 0.2f);
			}
			gl.glLineWidth(2f);
			gl.glBegin(GL.GL_LINES);

			gl.glVertex2d(x, y);             
			gl.glVertex2d(x+w, y); 
			gl.glVertex2d(x+w, y);
			gl.glVertex2d(x+w, y+h); 
			gl.glVertex2d(x+w, y+h); 
			gl.glVertex2d(x, y+h);
			gl.glVertex2d(x, y+h);
			gl.glVertex2d(x, y);                 
			gl.glEnd(); 
		}
		
		if (!enabled) GRAYSCALE.deactivate(gl);
	}

	/**
	 * To be called with the gl reshape method.
	 * Will resize the button but not shrink it past the text width and height.
	 */
	@Override
	public void resize() {
		int h = 0;
		int w = 0;
		if(parent != null){
			h = - (int)((this.height)*parent.getHeight()/2f);
			w = (int)((this.width)*parent.getWidth()/2f);
		}

		if(w < minWidth){
			tooSkinny = true;
		}
		else{
			tooSkinny = false;
		}
		if(h < minHeight){
			tooShort = true;
		}
		else{
			tooShort = false;
		}

		setMinWidth();
	}


	/**
	 * To be called with a mouse listener
	 * @param e
	 * @return 1 - button enabled and clicked, 
	 * 0 - button disabled and clicked, 
	 * -1 - button click did not hit
	 * 
	 */
	@Override
	public int testClick(MouseEvent e){
		float mX = (e.getX()*2.0f/parent.getWidth())-1f;
		float mY = - (e.getY()*2.0f/parent.getHeight())+1f;

		float w = width;
		float h = height;
		if(tooSkinny){
			w =  (minWidth*2.0f/parent.getWidth());
		}
		if(tooShort){
			h = -(minHeight*2.0f/parent.getHeight());
		}


		if(mX>x && mX < x+w){
			if(mY<y && mY > y+h){
				if(activate()){
					return 1;
				}
				hovered = true;
				return 0;
			}
		}
		return -1;
	}

	/**
	 * To be called with a mouse motion listener
	 * @param e
	 * @return 1 - button enabled and clicked, 
	 * 0 - button disabled and clicked, 
	 * -1 - button click did not hit
	 * 
	 */
	@Override
	public int testHover(MouseEvent e){
		float mX = (e.getX()*2.0f/parent.getWidth())-1f;
		float mY = - (e.getY()*2.0f/parent.getHeight())+1f;

		float w = width;
		float h = height;
		if(tooSkinny){
			w =  (minWidth*2.0f/parent.getWidth());
		}
		if(tooShort){
			h = -(minHeight*2.0f/parent.getHeight());
		}

		if(mX>x && mX < x+w){
			if(mY<y && mY > y+h){
				if(isEnabled()){
					hovered = true;
					return 1;
				}
				hovered = true;
				return 0;
			}
		}
		hovered = false;
		return -1;
	}



	public void setText(String text){
		this.text = text;
		setMins();
	}
	
	public void setFont(Font font){
		textRenderer = new TextRenderer(font);
		setMins();
	}
	
	/**
	 * Sets the text colour with floats between 1 and 0
	 */
	public void setTextColour(float r, float b, float g, float a){
		textColour = new Color(r, g, b, a);
	}

	
	public void setIcon(Texture t){
		this.icon = t;
		setMins();
	}
	
	public boolean setIcon(String filename){		
		this.icon = (Texture)Material.getMaterial(filename);
		setMins();
		return true;
	}
	
	public void setIconHeight(int h){
		this.iconHeight = h;
	}


	/**
	 * Reverse of the absolute coordinate operations
	 * Returns absolute coordinate value with origin at top left
	 * @return
	 */
	@Override
	public int getXCoord(){
		if(parent != null){
			return (int)((this.x+1)*parent.getWidth()/2f);
		}
		return -99;
	}
	
	/**
	 * Reverse of the absolute coordinate operations
	 * Returns absolute coordinate value with origin at top left
	 * @return
	 */
	@Override
	public int getYCoord(){
		if(parent != null){
			return -(int)((this.y-1)*parent.getHeight()/2f);
		}
		return -99;
	}
	
	/**
	 * Reverse of the absolute coordinate operations
	 * Will return slightly greater than the text width
	 * if too skinny
	 * @return
	 */
	@Override
	public int getPixelWidth(){
		float w = this.width;
		if(tooSkinny){
			w =  (minWidth*2.0f/parent.getWidth());
		}
		if(parent != null){
			return (int)((w)*parent.getWidth()/2f);
		}
		return -99;
	}
	
	/**
	 * Reverse of the absolute coordinate operations
	 * Will return slightly greater than the text height
	 * if too short
	 * @return
	 */
	@Override
	public int getPixelHeight(){
		float h = this.height;
		if(tooShort){
			h =  -(minHeight*2.0f/parent.getHeight());
		}
		if(parent != null){
			return -(int)((h)*parent.getHeight()/2f);
		}
		return -99;
	}

	public int getIconHeight(){
		return iconHeight;
	}


	/**
	 * Must call in this order. Convenience method to reduce bugs.
	 */
	private void setMins(){
		setMinHeight();
		setMinWidth();
	}

	private void setMinHeight(){
		if(text != null && text != ""){
			Rectangle2D textBounds = textRenderer.getBounds(text);
			int textHeight = -(int)textBounds.getMinY();
			this.minHeight = (int)(textHeight * 1.5);
		}
		else if(icon != null){
			if(iconHeight > 0){
				this.minHeight = iconHeight;
			}
		}
		else{
			this.minHeight = 5;
		}

	}

	private void setMinWidth(){
		if(icon != null && text != null && text != ""){
			Rectangle2D textBounds = textRenderer.getBounds(text);
			int textWidth = (int)textBounds.getMaxX();
			this.minWidth = (int)(textWidth + 4 + (getPixelHeight() * icon.getAspectRatio()));
		}
		else if(text != null && text != ""){
			Rectangle2D textBounds = textRenderer.getBounds(text);
			int textWidth = (int)textBounds.getMaxX();
			this.minWidth = textWidth + 4;
		}
		else if(icon != null){
			if(iconHeight > 0){
				this.minWidth = (int)(iconHeight * icon.getAspectRatio());
			}
		}
		else{
			this.minWidth = 5;
		}
	}

}

