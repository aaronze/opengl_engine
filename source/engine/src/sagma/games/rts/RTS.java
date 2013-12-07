package sagma.games.rts;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.data.index.MaterialIndex;
import sagma.core.data.manager.ModelManager;
import sagma.core.data.model.Pickable;
import sagma.core.event.CollisionAction;
import sagma.core.event.PickedObjectAction;
import sagma.core.event.PickedObjectEvent;
import sagma.core.event.PickedObjectEventAction;
import sagma.core.event.WildcardEvent;
import sagma.core.input.KeyBind;
import sagma.core.io.FileGetter;
import sagma.core.io.RadialMenu;
import sagma.core.io.RadialMenuItem;
import sagma.core.light.*;
import sagma.core.material.Composition;
import sagma.core.material.Material;
import sagma.core.material.Texture;

import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Instance;
import sagma.core.model.Model;
import sagma.core.network.FlowController;
import sagma.core.network.connection.ServerConnection;
import sagma.core.profile.Profiler;
import sagma.core.render.FastText;
import sagma.core.render.Game;
import sagma.core.render.Render;
import sagma.games.rts.client.Chat;
import sagma.games.rts.client.Client;
import sagma.games.rts.client.Player;
import sagma.games.rts.entity.cellestial.Celestial;
import sagma.games.rts.entity.cellestial.Planet;
import sagma.games.rts.entity.cellestial.Star;
import sagma.games.rts.entity.cellestial.Sun;
import sagma.games.rts.entity.projectile.Projectile;
import sagma.games.rts.entity.ship.Ship;

import sagma.core.sound2d.Mixer;
import sagma.core.sound2d.SoundClip;
import sagma.core.sound3d.Mixer3D;
import sagma.core.ui.Button;
import sagma.core.ui.UIContainer;

import sagma.core.render.Text;


import static sagma.core.math.Math.*;

public class RTS extends Game {
	public static String resources = FileGetter.root + "games/rts/resources/";
	public static Hashtable<String, SoundClip> soundCollection;
	public static FastText textRender = new FastText();
	public static Client client = new Client();
	public static Chat chat = new Chat();  //TODO change this to a UIComponent
	boolean isChatting = false;
	String currentChat = "";
	private Text txtChat;
	
	public Mixer3D sounds;
	public static Mixer soundSystem;

	public static ArrayList<Player> playerList = new ArrayList<Player>();

	public static float zoom = 4.0f;
	public static final float MIN_ZOOM = 1.3f;
	public static final float MAX_ZOOM = 25f;
	
	public static float MAP_WIDTH = 100;
	public static float MAP_HEIGHT = 100;

	MaterialIndex starBucket;	
	Instance selectedObject;
	private Vec3 playerDirection = new Vec3(0,0,0);
	public static Material earthTexture;
	public static Material moonTexture;
	private Model sphereTemplate;

	public static ServerConnection connect;
	FlowController flow;
	
	RadialMenu planetMenu;
	public Mixer sound2D;
	
	private Text txtOre;
	private Text txtFuel;
	private Text txtCrystals;
	private Text txtFPS;
	
	private Model robPlanet;
	
	//selectionevents
	//all normalised to screen scale
	private Vec2 mouseDownPosition;
	private Vec2 mouseUpPosition;
	private Vec2 mouseCurrentPosition;
	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		
		Profiler.start("Loading time...");
		
		addInstanceToUI(chat);
		chat.addLine("Hello!!");
		chat.addLine("Feeling lonely? Press [t] to chat!");
		
		//switchToFullScreen();
		setEscapeKeyToExit();
		setGameMode(GAMEMODE_3D);
		disableMouse();
		showMouse();
		
		soundSystem = new Mixer();
		soundSystem.start();
		earthTexture = Material.getMaterial("Texture/earth-living.jpg");
		moonTexture = Material.getMaterial("Texture/moon.jpg");
				
