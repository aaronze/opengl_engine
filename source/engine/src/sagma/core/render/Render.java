package sagma.core.render;

/**
 *
 * @author Aaron Kison
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.MemoryImageSource;
import java.io.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.jogamp.opengl.util.awt.TextRenderer;
import static javax.media.opengl.GLProfile.GL3;

import sagma.Driver;
import sagma.core.data.FrameBuffer;
import sagma.core.data.manager.ModelManager;
import sagma.core.data.model.Collidable;
import sagma.core.data.model.Pickable;
import sagma.core.event.CollisionEvent;
import sagma.core.event.PickedObjectEvent;
import sagma.core.event.Register;
import sagma.core.event.WildcardEvent;
import sagma.core.input.KeyBind;
import sagma.core.io.FileIO;
import sagma.core.light.*;
import sagma.core.material.Material;
import sagma.core.material.Shader;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.ui.UIComponent;

public class Render implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
	public static int WIDTH = 768;
	public static int HEIGHT = 768;
	public static float MOTION_BLUR = 0.6f;

	public static float POINTER_ACCURACY = 0.1f;
	public static boolean MOUSE_ENABLED = true;

	public static boolean[] FLAGS = new boolean[2];
	public final static int FLAG_MEMORY = 0;
	public final static int FLAG_FPS = 1;

	private static int[] memoryBuffer = new int[400];
	private static int memoryIndex = 0;

	public static int FRAMES_PER_SECOND = 24;

	static long time;
	static int frames;
	static long timeCounter;
	public static int frameRate;
	
	static long stepTime;
	static int steps;
	static long stepCounter;
	public static int stepRate;

	public static boolean[] keys;   
	public static Camera camera;

	public static LinkedList<Instance> instanceManager;
	private static ArrayList<Instance> uiInstanceManager;  //TODO Remove this Aaron
	private static ArrayList<UIComponent> uiManager; 
	
	private static Timer stepTimer;
	private static ArrayList<KeyBind>keyBindings;
	private static ArrayList<Register> registers;

	private static Game game;
	private static FloatBuffer matrix;

	public static Robot robot;
	public static boolean paused;

	static boolean needsModeSwitch = false;
	static int newMode = 0;

	public final static FloatBuffer black = FloatBuffer.wrap(new float[] {0,0,0,1});
	public final static FloatBuffer white = FloatBuffer.wrap(new float[] {1,1,1,1});
	public final static FloatBuffer red = FloatBuffer.wrap(new float[] {1,0,0,1});
	public static FloatBuffer GLOBAL_AMBIENT = FloatBuffer.wrap(new float[] {1,1,1,1});

	public static File downloadDir = new File("src/");
	private static int totalComments = 0, totalSize = 0;

	public static boolean LIGHTING_ENABLED = true;
	public static boolean BLOOM_ENABLED = true;
	public final static int RENDER_IDLE = 0;
	public final static int RENDER_DRAW = 1;
	public final static int RENDER_LIGHTING = 2;
	public final static int RENDER_BLOOM = 3;
	public final static int RENDER_UI = 4;
	public static int renderPass = RENDER_IDLE;
	public static LightManager lightManager = new LightManager();

	private static FrameBuffer bloom;
	private static FrameBuffer render;
	private static FrameBuffer blur;
	
	private static Material trans;
	private static Material blurShader;
	public static Material darker;
	
	public static TextRenderer textRender;
	private static Vec2 mousePosition = new Vec2(0,0);
	private static Vec2 mousePositionNormalized = new Vec2(0,0);
    
    public static void bloom(GL2 gl) {
    	gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
    	
    	bloom.clear(gl);
    	
    	gl.glDisable(GL.GL_DEPTH_TEST);
    	gl.glDisable(GL.GL_BLEND);
    	gl.glBindFramebuffer(GL2GL3.GL_READ_FRAMEBUFFER, render.getFrameIndex());
    	gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, bloom.getFrameIndex());
    	
    	gl.glBlitFramebuffer(
    			0, 0, render.getWidth(), render.getHeight(),
    			0, 0, bloom.getWidth(), bloom.getHeight(),
    			GL.GL_COLOR_BUFFER_BIT, GL.GL_LINEAR);
    	
    	gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, blur.getFrameIndex());
    	darker.activate(gl);
    	int loc = gl.glGetUniformLocation(((Shader)darker).getID(), "factor");
    	gl.glUniform1f(loc, MOTION_BLUR);
    	blur.draw(gl);
    	darker.deactivate(gl);
    	
    	gl.glDisable(GL.GL_DEPTH_TEST);
    	gl.glEnable(GL.GL_BLEND);
    	gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
    	blurShader.activate(gl);
    	bloom.draw(gl);
    	blurShader.deactivate(gl);
    	
    	gl.glBindFramebuffer(GL2GL3.GL_READ_FRAMEBUFFER, blur.getFrameIndex());
    	gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, bloom.getFrameIndex());
    	
    	gl.glBlitFramebuffer(
    			0, 0, blur.getWidth(), blur.getHeight(),
    			0, 0, bloom.getWidth(), bloom.getHeight(),
    			GL.GL_COLOR_BUFFER_BIT, GL.GL_LINEAR);
    	
    	gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
    	gl.glEnable(GL.GL_BLEND);
    	gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, render.getFrameIndex());
    	bloom.draw(gl);

    	gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    	gl.glPopAttrib();
    }
	
	
	public Render(Game g) {
		game = g;
	}

	private static void reset() {
		instanceManager = new LinkedList<Instance>();
		camera = new Camera(new Vec3(0, 0, 0), new Vec3(0, 0, 0));
		keyBindings = new ArrayList<KeyBind>(16);
		registers = new ArrayList<Register>();
		keys = new boolean[525];
		time = System.nanoTime();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		Game.savedDrawable = drawable;
		matrix = FloatBuffer.wrap(new float[16]);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		GL2 gl = drawable.getGL().getGL2();	
		set3DMode(drawable);

		gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		render = new FrameBuffer(WIDTH, HEIGHT);
		bloom = new FrameBuffer(WIDTH/4, HEIGHT/4);
		blur = new FrameBuffer(WIDTH, HEIGHT);
		
		trans = Material.getMaterial("Shaders/Texture/transparency");
		blurShader = Material.getMaterial("Shaders/Texture/gaussian5");
		darker = Material.getMaterial("Shaders/Texture/darker");
		
		uiInstanceManager = new ArrayList<Instance>(); //TODO remove this
		uiManager = new ArrayList<UIComponent>();
		textRender = new TextRenderer(new Font("SansSerif", Font.BOLD, 20));
		textRender.setColor(Color.WHITE);
		
		System.out.println("------------------------");
		System.out.println(System.getProperty("os.name") + " " + System.getProperty("os.version"));
		System.out.println("JRE Version: " + System.getProperty("java.version"));
		System.out.println("CPU Processors: " + Runtime.getRuntime().availableProcessors());
		System.out.println(gl.glGetString(GL.GL_RENDERER));
		System.out.println("Loaded engine with OpenGL " + gl.glGetString(GL.GL_VERSION));
		System.out.println("------------------------");
		String error = getError(drawable.getGL().getGL2());
		if (error.equals("")) {
			System.out.println("Engine Says: It's a nice day to be an engine, isn't it?");
		} else {
			System.out.println("Engine Says: I don't want to alarm you, but you seem to have an " + error.substring(0, error.indexOf('-')-1) + " error");
		}
		
		// Fetch new game
		loadGame(drawable);
		
		if (!Driver.glCanvas.hasFocus()) Driver.glCanvas.requestFocus();

		stepTimer = new Timer(1000/FRAMES_PER_SECOND, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				step();
			}
		});
		
		stepTimer.start();
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.gc();
				System.gc();
			}
		});
	}

	private static void loadGame(GLAutoDrawable drawable) {
		reset();
		game.initialize(drawable);
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		time = System.nanoTime();
		if (ModelManager.hasMoreToLoad()) ModelManager.loadAll(drawable);
		
		renderPass = RENDER_DRAW;

		if (needsModeSwitch) {
			needsModeSwitch = false;
			if (newMode == Game.GAMEMODE_2D) set2DMode(drawable);
			if (newMode == Game.GAMEMODE_3D) set3DMode(drawable);
		}

		GL2 gl = drawable.getGL().getGL2();
		render.startRendering(gl);
		gl.glPushMatrix();
		
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glLoadIdentity();
		gl.glScalef(HEIGHT*1.0f/WIDTH, 1.0f, 1.0f);
		setupCamera(gl);

		gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL.GL_DEPTH_TEST);

		if (!paused) {
			setupBaseScene(gl);
			LightManager.deactivate();
			
			gl.glEnable(GL.GL_BLEND);
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
			trans.activate(gl);
			
			drawScene(drawable);
			
			trans.deactivate(gl);

			if (LIGHTING_ENABLED) {
				renderPass = RENDER_LIGHTING;
				drawScene(drawable);
			}

			if (FLAGS[FLAG_FPS]) displayFPS(gl);
			if (FLAGS[FLAG_MEMORY]) displayMemory(gl);

		}
		gl.glGetFloatv(GLMatrixFunc.GL_MODELVIEW_MATRIX, matrix);
		
		gl.glPopMatrix();
		gl.glPopAttrib();
		
		renderPass = RENDER_BLOOM;

		render.stopRendering(gl);
		if (BLOOM_ENABLED) bloom(gl);
		render.draw(gl);
		
		renderPass = RENDER_UI;
		for (Instance i : uiInstanceManager) {  //TODO remove this
			i.draw(drawable);
		}
		
		for (UIComponent i : uiManager) {
			i.draw(drawable);
		}
		
		
		renderPass = RENDER_IDLE;
		
		// Calculate the frame rate
		long currentTime = System.nanoTime();
		long timePassed = currentTime - time;
		timeCounter += timePassed; 
		frames++;
		if (timeCounter > 1000000000L) {frameRate = frames; timeCounter -= 1000000000L; frames = 0;}
	}

	private static void setupBaseScene(GL2 gl) {
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ZERO);
		gl.glEnable(GL.GL_BLEND);

		gl.glDepthMask(true);
		gl.glDepthFunc(GL.GL_LEQUAL);

		gl.glLightModelfv(GL2ES1.GL_LIGHT_MODEL_AMBIENT, GLOBAL_AMBIENT);
	}

	private static void setupCamera(GL2 gl) {
		gl.glRotatef(camera.rotX(), 1, 0, 0);
		gl.glRotatef(camera.rotY(), 0, 1, 0);
		gl.glRotatef(camera.rotZ(), 0, 0, 1);
		gl.glTranslatef(camera.posX(), camera.posY(), camera.posZ());
	}

	@SuppressWarnings("unused")
	private static void setupStencil(GL2 gl) {
		gl.glLightModelfv(GL2ES1.GL_LIGHT_MODEL_AMBIENT, black);

		gl.glEnable(GLLightingFunc.GL_LIGHTING);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ZERO);

		gl.glDepthMask(false);
		gl.glEnable(GL2.GL_STENCIL_TEST_TWO_SIDE_EXT);
	}

	private static void drawScene(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		game.draw(drawable);

		Object[] list = instanceManager.toArray();
		for (Object i : list) {
			gl.glColor4f(1, 1, 1, 1);
			((Drawable)i).draw(drawable);
		}
	}

	private static void displayFPS(GL2 gl) {
		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		String s = "FPS: "+frameRate + "  " + "XYZ: "+camera.getLocation() + "  "+ "HPR: "+camera.getState().getRotation();
		BitmapText.drawText(gl, s, -0.9f, 0.9f, 2);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}

	private static void displayMemory(final GL2 gl) {
		long memoryTotal = Runtime.getRuntime().totalMemory();
		long memoryUsed = memoryTotal - Runtime.getRuntime().freeMemory();
		int perc = (int)(memoryUsed*100/memoryTotal);
		memoryBuffer[memoryIndex] = perc;

		gl.glDisable(GL.GL_DEPTH_TEST);
		gl.glBegin(GL.GL_LINES);
		for (int i = memoryBuffer.length-1; i >= 0; i--) {
			int val = memoryBuffer[i];
			float redU = 0.0f;
			float greenU = 0.0f;
			float blueU = 0.0f;
			if (val < 30) {
				greenU = 1.0f - (val * 0.03f);
				blueU = 1.0f - greenU;
			}
			else if (val < 70) {
				blueU = 1.0f - ((val - 30) * 0.04f);
				redU = 1.0f - blueU;
			}
			else {
				redU = 1.0f - ((val - 70) * 0.03f);
			}
			if (i > memoryIndex) {
				int dif = i - memoryIndex;
				float c = dif * 1.0f / memoryBuffer.length;
				gl.glColor4f(c*redU, c*greenU, c*blueU, c);
			} else if (i < memoryIndex) {
				int dif = memoryIndex - i;
				float c = dif * 1.0f / memoryBuffer.length;
				gl.glColor4f((1.0f-c)*redU, (1.0f-c)*greenU, (1.0f-c)*blueU, 1.0f-c);
			} else {
				gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
			}
			float x = -1.0f + (i * 0.002f);
			float y = -1.0f + (memoryBuffer[i] * 0.004f);
			gl.glVertex2f(x, -1.0f);
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
		gl.glEnable(GL.GL_DEPTH_TEST);

		if (++memoryIndex >= memoryBuffer.length) memoryIndex = 0;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {

	}

	public static void updateScreenSize(int w, int h) {
		WIDTH = w;
		HEIGHT = h;
	}

	public static void add(KeyBind bind) {
		keyBindings.add(bind);
	}

	void step() {
		stepTime = System.nanoTime();
		
		mousePosition = null;
		mousePositionNormalized = null;
		game.step();

		if (!paused) {
			objectStep();
			checkForCollisions();
		}
		
		// Calculate the frame rate
		long currentTime = System.nanoTime();
		long timePassed = currentTime - stepTime;
		stepCounter += timePassed; 
		steps++;
		if (stepCounter > 1000000000L) {stepRate = steps; stepCounter -= 1000000000L; steps = 0;}
			
	}

	public static void objectStep() {
		for (int i = instanceManager.size()-1; i >= 0; i--) {
			try {
				Steppable step = (Steppable)instanceManager.get(i);
				step.step();
			} catch (Exception e) {

			}
		}
		camera.step();
	}

	public static void quit() {
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyPressed(e.getKeyCode(), e.getKeyChar());
		
		int key = e.getKeyCode();
		keys[key] = true;

		for (int i = 0; i < keyBindings.size(); i++) {
			if (keyBindings.get(i).isKey(e.getKeyCode()) && !keyBindings.get(i).isKeyDown()) {
				keyBindings.get(i).keyPressed();
				keyBindings.get(i).setKeyDown(true);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyReleased(e.getKeyCode(), e.getKeyChar());
		
		int key = e.getKeyCode();
		keys[key] = false;

		for (int i = 0; i < keyBindings.size(); i++) {
			if (keyBindings.get(i).isKey(e.getKeyCode())) {
				keyBindings.get(i).keyReleased();
				keyBindings.get(i).setKeyDown(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (game.getGameMode() == Game.GAMEMODE_3D && MOUSE_ENABLED) {
			Point loc = e.getLocationOnScreen();
			int x = (int) loc.getX();
			int y = (int) loc.getY();
			int midX = WIDTH / 2;
			int midY = HEIGHT / 2;
			int xD = midX - x;
			int yD = midY - y;
			try {
				robot.mouseMove(midX, midY);
			} catch (Exception ex) {
				System.out.println(ex);
			}
			if (xD > 30 || yD > 30 || xD < -30 || yD < -30) {
				return;
			}
			float addY = -xD;
			float addX = -yD;
			camera.getState().addRotation(addX, addY, 0);
		}
		game.setMouseCoords(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		game.setMouseCoords(e);
	}

	public static void add(Instance i) {
		instanceManager.add(i);
	}

	public static void register(sagma.core.event.EventListener parent, WildcardEvent e) {
		Iterator<Register> regs = registers.iterator();
		while (regs.hasNext()) {
			Register reg = regs.next();
			if (reg.listener() == parent) {
				reg.addEvent(e);
				return;
			}
		}
		Register reg = new Register(parent);
		reg.addEvent(e);
		registers.add(reg);
	}

	public static void checkForCollisions() {
		Collidable a;
		Collidable b;

		for (int i = instanceManager.size()-1; i >= 0; i--) {
			Object o = instanceManager.get(i);
			if (o != null && Collidable.class.isAssignableFrom(o.getClass())) {
				a = (Collidable)o;
				if (!a.isSolid()) continue;
				for (int j = i-1; j >= 0; j--) {
					o = instanceManager.get(j);
					if (o != null && Collidable.class.isAssignableFrom(o.getClass())) {
						b = (Collidable)o;
						if (!b.isSolid()) continue;
						if (game.getGameMode() == Game.GAMEMODE_3D) {
							if (a.collidesWith(b)) {
								// Trigger a collision event
								triggerEvent(new CollisionEvent(a, b));
							}
						} 
						else if (game.getGameMode() == Game.GAMEMODE_2D) {
							if (a.boundsWith(b)) {
								// Trigger a collision event
								triggerEvent(new CollisionEvent(a, b));
							}
						}
					}
				}
			}
		}
	}

	public static void triggerEvent(WildcardEvent e) {
		for (Register reg : registers) {
			reg.eventRecieved(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Attempt to pick an object from the scene
		int x = e.getX();
		int y = e.getY();
		float cam = -camera.getPosition().z;
		if (cam < 2) cam = 2.0f;
		Vec2 pointerPos = new Vec2((x*cam/WIDTH)-cam/2, -((y*cam/HEIGHT)-cam/2));
		
		float bestDist = Float.MAX_VALUE;
		Pickable bestObject = null;

		// For all pickable objects in the scene
		Iterator<Instance> objects = instanceManager.iterator();
		while (objects.hasNext()) {
			Object o = objects.next();
			if (Pickable.class.isAssignableFrom(o.getClass())) {
				Pickable object = (Pickable)o;
				Vec2 screenPos = getScreenCoordOfPoint(object.getLocation());
				float dist = pointerPos.distanceTo(screenPos);
				if (dist < bestDist) {
					bestDist = dist;
					bestObject = object;
				}
			}
		}
		
		if (bestObject != null) {
			
			// Raise a picked object event if the distance is above threshold
			triggerEvent(new PickedObjectEvent(bestObject, pointerPos, e.getButton()));
		}
		
		Vec2 v = getMousePositionNormalized();
		mousePressed(v.x, v.y);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Vec2 v = getMousePositionNormalized();
		mouseReleased(v.x, v.y);
	}

	public static Vec2 getScreenCoordOfPoint(Vec3 v) {
		float[] p = matrix.array();
		float[] pm = new float[] {p[0]*v.x + p[4]*v.y + p[8]*v.z + p[12],
				p[1]*v.x + p[5]*v.y + p[9]*v.z + p[13],
				p[2]*v.x + p[6]*v.y + p[10]*v.z + p[14],
				p[3]*v.x + p[7]*v.y + p[11]*v.z + p[15]};

		Vec2 coord = new Vec2(pm[0]/pm[3], pm[1]/pm[3]);
		return coord;
	}

	public static void remove(Object o) {
		int index = instanceManager.indexOf(o);
		if (index >= 0) {
			instanceManager.remove(index);
		}
	}

	public static BufferedInputStream getResourceAsStream(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static ArrayList<String> readFile(File file) {
		BufferedInputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> lines = new ArrayList<String>();
		InputStreamReader reader = new InputStreamReader(stream);
		Scanner sc = new Scanner(reader);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		return lines;
	}

	public static File getFile(String filename) {
		// Figure out if its been downloaded
		File file = new File(filename);
		if (!file.exists()) {
			downloadFile(file);
		}
		return file;
	}

	/**
	 * @param file Not yet implemented 
	 */
	public static void downloadFile(File file) {
		System.out.println("download not yet implemented");
		// TODO download from server
	}

	public static void switchToMode(int mode) {
		needsModeSwitch = true;
		newMode = mode;
	}

	public void set3DMode(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, WIDTH, HEIGHT);
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(50, WIDTH/HEIGHT, 0.1, 100.0);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
	}

	public void set2DMode(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, WIDTH, HEIGHT);
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glOrthof(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
		Driver.glCanvas.setCursor(Cursor.getDefaultCursor());
	}

	public static void printDoc() {
		File main = new File("src/sagma");
		printFile(main);

		int perc = totalComments * 100 / (totalSize + totalComments);
		System.out.println("PROJECT STATS:");
		System.out.println(" lines of code:       " + totalSize);
		System.out.println(" lines of commenting: " + totalComments);
		System.out.println(" ratio of commenting: " + perc + "%");

	}

	private static void printFile(File f) {
		if (f.isDirectory()) {
			File[] children = f.listFiles();
			for (File file : children)
				printFile(file);
					return;
		}

		ArrayList<String> lines = FileIO.read(f);

		int size = lines.size();
		int commentLines = 0;

		for (String line : lines) {
			if (line.equals("")) size--;
			else if (isComment(line)) commentLines++;
		}
		
		totalComments += commentLines;
		totalSize += size;
	}

	private static boolean isComment(String s) {
		char c = s.charAt(0);
		int i = 1;
		int len = s.length();
		while (i < len && c == ' ') c = s.charAt(i++);

		return (c == '/' || c == '*');
	}

	public static void addMouseListener(MouseListener listener) {
		Driver.glCanvas.addMouseListener(listener);
	}

	public static void addMouseMotionListener(MouseMotionListener listener) {
		Driver.glCanvas.addMouseMotionListener(listener);
	}

	public static void addMouseWheelListener(MouseWheelListener listener) {
		Driver.glCanvas.addMouseWheelListener(listener);
	}

	public static void hideMouse() {
		try {
			Driver.glCanvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
					Driver.glCanvas.createImage(
							new MemoryImageSource(16, 16, new int[16 * 16], 0, 16)), 
							new Point(0, 0), "invisiblecursor"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showMouse() {
		try {
			Driver.glCanvas.setCursor(Cursor.getDefaultCursor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addInstanceToUI(Instance i) {  //TODO remove this
		uiInstanceManager.add(i);
	}
	
	public static void removeInstanceFromUI(Instance i) {  //TODO remove this
		uiInstanceManager.remove(i);
	}
	
	public static void addToUI(UIComponent i) {
		uiManager.add(i);
	}
	
	public static void removeFromUI(UIComponent i) {
		uiManager.remove(i);
	}
	
	public static Vec2 getMousePosition() {
		if (mousePosition == null) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			Point screenPos = Driver.glCanvas.getLocationOnScreen();
			int x = p.x - screenPos.x;
			int y = p.y - screenPos.y;
			mousePosition = new Vec2(x,y);
		}
		return mousePosition;
	}
	
	public static Vec2 getMousePositionNormalized() {
		if (mousePositionNormalized == null) {
			Vec2 pos = getMousePosition();
			float cam = -Render.camera.getPosition().z;
			float x = (pos.x*cam/Render.WIDTH) - cam/2;
			float y = -(pos.y*cam/Render.HEIGHT) + cam/2;
			mousePositionNormalized = new Vec2(x,y);
		}
		return mousePositionNormalized;
	}
	
	public static String getError(GL2 gl) {
		int errorCode = gl.glGetError();
		
		if (errorCode == GL.GL_NO_ERROR) {
			return "";
		}
		if (errorCode == GL.GL_INVALID_ENUM) {
			return "Invalid Enumeration - Wrong parameters locally";
		}
		if (errorCode == GL.GL_INVALID_VALUE) {
			return "Invalid Value - Locally a parameter has a value outside bounds";
		}
		if (errorCode == GL.GL_INVALID_OPERATION) {
			return "Invalid Operation - Wrong parameters";
		}
		if (errorCode == GL.GL_OUT_OF_MEMORY) {
			return "Out Of Memory - Why didn't java tell you that?";
		}
		if (errorCode == GL.GL_INVALID_FRAMEBUFFER_OPERATION) {
			return "Invalid Framebuffer Operation - Attempting to read/write to an invalid framebuffer";
		}
		if (errorCode == GL2ES1.GL_STACK_OVERFLOW) {
			return "Stack Overflow - Push Operation was aborted because stack is full";
		}
		if (errorCode == GL2ES1.GL_STACK_UNDERFLOW) {
			return "Stack Underflow - Pop Operation was aborted because stack is empty";
		}
		
		return "GL ERROR CODE SAYS WHAT? - You have serious issues.";
	}
	
	/**
	 * @param x Value between -1 and 1 from the left side of screen to the right side of screen.
	 * @param y Value between -1 and 1 from the bottom of the screen to the top of the screen.
	 */
	public void mousePressed(float x, float y) {
		game.mousePressed(x, y);
	}
	
	/**
	 * @param x Value between -1 and 1 from the left side of screen to the right side of screen.
	 * @param y Value between -1 and 1 from the bottom of the screen to the top of the screen.
	 */
	public void mouseReleased(float x, float y) {
		game.mouseReleased(x, y);
	}
	
	public void keyPressed(int keyCode, char keyChar) {
		game.keyPressed(keyCode, keyChar);
	}
	
	public void keyReleased(int keyCode, char keyChar) {
		game.keyReleased(keyCode, keyChar);
	}
}
