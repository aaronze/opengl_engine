package sagma.core.render;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GLAutoDrawable;

import sagma.Driver;
import sagma.core.data.manager.ModelManager;
import sagma.core.event.*;
import sagma.core.event.system.MemoryLowEvent;
import sagma.core.event.system.SystemEvent;
import sagma.core.input.KeyBind;
import sagma.core.light.VirtualLight;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Constructable;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.model.ModelConstructor;
import sagma.core.profile.Profiler;
import sagma.core.ui.UIComponent;
import static sagma.core.render.Render.*;
import static sagma.core.math.Math.*;

/**
 * <b>Game</b> serves as a template and action springboard for all
 * game implementations using the SAGMA engine.
 * 
 * 
 * @author Aaron Kison
 *
 */
public abstract class Game {
	public final static int GAMEMODE_2D = 1;
	public final static int GAMEMODE_3D = 2;
	
	public final static int UP = KeyEvent.VK_UP;
	public final static int LEFT = KeyEvent.VK_LEFT;
	public final static int DOWN = KeyEvent.VK_DOWN;
	public final static int RIGHT = KeyEvent.VK_RIGHT;
	public final static int SPACE = KeyEvent.VK_SPACE;
	public final static int SHIFT = KeyEvent.VK_SHIFT;
	public final static int ESCAPE = KeyEvent.VK_ESCAPE;
	public final static int ENTER = KeyEvent.VK_ENTER;
	public final static int CONTROL = KeyEvent.VK_CONTROL;
	public final static int A = KeyEvent.VK_A;
	public final static int B = KeyEvent.VK_B;
	public final static int C = KeyEvent.VK_C;
	public final static int D = KeyEvent.VK_D;
	public final static int E = KeyEvent.VK_E;
	public final static int F = KeyEvent.VK_F;
	public final static int G = KeyEvent.VK_G;
	public final static int H = KeyEvent.VK_H;
	public final static int I = KeyEvent.VK_I;
	public final static int J = KeyEvent.VK_J;
	public final static int K = KeyEvent.VK_K;
	public final static int L = KeyEvent.VK_L;
	public final static int M = KeyEvent.VK_M;
	public final static int N = KeyEvent.VK_N;
	public final static int O = KeyEvent.VK_O;
	public final static int P = KeyEvent.VK_P;
	public final static int Q = KeyEvent.VK_Q;
	public final static int R = KeyEvent.VK_R;
	public final static int S = KeyEvent.VK_S;
	public final static int T = KeyEvent.VK_T;
	public final static int U = KeyEvent.VK_U;
	public final static int V = KeyEvent.VK_V;
	public final static int W = KeyEvent.VK_W;
	public final static int X = KeyEvent.VK_X;
	public final static int Y = KeyEvent.VK_Y;
	public final static int Z = KeyEvent.VK_Z;
	public final static int ONE = KeyEvent.VK_1;
	public final static int TWO = KeyEvent.VK_2;
	public final static int THREE = KeyEvent.VK_3;
	public final static int FOUR = KeyEvent.VK_4;
	public final static int FIVE = KeyEvent.VK_5;
	public final static int SIX = KeyEvent.VK_6;
	public final static int SEVEN = KeyEvent.VK_7;
	public final static int EIGHT = KeyEvent.VK_8;
	public final static int NINE = KeyEvent.VK_9;
	public final static int ZERO = KeyEvent.VK_0;
	
	public boolean isPreLoad = false;
	public int percentComplete = 0;
	
	public enum WriteFlag {WARNING, OVERWRITE, SILENT, DEBUG };
	private static WriteFlag writeFlag = WriteFlag.WARNING;
	
	public enum Direction { DIR_UP, DIR_DOWN, DIR_LEFT, DIR_RIGHT, FORWARD, BACK };
	
	private int gameMode = GAMEMODE_2D;
	public static float PLAYER_SPEED = 0.1f;
	
	public abstract void init(GLAutoDrawable drawable);
	
	private boolean defaultBindings;
	public static GLAutoDrawable savedDrawable;
	public static boolean displayHUD = false;
	private boolean isFullscreen = false;
	