		initGUI();
		loadModels(drawable.getGL().getGL2());
		loadSounds();
		initLights();
		initConnection(); // MUST be placed after model loading.

		RandomNumberGenerator randX = new RandomNumberGenerator(-20.0f, -10.0f); //RandomNumberGenerator(float max, int seed) 
		RandomNumberGenerator randY = new RandomNumberGenerator(-20.0f, -10.0f); //is too close to RandomNumberGenerator(float max, float min)
		
		Material jeffTerran = Material.getMaterial("Texture/jeffTerranTex.jpg");
		Material desert = Material.getMaterial("Texture/Desert.jpg");
		Material ice = Material.getMaterial("Texture/Ice.jpg");
		Material volcanic = Material.getMaterial("Texture/Volcanic.jpg");
		
		Button b1 = new Button();
		Button b2 = new Button();
		Button b3 = new Button();
		Button b4 = new Button();
		Button b5 = new Button();
		Button b6 = new Button();
		Button b7 = new Button();
		Button b8 = new Button();
		Button b9 = new Button();
		Button b10 = new Button();
		UIContainer u = new UIContainer(0, 0, 1, 1);
		u.add(b1); u.add(b2); u.add(b3); u.add(b4); u.add(b5);  u.add(b6); u.add(b7); u.add(b8);  u.add(b9); u.add(b10);
		//addToUI(u);
		
		//addToUI(new Button());
		
		for(int i=0; i<5; i++){	
			for(int j=0; j<5; j++){
				switch (i%5) {
				case 0:
					sphereTemplate.setMaterial(jeffTerran);
					break;
				case 1:
					sphereTemplate.setMaterial(desert);
					break;
				case 2:
					sphereTemplate.setMaterial(ice);
					break;
				case 3:
					sphereTemplate.setMaterial(volcanic);
					break;
				default:
					sphereTemplate.setMaterial(earthTexture);
					break;
				}
				
				Planet planet = new Planet(sphereTemplate.getClone());
				planet.setPosition(randX.nextNumber(), randY.nextNumber(), 1f);
				planet.addRotationalSpeed((float)Math.random()*0.1f, (float)Math.random()*0.1f, (float)Math.random()*0.1f);
				add(planet);
				
				randX.setMax(randX.getMax()+10);
				randX.setMin(randX.getMin()+10);
			}
			randY.setMin(randY.getMin()+10);
			randY.setMax(randY.getMax()+10);
			randX.setMax(10);
			randX.setMin(0);
		}
		
		sphereTemplate.setMaterial(Material.getMaterial(FileGetter.root + "games/rts/resources/sun.jpg"));
		Planet sun = new Planet(sphereTemplate.getClone());
		sun.setScale(2.0f);
		sun.setPosition(0, 0, 1f);
		sun.addAsteroids(6); 
		add(sun);
		
		playerList.add(new Player("Anonymous", sounds, this));
		playerList.add(new Player("Enemy", sounds, this));
		playerList.add(new Player("Grunt", sounds, this));
		playerList.add(new Player("Sartek", sounds, this));
		playerList.add(new Player("The Emporer", sounds, this));
		playerList.add(new Player("Cheesh", sounds, this));
		
		playerList.get(0).ship.addLocation(-4, -4, 0);
		playerList.get(1).ship.addLocation(4, 4, 0);
		playerList.get(1).ship.addRotation(0, 0, 270);		
		/*playerList.get(2).ship.addLocation(100, 70, 0);
		playerList.get(2).ship.addRotation(0, 0, 150);
		playerList.get(3).ship.addLocation(-80, 40, 0);
		playerList.get(3).ship.addRotation(0, 0, 70);
		playerList.get(4).ship.addLocation(-22, -10, 0);
		playerList.get(4).ship.addRotation(0, 0, 270);
		playerList.get(5).ship.addLocation(4, 4, 0);
		playerList.get(5).ship.addRotation(0, 0, 190);*/
		
