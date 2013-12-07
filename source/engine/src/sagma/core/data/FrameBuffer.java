package sagma.core.data;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import sagma.core.render.Game;
import sagma.core.render.Render;

public class FrameBuffer {
	private int frameIndex;
	private int depthIndex;
	private int textureIndex;
	
	private int width;
	private int height;
	
	public FrameBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init() {	
		initDepthBuffer();
		initTexture();
		initFrameBuffer();
	}
	
	private void initFrameBuffer() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		int[] indexStore = new int[1];

		gl.glGenFramebuffers(1, indexStore, 0);
		frameIndex = indexStore[0];
		
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, frameIndex);
		gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, textureIndex, 0);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthIndex);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}
	
	private void initDepthBuffer() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		int[] indexStore = new int[1];
		gl.glGenRenderbuffers(1, indexStore, 0);
		depthIndex = indexStore[0];
		
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthIndex);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL2ES2.GL_DEPTH_COMPONENT, width, height);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthIndex);
		
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
	}
	
	private void initTexture() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glEnable(GL.GL_TEXTURE_2D);
		int[] indexStore = new int[1];
		gl.glGenTextures(1, indexStore, 0);
		textureIndex = indexStore[0];
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex);
		
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glGenerateMipmap(GL.GL_TEXTURE_2D);
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
		gl.glDisable(GL.GL_TEXTURE_2D);
	}
	
	public void copyScene(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		
		gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, frameIndex);
		gl.glBindFramebuffer(GL2GL3.GL_READ_FRAMEBUFFER, 0);
		
		gl.glBlitFramebuffer(
				0, 0, Render.WIDTH, Render.HEIGHT, 
				0, 0, width, height, 
				GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT, GL.GL_LINEAR);
		
		gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, 0);
		
		gl.glPopAttrib();
	}
	
	public void startRendering(GL2 gl) {
		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT);
		gl.glViewport(0, 0, width, height);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, frameIndex);
	}
	
	public void stopRendering(GL2 gl) {
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		gl.glPopAttrib();
	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureIndex);
		
		setOrthoView(gl);
		
        gl.glRotatef(180, 1, 0, 0);

        gl.glBegin(GL2.GL_QUADS);                    
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);    

        gl.glTexCoord2f(0,1);      
        gl.glVertex2f(-1.0f,-1.0f);                

        gl.glTexCoord2f(0,0);  
        gl.glVertex2f(-1.0f,1.0f);     

        gl.glTexCoord2f(1,0);      
        gl.glVertex2f(1.0f,1.0f);             

        gl.glTexCoord2f(1,1);          
        gl.glVertex2f(1.0f,-1.0f);  

        gl.glEnd();

		
		setPerspectiveView(gl);
		
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
	private static void setOrthoView(GL2 gl) {
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);  
		gl.glPushMatrix();
		gl.glLoadIdentity(); 
		gl.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);                
		gl.glPushMatrix();             
		gl.glLoadIdentity();    
	}

	private static void setPerspectiveView(GL2 gl) {
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);         
		gl.glPopMatrix();              
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);      
		gl.glPopMatrix();       
	}
	
	public int getFrameIndex() {
		return frameIndex;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void clear(GL2 gl) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}
}