	private Vec2 lastMouseCoords = new Vec2(0,0);
	
	/**
	 * Adds a new key binding the the engine.
	 * 
	 */
	public static void add(KeyBind bind) {
		Render.add(bind);
	}
	
	/**
	 * You might want to touch this. But don't.
	 * Seriously don't!
	 */
	public final void initialize(GLAutoDrawable drawable) {
		savedDrawable = drawable;
		isPreLoad = false;
		init(drawable);
		//touch!
	}
	
	/**
	 * Yea, don't use this one either.
	 */
	public void preLoadAll() {
		if (!isPreLoad) {
			isPreLoad = true;
			try {
				preload();
			} catch (Exception e) {
				System.out.println("You must use pre-loading compatible methods in pre-load!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Actions to be performed before the application has technically loaded.
	 * 
	 * This is a good spot to start loading and reading files and
	 * setting up data structures.
	 * 
	 * Warning: Completion not guaranteed before the start of the application.
	 * 
	 */
	public void preload() {
		
	}
	/**
	 * <p>
	 * Binds the escape key to immediately quitting the application.
	 * </p>
	 * 
	 * <p>
	 * Applications that require save states should avoid this function
	 *  or override the {@link #exit() exit} method.
	 * </p>
	 */
	public void setEscapeKeyToExit() {
		add(new KeyBind() {

			@Override
			public boolean isKey(int key) {
				return key == KeyEvent.VK_ESCAPE;
			}

			@Override
			public void keyPressed() {
				exit();
			}

			@Override
			public void keyReleased() {
			}
			
		});
	}
	
	/**
	 * <p>
	 * Immediately terminates the engine
	 * </p>
	 * 
	 * <p>
	 * Override to change the function of {@link #setEscapeKeyToExit() setEscapeKeyToExit}
	 * </p>
	 */
	public void exit() {
		System.out.println(Profiler.print());
		System.exit(0);
	}
	
	/**
	 * Adds a new Instance to the engine to be used and drawn if possible.
	 * 
	 * @see Instance
	 */
	public static void add(Instance instance) {
		Render.add(instance);
	}
	
	/**
	 * Returns the screen width as recorded by the Rendering Engine
	 */
	public static int width() {
		return WIDTH;
	}
	
	/**
	 * Returns the screen height as recorded by the Rendering Engine
	 */
	public static int height() {
		return HEIGHT;
	}
	
	/**
	 * <p>May or may not be the centre of the screen.</p>
	 * 
	 * <p>For simple cases, it should be the centre.</p>
	 */
	@Deprecated
	public static Vec3 centreOfScreen() {
		return camera.getState().getLocation();
	}
	
	/**
	 * <p>
	 * Makes a new model or sprite based upon the filename given.
	 * </p>
	 * 
	 * <p>
	 * </br>If a supported model format (.obj) is found, then a model is created
	 * </br>If a supported sprite format (.jpg, .png, .gif, .bmp) is found, then a sprite is created
	 * </p>
	 * 
	 * <p>
	 * This method automatically packs the model or sprite into an Instance and adds 
	 * the instance to the rendering engine.
	 * </p>
	 * 
	 * <p>
	 * If you do not want your objects auto-packed, for example you want to re-use a model,
	 * then you should use {@link #makeModel(GLAutoDrawable, String) makeModel(drawable, filename)}
	 * 
	 * @see Instance
	 * 
	 * @param drawable The drawable context given
	 * @param filename Relative path and name of the file
	 * @return
	 */
	public static Instance make(GLAutoDrawable drawable, String filename) {
		String ext = filename.substring(filename.indexOf('.')+1, filename.length());
		if (ext.equalsIgnoreCase("obj")) {
			Instance instance = ModelConstructor.makeInstance(drawable, filename, 1);
			add(instance);
			return instance;
		}
		if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")|| ext.equalsIgnoreCase("gif")
				|| ext.equalsIgnoreCase("bmp")) {
			Instance instance = ModelConstructor.makeSpriteInstance(drawable, filename);
			add(instance);
			return instance;
		}
		return null;
	}
	
	/**
	 * @see #make(GLAutoDrawable, Constructable)
	 */
	public static Instance make(String filename) {
		return make(savedDrawable, filename);
	}
	
	/**
	 * Another method to not touch. Required by engine.
	 */
	public void updateSavedDrawable(GLAutoDrawable drawable) {
		savedDrawable = drawable;
	}
	
	/**
	 * Creates a new Model using the specified file. 
	 * 
	 * For more reading see {@link #make(GLAutoDrawable, String) make(drawable, filename)}
	 * 
	 * @param drawable
	 * @param filename
	 * @return
	 */
	public static Model makeModel(GLAutoDrawable drawable, String filename) {
		String ext = filename.substring(filename.indexOf('.')+1, filename.length());
		Model model = null;
		if (ext.equalsIgnoreCase("obj")) {
			model = ModelConstructor.makeModel(drawable, filename, 1);
		}
		if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png")|| ext.equalsIgnoreCase("gif")
				|| ext.equalsIgnoreCase("bmp")) {
			model = ModelConstructor.makeSprite(drawable, filename);
		}
		return model;
	}
	
	/**
	 * @see #makeModel(GLAutoDrawable, String)
	 */
	public static Model makeModel(String filename) {
		return makeModel(savedDrawable, filename);
	}
	
	/**
	 * <p>Sets the current game mode.</p>
	 * 
	 * <p>
	 * Valid options are:
	 * <ul>
	 * <li>{@link #GAMEMODE_2D GAMEMODE_2D}
	 * <li>{@link #GAMEMODE_3D GAMEMODE_3D}
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * The default is GAMEMODE_2D
	 * </p>
	 * 
	 * @param mode
	 */
	public void setGameMode(int mode) {
		gameMode = mode;
		switchToMode(mode);
	}
	
	/**
	 * Gets the current game mode
	 */
	public int getGameMode() {
		return gameMode;
	}
	
	/**
	 * <p>Adds a new collision listener, usually in the form of a 
	 * {@link CollisionAction CollisionAction.</p>
	 * 
	 * <p>
	 * This event will fire when one applicable instance intersects another applicable instance
	 * </p>
	 * 
	 * <p>
	 * Instances can be made immune to collisions by setting their solidarity with 
	 * {@link Instance#setSolid(boolean) setSolid(boolean)}
	 * </p>
	 * 
	 * @see Instance
	 * @see CollisionAction
	 * 
	 * @param listener
	 */
	public void add(CollisionListener listener) {
		register(listener, new CollisionEvent());
	}
	
	/**
	 * Replaced with {@link #add(CollisionListener) add(CollisionListener)}
	 * 
	 * @param listener
	 */
	@Deprecated
	public void addCollisionListener(CollisionListener listener) {
		register(listener, new CollisionEvent());
	}
	
	/**
	 * <p>
	 * Adds a new picked object listener, usually in the form of a 
	 * {@link PickedObjectAction PickedObjectAction}.</p>
	 * 
	 * <p>
	 * A picked object is an object triggered by a mouse or other
	 * input device clicking or otherwise selecting an applicable object
	 * in the scene.
	 * </p>
	 * 
	 * <p>
	 * By default all items are pickable, to render an object immune to picking
	 * the {@link Instance#setPickable(boolean) setPickable(boolean) method can be used.</p>
	 * 
	 * @see Instance
	 * @see PickedObjectAction
	 * 
	 * @param listener
	 */
	public void add(PickedObjectListener listener) {
		register(listener, new PickedObjectEvent());
	}
	
	/**
	 * Replaced with {@link #add(PickedObjectListener) add(PickedObjectListener}
	 */
	@Deprecated
	public void addPickedObjectListener(PickedObjectListener listener) {
		register(listener, new PickedObjectEvent());
	}
	
	/**
	 * <p>Returns the direction from the first vector to the second.</p>
	 * 
	 * <p>
	 * Possible values are:
	 * <ul>
	 * <li>{@link Direction#DIR_UP UP}
	 * <li>{@link Direction#DIR_DOWN DOWN}
	 * <li>{@link Direction#DIR_LEFT LEFT}
	 * <li>{@link Direction#DIR_RIGHT RIGHT}
	 * <li>{@link Direction#FORWARD FORWARD}
	 * <li>{@link Direction#BACK BACK}
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Note: You will only see FORWARD and BACK directions when in GAMEMODE_3D
	 * </p>
	 * 
	 * @see #setGameMode(int)
	 */
	public Direction directionFrom(Vec3 v1, Vec3 v2) {
		float x = v1.x - v2.x;
		float y = v1.y - v2.y;
		float z = v1.z - v2.z;
		
		float xSize = Math.abs(x);
		float ySize = Math.abs(y);
		float zSize = Math.abs(z);
		
		if (gameMode == GAMEMODE_3D) {
			if (xSize > ySize && xSize > zSize) {
				if (x > 0) return Direction.DIR_LEFT;
				return Direction.DIR_RIGHT;
			} 
			if (ySize > xSize && ySize > zSize){
				if (y > 0) return Direction.DIR_DOWN;
				return Direction.DIR_UP;
			}
			if (zSize > xSize && zSize > ySize) {
				if (z > 0) return Direction.FORWARD;
				return Direction.BACK;
			}
		}
		if (gameMode == GAMEMODE_2D) {
			if (xSize > ySize) {
				if (x > 0) return Direction.DIR_LEFT;
				return Direction.DIR_RIGHT;
			}
			if (y > 0) return Direction.DIR_DOWN;
			return Direction.DIR_UP;
		}
		return null;
	}
	
	/**
	 * Returns the direction from Instance A to Instance B
	 * 
	 * @see #directionFrom(Vec3, Vec3)
	 */
	public Direction directionFrom(Instance a, Instance toB) {
		Vec3 posA = a.getState().getPosition();
		Vec3 posB = toB.getState().getPosition();
		return directionFrom(posA, posB);
	}
	
	/**
	 * <p>
	 * The heartbeat is a consistant beat used by the rendering engine to keep everything 
	 * synchronized. This method is a courtesy method designed to replace the 
	 * function of the Timer. 
	 * </p>
	 * 
	 * <p>
	 * Heartbeat will get called at the step-rate of the engine, which is about 24 times per
	 * second. This method can be freely overriden without any consequence.
	 * </p>
	 */
	public void heartbeat() {
		
	}
	
	/**
	 * <p>
	 * Centres the camera on the location of a given instance.
	 * </p>
	 * 
	 * <p>
	 * Call this in a heartbeat to continually track the instance.
	 * </p>
	 * 
	 * @param i
	 */
	public void moveCameraTo(Instance i) {
		camera.setLocation(i.getLocation().scale(-1));
	}
	
	/**
	 * <p>
	 * Attempts to generate a mask for a given picture file.
	 * </p>
	 * 
	 * <p>
	 * If the mask already exists then the action will depend on what WriteFlag is set.
	 * </p>
	 * 
	 * @see #setWriteFlag(WriteFlag)
	 * 
	 * @param file
	 */
	public static void generateMaskFor(File file) {
		try {
			String name = file.getName();
			int index = name.indexOf('.');
			String ext = name.substring(index+1, name.length());
			String writePath = file.getPath().substring(0, file.getPath().length()-4) + "_." + ext;
			File write = new File(writePath);
			if (writeFlag != WriteFlag.OVERWRITE && write.exists()) {
				if (writeFlag == WriteFlag.WARNING || writeFlag == WriteFlag.DEBUG)
					System.out.println(file.getName() + " mask already exists, nothing was generated.");
				return;
			}
			
			BufferedImage image = ImageIO.read(file);
			
			// Assume top left is background
			int bg = image.getRGB(0, 0);
			int bgR = bg >> 16 & 0xFF;
			int bgG = bg >> 8 & 0xFF;
			int bgB = bg & 0xFF;
			int w = image.getWidth();
			int h = image.getHeight();
			
			BufferedImage mask = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					int rgb = image.getRGB(x, y);
					int r = rgb >> 16 & 0xFF;
					int g = rgb >> 8 & 0xFF;
					int b = rgb & 0xFF;
					int a = rgb >> 24 & 0xFF;
					int dif = Math.abs(r - bgR) + Math.abs(g - bgG) + Math.abs(b - bgB);
					if (dif < 60 || a < 255) {
						mask.setRGB(x, y, Color.BLACK.getRGB());
					} else {
						mask.setRGB(x, y, Color.white.getRGB());
					}
				}
			}
			
			ImageIO.write(mask, ext, write);
			if (writeFlag == WriteFlag.DEBUG)
				System.out.println(write.getName() + " was written");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>
	 * Sets the action to occur when a mask exists and another was created.
	 * </p>
	 * 
	 * <p>
	 * Possible values are:
	 * <ul>
	 * <li>{@link WriteFlag#OVERWRITE OVERWRITE}
	 * <li>{@link WriteFlag#SILENCE SILENCE}
	 * <li>{@link WriteFlag#DEBUG DEBUG}
	 * <li>{@link WriteFlag#WARNING WARNING}
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * OVERWRITE flag will automatically write over the mask with the newly generated mask
	 * </p>
	 * 
	 * <p>
	 * SILENCE flag will not overwrite anything, and it wont say anything about the conflicts.
	 * </p>
	 * 
	 * <p>
	 * DEBUG flag will not overwrite and will inform you of all conflicts and all newly
	 * created masks
	 * </p>
	 * 
	 * <p>
	 * WARNING flag will not overwrite and will only inform you of conflicts
	 * </p>
	 * 
	 * @param flag
	 */
	public void setWriteFlag(WriteFlag flag) {
		writeFlag = flag;
	}
	
	/**
	 * Generates a mask for all applicable images inside the given folder.
	 * 
	 * @see #generateMaskFor(File)
	 */
	public void generateMasksForAll(File folder) {
		if (!folder.isDirectory()) {
			return;
		}
		File[] files = folder.listFiles();
		
		for (int i = files.length-1; i >= 0; i--) {
			File file = files[i];
			String name = file.getName();
			int index = name.lastIndexOf('.');
			String title = name.substring(0, index);
			if (title.length() > 0 && title.charAt(title.length()-1) != '_') {
				generateMaskFor(file);
			}
		}
	}
	
	/**
	 * Moves the camera forward by a given value.
	 */
	public void moveCameraForward(float val) {
		Vec3 rot = camera.getRotation();
		float x = -sin(rot.y);
		float y = sin(rot.x);
		float z = cos(rot.x) * cos(rot.y);
		Vec3 speed = new Vec3(x, y, z);
		speed.m_normalize();
		speed.m_scale(val);
		camera.addPosition(speed);
	}
	
	/**
	 * Moves the camera backwards by a given value.
	 */
	public void moveCameraBackward(float val) {
		Vec3 rot = camera.getRotation();
		float x = -sin(rot.y);
		float y = sin(rot.x);
		float z = cos(rot.x) * cos(rot.y);
		Vec3 speed = new Vec3(x, y, z);
		speed.m_normalize();
		speed.m_scale(-val);
		camera.addPosition(speed);
	}
	
	/**
	 * Strafes the camera left by a given value
	 */
	public void moveCameraLeft(float val) {
		float radY = camera.rotY();
		float addX = cos(radY) * val;
		float addZ = sin(radY) * val;
		camera.addPosition(addX, 0, addZ);
	}
	
	/**
	 * Strafes the camera right by a given value
	 */
	public void moveCameraRight(float val) {
		float radY = (camera.rotY() / 180.0f * PI);
		float addX = -cos(radY) * val;
		float addZ = -sin(radY) * val;
		camera.addPosition(addX, 0, addZ);
	}
	
	/**
	 * Moves the camera directly upwards by a given value, direction independent
	 */
	public void moveCameraUp(float val) {
		camera.addPosition(0, -val, 0);
	}
	
	/**
	 * Moves the camera directly downwards by a given value, direction independent
	 */
	public void moveCameraDown(float val) {
		camera.addPosition(0, val, 0);
	}
	
	/**
	 * @see #make(Model)
	 */
	public Instance make(Constructable c) {
		return make(c.getModelConstructor().getModel());
	}
	
	/**
	 * @see #moveCameraForward(float)
	 */
	public void moveCameraForward() {
		moveCameraForward(PLAYER_SPEED);
	}
	
	/**
	 * @see #moveCameraBackward(float)
	 */
	public void moveCameraBackward() {
		moveCameraBackward(PLAYER_SPEED);
	}
	
	/**
	 * @see #moveCameraLeft(float)
	 */
	public void moveCameraLeft() {
		moveCameraLeft(PLAYER_SPEED);
	}
	
	/**
	 * @see #moveCameraRight(float)
	 */
	public void moveCameraRight() {
		moveCameraRight(PLAYER_SPEED);
	}
	
	/**
	 * @see #moveCameraUp(float)
	 */
	public void moveCameraUp() {
		moveCameraUp(PLAYER_SPEED);
	}
	
	/**
	 * @see #moveCameraDown(float)
	 */
	public void moveCameraDown() {
		moveCameraDown(PLAYER_SPEED);
	}
	
	/**
	 * @see #make(Model)
	 */
	public Instance make(Model m) {
		Instance i = new Instance(m);
		add(i);
		return i;
	}
	
	/**
	 * Returns true if given key is currently pressed
	 */
	public static boolean keyIsDown(int key) {
		return Render.keys[key];
	}
	
	/**
	 * Called by the engine about 24 times a second
	 */
	public final void step() {
		heartbeat();
		
		if (defaultBindings) {
			if (gameMode == GAMEMODE_3D) {
				if (keyIsDown(UP) || keyIsDown(W)) {
					moveCameraForward();
				}
				if (keyIsDown(LEFT) || keyIsDown(A)) {
					moveCameraLeft();
				}
				if (keyIsDown(DOWN) || keyIsDown(S)) {
					moveCameraBackward();
				}
				if (keyIsDown(RIGHT) || keyIsDown(D)) {
					moveCameraRight();
				}
				if (keyIsDown(SHIFT)) 
					moveCameraDown();
				if (keyIsDown(SPACE))
					moveCameraUp();
			}
			if (gameMode == GAMEMODE_2D) {
				if (keyIsDown(UP) || keyIsDown(W)) {
					camera.addPosition(0, PLAYER_SPEED, 0);
				}
				if (keyIsDown(LEFT) || keyIsDown(A)) {
					camera.addPosition(PLAYER_SPEED, 0, 0);
				}
				if (keyIsDown(DOWN) || keyIsDown(S)) {
					camera.addPosition(0, -PLAYER_SPEED, 0);
				}
				if (keyIsDown(RIGHT) || keyIsDown(D)) {
					camera.addPosition(-PLAYER_SPEED, 0, 0);
				}
			}
		}
	}
	
	/**
	 * Adds the default key-bindings for the current game mode.
	 * 
	 * <p>
	 * 3D will add forward, backward, strafe left and right, float up and
	 * float down.
	 * </p>
	 * 
	 * <p>
	 * 2D will add up, down, left and right
	 * </p>
	 */
	public void addDefaultKeyBindings() {
		defaultBindings = true;
	}
	
	/**
	 * Reads an image from a given filename and returns it
	 */
	public static BufferedImage getImage(String filename) {
		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Signals the engine to display the FPS.
	 */
	public static void showFPS() {
		FLAGS[FLAG_FPS] = true;
	}
	
	/**
	 * Signals the engine to stop displaying the FPS
	 */
	public static void hideFPS() {
		FLAGS[FLAG_FPS] = false;
	}
	
	/**
	 * Signals the engine to start displaying information
	 * about the current memory state.
	 */
	public static void showMemory() {
		FLAGS[FLAG_MEMORY] = true;
	}
	
	/**
	 * Signals the engine to stop displaying information
	 * about the current memory state.
	 */
	public static void hideMemory() {
		FLAGS[FLAG_MEMORY] = false;
	}
	
	public void setCameraLocation(float x, float y, float z) {
		camera.setLocation(x, y, z);
	}
	
	public void setCameraLocation(Vec3 v) {
		camera.setLocation(v);
	}
	
	public void setCameraRotation(float x, float y, float z) {
		camera.setRotation(x, y, z);
	}
	
	public void setCameraRotation(Vec3 v) {
		camera.setRotation(v);
	}
	
	public void systemEventRecieved(SystemEvent e) {
		if (e.getClass() == MemoryLowEvent.class) lowMemory((MemoryLowEvent)e);
	}
	
	/**
	 * @param e Not yet implemented 
	 */
	public void lowMemory(MemoryLowEvent e) {
		
	}
	
	/**
	 * Removes an instance from the engine to be displayed or collided with
	 * @param i
	 */
	public void remove(Instance i) {
		remove(i);
	}
	
	public void switchToFullScreen() {
		Driver.switchToFullScreen();
	}
	
	public Vec3 toDirection(Vec3 rotation) {
		if (gameMode == GAMEMODE_2D) {
			float theta = rotation.z+90;
			float x = cos(theta);
			float y = sin(theta);
			return new Vec3(x, y, 0);
		}
		// TODO 3D direction support
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public void addMouseListener(MouseListener listener) {
		Render.addMouseListener(listener);
	}
	
	public void addMouseMotionListener(MouseMotionListener listener) {
		Render.addMouseMotionListener(listener);
	}
	
	public static Vec2 getMousePosition() {
		return Render.getMousePosition();
	}
	
	public static void setMousePosition(Vec2 v) {
		robot.mouseMove((int)v.x, (int)v.y);
	}
	
	public static void setMousePosition(int x, int y) {
		robot.mouseMove(x, y);
	}
	
	/**
	 * @param drawable Draw event to be implemented maybe 
	 */
	public void draw(GLAutoDrawable drawable) {
		
	}
	
	/**
	 * Disables the mouse-look control in 3D Mode. Default is enabled.
	 */
	public void disableMouse() {
		MOUSE_ENABLED = false;
	}
	
	/**
	 * Re-enables the mouse-look control in 3D Mode. Default is enabled.
	 */
	public void enableMouse() {
		MOUSE_ENABLED = true;
	}
	
	/**
	 * <p>
	 * Pauses the rendering engine.
	 * </p>
	 * 
	 * <p>
	 * Note that this does not pause Game.draw and Game.hearbeat, both can be 
	 * overriden for "pause graphics" such as menus or the pause screen.
	 * </p>
	 * 
	 * <p>
	 * To unpause, the unpause() or resume() commands can be used.
	 * </p>
	 */
	public static void pause() {
		paused = true;
	}
	
	public static void resume() {
		paused = false;
	}
	
	public static void unpause() {
		paused = false;
	}
	
	/**
	 * Displays the mouse by setting the mouse picture to the default mouse picture
	 */
	public static void showMouse() {
		Render.showMouse();
	}
	
	/**
	 * Hides the mouse by setting the mouse picture to a transparent rectangle.
	 */
	public static void hideMouse() {
		Render.hideMouse();
	}
	

	public static void add(VirtualLight light) {
		lightManager.add(light);
	}
	
	public Vec2 getMouseCoords(){
		return new Vec2(lastMouseCoords);
	}
	
	//These coords can be used for fancy things in the game class depending on the mouse position
	//YAY
	public void setMouseCoords(MouseEvent e){
		lastMouseCoords.set(e.getX(), e.getY());
	}
	
	public void addInstanceToUI(Instance i) {  //TODO remove
		Render.addInstanceToUI(i);
	}
	
	
	public void addToUI(UIComponent i) {  
		Render.addToUI(i);
	}
	public void removeFromUI(UIComponent i) {  
		Render.removeFromUI(i);
	}
	

	public static Vec2 getMousePositionNormalized() {
		return Render.getMousePositionNormalized();
	}
	
	public void makeLater(String modelFileName) {
		ModelManager.loadLater(modelFileName);
	}
	
	public void setFullscreen(boolean b) {
		isFullscreen = b;
	}
	
	public boolean isFullscreen() {
		return isFullscreen;
	}
	
	public void mousePressed(float x, float y) {
		
	}
	
	public void mouseReleased(float x, float y) {
		
	}
	
	public void keyPressed(int keyCode, char keyChar) {

	}
	
	public void keyReleased(int keyCode, char keyChar) {

	}
}