		add(playerList.get(0).ship);
		//add(playerList.get(1).ship);
		/*add(playerList.get(2).ship);
		add(playerList.get(3).ship);
		add(playerList.get(4).ship);
		add(playerList.get(5).ship);*/
		
		
		
		/*CapitalShip ship = (CapitalShip)playerList.get(1).ship;
		InstanceController ic = new InstanceController(ship);
		ic.addPrey(CapitalShip.class);
		ic.addPrey(Planet.class);
		ic.addPredator(Bullet.class);
		ic.setTargetType(InstanceController.TARGET_SINGLE);
		ic.setPreyRange(20);
		ic.setPredatorRange(5);*/
		
		playerList.get(0).addMouseAdapter();
		playerList.get(0).ship.networkName = System.getProperty("user.name");

		initStars();

		add(new CollisionAction(Projectile.class, Planet.class, new Projectile.collidesWithPlanet()));
		add(new CollisionAction(Projectile.class, Sun.class, new Projectile.collidesWithPlanet()));
		add(new CollisionAction(Projectile.class, Ship.class, new Projectile.collidesWithPlanet()));
		
		add(new BPressed());
		add(new PPressed());

		add(new PickedObjectAction(Planet.class, new PlanetMine()));
		add(new PickedObjectAction(Sun.class, new PlanetMine()));

		Render.POINTER_ACCURACY = 0.2f; // Can be more lenient because space is so vast
		
		txtOre = new Text("Ore: "+playerList.get(0).getOre(), 200, 20);
		txtFuel = new Text("Fuel: "+playerList.get(0).getFuel(), 350, 20);
		txtCrystals = new Text("Crystals: "+playerList.get(0).getCrystal(), 500, 20);
		txtFPS = new Text("FPS: " + Render.frameRate, 50, Render.HEIGHT-50);
		txtChat = new Text("", 10, 30);
		textRender.add(txtOre);
		textRender.add(txtFuel);
		textRender.add(txtCrystals);
		textRender.add(txtFPS);
		textRender.add(txtChat);
		
		FadeOut f1 = new FadeOut(playerList.get(0), FadeOut.LEFT);
		FadeOut f2 = new FadeOut(playerList.get(0), FadeOut.TOP);
		FadeOut f3 = new FadeOut(playerList.get(0), FadeOut.RIGHT);
		FadeOut f4 = new FadeOut(playerList.get(0), FadeOut.BOTTOM);
		Render.add(f1);
		Render.add(f2);
		Render.add(f3);
		Render.add(f4);
		
		soundSystem.playLoop(new SoundClip(FileGetter.root + "games/rts/resources/SpaceLoop2.wav"));
		addInstanceToUI(textRender);
		
		Planet planet = new Planet(robPlanet);
		planet.setScale(1.5f);
		planet.setLocation(3.0f, 3.0f, 1.0f);
		planet.addRotationalSpeed(0.02f, 0.2f, 0);
		add(planet);

		Render.MOTION_BLUR = 0.1f;
		
