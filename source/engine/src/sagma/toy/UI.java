package sagma.toy;

import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.swing.JFrame;

import sagma.core.ui.Button;
import sagma.core.material.Material;
import sagma.core.render.Game;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.awt.TextureRenderer;

public class UI extends JFrame {
	private static final long serialVersionUID = 1L;
	Canvas canvas;

	public UI(){
		super("toy");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		GLCapabilities cap = new GLCapabilities(GLProfile.getDefault());
		cap.setAlphaBits(8);
		canvas = new Canvas(cap, this);

		add(canvas);
		setVisible(true);
	}

	public static void main(String[] args){
		new UI();
	}

	public class Canvas extends GLCanvas  implements GLEventListener, MouseListener, MouseMotionListener, ActionListener{
		private static final long serialVersionUID = 1L;
		TextRenderer textRender;
		TextureRenderer textureRender;
		FPSAnimator animator;
		LinkedList<Material> textures;
		Component parent;
		public ArrayList<Button> buttons;

		public Canvas(GLCapabilities cap, Component parent) {
			super(cap);
			this.parent = parent;
			this.addGLEventListener(this);
			buttons = new ArrayList<Button>();
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		@Override
		public void display(GLAutoDrawable d) {
			GL2 gl = d.getGL().getGL2();
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

			// Apply texture.
			textures.get(0).activate(gl);
			gl.glBegin(GL2.GL_QUADS);            // Draw A Quad

			gl.glColor3f(1.0f, 1.0f, 1.0f);     // Set The Color To White
			gl.glTexCoord2f(1, 0);
			gl.glVertex3f(1f, 1f, 0f);    // Top Right Of The Quad (Front)
			gl.glTexCoord2f(0, 0);
			gl.glVertex3f(-1f, 1f, 0f);   // Top Left Of The Quad (Front)
			gl.glTexCoord2f(0, 1);
			gl.glVertex3f(-1f, -1f, 0f);  // Bottom Left Of The Quad (Front)
			gl.glTexCoord2f(1, 1);
			gl.glVertex3f(1f, -1f, 0f);   // Bottom Right Of The Quad (Front)

			gl.glEnd();
			// Unapply texture
			textures.get(0).deactivate(gl);

//			for (int i=0; i<buttons.size(); i++) {
//				buttons.get(i).draw(gl);
//			}       

			gl.glFlush();
			d.swapBuffers(); // Double buffering to avoid noise and tearing.
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {	}


		@Override
		public void init(GLAutoDrawable d) {
			GL2 gl = d.getGL().getGL2();
			Game.savedDrawable = d; // Manual set because not using official engine.

			gl.glEnable(GL.GL_DEPTH_TEST);
			gl.glDepthFunc(GL.GL_LEQUAL); 
			gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

			textures = new LinkedList<Material>();
			textures.push(Material.getMaterial("src/sagma/games/rts/resources/RTS_1st_test.png"));
			
//			buttons.add(new Button("Campaign", 40, 250, 140, 40, this));
//			buttons.get(0).enable(false);
//			buttons.get(0).setActionCommand("CAMPAIGN");
//			buttons.add(new Button("Skirmish", 40, 300, 140, 40, this));
//			buttons.get(1).setActionCommand("SKIRMISH");
//			buttons.add(new Button("Options", 40, 350, 140, 40, this));
//			buttons.get(2).enable(false);
//			buttons.get(2).setActionCommand("OPTIONS");
//			buttons.add(new Button("Exit", 40, 400, 140, 40, this));
//			buttons.get(3).setActionCommand("EXIT");
//
//			Material t = Material.getMaterial("Texture/Ice.jpg");
//			for(Button b : buttons){
//				b.addActionListener(this);
//				b.setTexture(t);
//			}


			animator = new FPSAnimator(this, 40);
			animator.start();
		}


		@Override
		public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
			GL2 gl = d.getGL().getGL2();
			gl.glViewport(0, 0, width, height);
//			for(Button b : canvas.buttons){
//				b.resize();
//			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {			
//			for(Button b : canvas.buttons){
//				b.testClick(e);
//			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
//			for(Button b : canvas.buttons){
//				b.testHover(e);
//			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
//			for(Button b : canvas.buttons){
//				b.testHover(e);
//			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {	}
		@Override
		public void mouseExited(MouseEvent e ) {	} 
		@Override
		public void mousePressed(MouseEvent e) {	}
		@Override
		public void mouseReleased(MouseEvent e) {	}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "EXIT"){
				System.exit(0);
			}
			else if(e.getActionCommand() == "SKIRMISH"){
				System.out.println("BANGBANG");
			}
			else if(e.getActionCommand() == "CAMPAIGN"){
				System.exit(0);
			}
			else if(e.getActionCommand() == "OPTIONS"){
				System.out.println("Tinkers");
			}

		}

	}
}