		add(new KeyBind() {

			@Override
			public boolean isKey(int key) {
				return key == KeyEvent.VK_ENTER;
			}

			@Override
			public void keyPressed() {
				if (currentChat != null && currentChat.length() > 0 && currentChat.charAt(0)=='/') {
					client.command(currentChat.substring(1, currentChat.length()));
					currentChat = "";
					isChatting = false;
					return;
				}
				chat.addLine(currentChat);
				client.chat(currentChat);
				currentChat = "";
				isChatting = false;
			}

			@Override
			public void keyReleased() {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		Profiler.stop("Loading time...");
	}

	private static void initLights() {
		Render.LIGHTING_ENABLED = false;
		add(new VirtualLight() {
			{
				setEnabled(true);
				setDiffuseColor(Color4f.WHITE);
				setSpecularColor(Color4f.WHITE);
				setAmbientColor(Color4f.BLACK);
				setPosition(new float[]{0,0,5,1});
				setBrightnessInner(128);
				setQuadraticAttenuation(0.02f);
			}
		});

		add(new VirtualLight() {
			{
				setEnabled(true);
				setDiffuseColor(Color4f.RED);
				setSpecularColor(Color4f.RED);
				setAmbientColor(Color4f.WHITE);
				setPosition(new float[]{0,0,5,1});
			}
		});
	}

	@Override
	public void heartbeat() {
		Render.MOTION_BLUR = 0.1f;
		float MAX_BLUR = 0.1f;
		for(Player p: playerList){
			p.action();
		}

		Ship player = playerList.get(0).ship;

		Vec3 rotation = player.getRotation();
		float theta = rotation.z+90;
		float x = cos(theta);
		float y = sin(theta);
		playerDirection.set(x, y, 0);

		if (!isChatting) {
			if (keyIsDown(A)) {
				player.addRotationalSpeed(0, 0, Ship.DEFAULT_ROTATION_THRUST);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(D)) {
				player.addRotationalSpeed(0, 0, -Ship.DEFAULT_ROTATION_THRUST);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(W)) {
				float thrust = player.forwardThrust;
				if (keyIsDown(SHIFT)){
					if(player.getSpeed().length() + player.boostThrust > Ship.DEFAULT_TURBO_MAX_SPEED){
						thrust = Ship.DEFAULT_TURBO_BOOST;
					}
					else{
						thrust += player.boostThrust;
					}
				}
				player.addSpeed(x*thrust, y*thrust, 0);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(S)) {
				float thrust = player.backwardThrust;
				if (keyIsDown(SHIFT)){
					if(player.getSpeed().length() + player.boostThrust > Ship.DEFAULT_TURBO_MAX_SPEED){
						thrust = Ship.DEFAULT_TURBO_BOOST;
					}
					else{
						thrust += player.boostThrust;
					}
				}
				player.addSpeed(x*-thrust, y*-thrust, 0);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(Q)){
				float thrust = player.sidewardThrust;
				if (keyIsDown(SHIFT)) thrust /=2;
				player.addSpeed(thrust*-y, thrust*x, 0);
				Render.MOTION_BLUR = MAX_BLUR;
	
			}
			if(keyIsDown(E)){
				float thrust = player.sidewardThrust;
				if (keyIsDown(SHIFT)) thrust /=2;
				player.addSpeed(thrust*y, thrust*-x, 0);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(SPACE)) {
				player.shoot();
			}
			if (keyIsDown(Z)) {
				zoom(1);
				Render.MOTION_BLUR = MAX_BLUR;
			}
			if (keyIsDown(X)) {
				zoom(-1);
				Render.MOTION_BLUR = MAX_BLUR;
			}
		}
			
		Render.camera.setLocation(player.getLocation());
		Render.camera.getLocation().m_negate();
		Render.camera.addLocation(0, 0, -zoom);
		Render.camera.setRotation(player.getRotation());
		Render.camera.getRotation().m_negate();


		txtOre.setText("Ore: " + playerList.get(0).getOre());
		txtFuel.setText("Fuel: " + playerList.get(0).getFuel());
		txtCrystals.setText("Crystal: " + playerList.get(0).getCrystal());
		txtFPS.setText("FPS: " + Render.frameRate);
		txtChat.setText(currentChat);
		
		Vec3 futurePosition = player.getPosition().add(player.getSpeed());
		if(abs(futurePosition.x) > MAP_WIDTH || abs(futurePosition.y) > MAP_HEIGHT) {
			player.setSpeed(0,0,0);
		}
		
		client.update(player);
	}

	/**
	 * So that other classes can affect zoom safely
	 */
	public static void zoom(float amount){
		if (zoom + amount < MIN_ZOOM){
			zoom = MIN_ZOOM;
		}
		else if (zoom + amount > MAX_ZOOM){
			zoom = MAX_ZOOM;
		}
		else{
			zoom += amount;
		}
	}
	
	
	class PPressed extends KeyBind {
		@Override
		public boolean isKey(int key) {
			return key == P && !isChatting;
		}

		@Override
		public void keyPressed() {
			starBucket.setVisible(!starBucket.isVisible());
			if (starBucket.isVisible()) System.out.println("Stars Enabled");
			else System.out.println("Stars Disabled");
		}

		@Override
		public void keyReleased() {
		}
	}

	class BPressed extends KeyBind {
		@Override
		public boolean isKey(int key) {
			return key == B && !isChatting;
		}

		@Override
		public void keyPressed() {
			Render.BLOOM_ENABLED = !Render.BLOOM_ENABLED;
			if (Render.BLOOM_ENABLED) System.out.println("Bloom Enabled");
			else System.out.println("Bloom Disabled");
			Render.add(planetMenu);
		}

		@Override
		public void keyReleased() {
		}

	}

	
	private void loadModels(GL2 gl) {
		sphereTemplate = new DirectedFieldPlanetoidGenerator("sphere", earthTexture).getModelConstructor().getModel();

		robPlanet = makeModel("Models/PlanetsBase.obj");
		Composition c = new Composition(gl);
		try {
			c.setDiffuse(new Texture("Texture/terran1/Terran1Diffuse.jpg"));
			c.setBump(new Texture("Texture/terran1/Terran1Bump.jpg"));
			c.setEmissive(new Texture("Texture/terran1/Terran1Emissive.jpg"));
			c.setNormal(new Texture("Texture/terran1/Terran1Normal.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		robPlanet.setMaterial(c);
		
		Model bullet = sphereTemplate.getClone();

		bullet.setMaterial(Material.getMaterial("Shaders/Biome/lava"));
		ModelManager.add("bullet", bullet);
		
		Model explosion = sphereTemplate.getClone();
		explosion.setMaterial(Material.getMaterial(FileGetter.root + "games/rts/resources/semiTransparentBlue.png"));
		ModelManager.add("exPLOUD", explosion);

		ModelManager.add("Star", makeModel(FileGetter.root + "games/rts/resources/star.png"));
		ModelManager.add("gun", makeModel(FileGetter.root + "games/rts/resources/gun.png"));
		ModelManager.add("fadeOut", makeModel(FileGetter.root + "games/rts/resources/fadeOut.png"));
		
		
		try {
			DirectedFieldPlanetoidGenerator.BUMP = 0.002f;
			DirectedFieldPlanetoidGenerator gen = new DirectedFieldPlanetoidGenerator(
					"craters", new ImageFieldGenerator(ImageIO.read(new File(resources + "asteroidMap_B.jpg"))), 
					Material.getMaterial(resources + "asteroidMap.jpg"));
			ModelManager.add("craters", gen.getModelConstructor().getModel());
			DirectedFieldPlanetoidGenerator.BUMP = DirectedFieldPlanetoidGenerator.DEFAULT_BUMP;
		} catch (Exception e) {
			System.out.println(e);
		}	

		Model m = makeModel("Models/mothership.obj");
		ModelManager.add("capitalShip", m);
	}
	
	private static void loadSounds() {
		soundCollection = new Hashtable<String, SoundClip>();

		soundCollection.put("shoot", new SoundClip(FileGetter.root + "games/rts/resources/gun.wav"));
		soundCollection.put("bigLaser", new SoundClip(FileGetter.root + "games/rts/resources/BigLaser.wav"));
		soundCollection.put("smallLaser", new SoundClip(FileGetter.root + "games/rts/resources/SmallLaser.wav"));
		soundCollection.put("upgrade", new SoundClip(FileGetter.root + "games/rts/resources/engineup.wav"));
		soundCollection.put("explode", new SoundClip(FileGetter.root + "games/rts/resources/Explode.wav"));
		soundCollection.put("implode", new SoundClip(FileGetter.root + "games/rts/resources/planetexplode.wav"));
		soundCollection.put("bonus", new SoundClip(FileGetter.root + "games/rts/resources/pickup_blownbottle.wav"));
		soundCollection.put("soundtrack", new SoundClip(FileGetter.root + "games/rts/resources/SpaceLoop.wav"));
	}

	public static Player getPlayer(int id){
		return playerList.get(id);
	}


	public class PlanetMine extends PickedObjectEventAction{
		@Override
		public void eventRecieved(Pickable p, PickedObjectEvent e) {
			if (p == null) return; // Why oh why is p null sometimes???
			Instance i = (Instance)p;
			if((e.getButton() == MouseEvent.BUTTON3 || keyIsDown(CONTROL)) && i.isPickable(MouseEvent.BUTTON3)){
				selectedObject = i;
				planetMenu.setPosition(i.getPosition().x, i.getPosition().y, 5);
				planetMenu.setParent(i);
				add(planetMenu);
			}
		}
	}
	
	private void initGUI() {
		planetMenu = new RadialMenu();
		
		RadialMenuItem addMiningStation = new RadialMenuItem(makeModel("Texture/Volcanic.jpg"));
		addMiningStation.setTriggeredEvent(new WildcardEvent() {
			@Override
			public void eventRecieved() {
				((Celestial)selectedObject).addMiningStation(playerList.get(0));
			}
		});
		planetMenu.add(addMiningStation);
		
		Model model = makeModel("Texture/Forrest.jpg");
		planetMenu.add(model);
		planetMenu.add(model);
		
		RadialMenu subMenu = new RadialMenu();
		subMenu.add(addMiningStation);
		subMenu.add(addMiningStation);
		subMenu.add(addMiningStation);
		subMenu.add(addMiningStation);
		
		planetMenu.add(subMenu);
		planetMenu.add(model);
		planetMenu.add(model);
	}
	
	private void initStars() {
		Star star = new Star();
		starBucket = new MaterialIndex(star.model().getBuffer().getMesh(0));
		
		for (int i = 0; i < 100; i++) {
			float bigX = (float)Math.random() * 2.0f * MAX_ZOOM - MAX_ZOOM;
			float bigY = (float)Math.random() * 2.0f * MAX_ZOOM - MAX_ZOOM;
			float depth = (float)(Math.random() * 20.0f - 10.0f);
			for (int k = 0; k < 100; k++) {
				float smallX = (float)Math.random() * 0.5f * MAX_ZOOM - MAX_ZOOM / 4.0f;
				float smallY = (float)Math.random() * 0.5f * MAX_ZOOM - MAX_ZOOM / 4.0f;
				Star s = new Star();
				s.setLocation(bigX+smallX, bigY+smallY, -20.0f + depth);
				starBucket.add(s);
			}
		}
		add(starBucket);
	}
	
	private static void initConnection() {
		try {
			connect = new ServerConnection(client, "aaronze.dyndns.org");
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void exit() {
		try {
			connect.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	
	 @Override
	public void mousePressed(float x, float y) {
		mouseDownPosition = Render.getMousePositionNormalized();
		super.mousePressed(x, y);
	}
	 @Override
	public void mouseReleased(float x, float y) {
		mouseUpPosition = Render.getMousePositionNormalized();
		super.mouseReleased(x, y);
	}
	 

	@Override
	public void keyPressed(int keyCode, char keyChar) {
		if (keyChar == '/') {
			isChatting = true;
		}
		
		if (keyChar == 't') {
			if (isChatting == false) {
				isChatting = true;
				return;
			}
		}
		
		if (!isChatting) return;
		
		if (keyCode == KeyEvent.VK_BACK_SPACE) {
			if (currentChat.length() > 0) currentChat = currentChat.substring(0, currentChat.length()-1);
		}
		
		if (keyCode > 20 && keyCode < 150) {
			currentChat += keyChar;
		}
	}
}
