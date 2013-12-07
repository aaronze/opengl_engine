package sagma.core.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.Timer;

import sagma.core.data.GameInfo;
import sagma.core.io.FileGetter;

public class GameLoader extends JFrame implements MouseListener, MouseMotionListener, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8441484024042023387L;
	public static int buttonWidth = 160;
	public static int buttonHeight = 50;
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	public static int OFFSET_Y = 80;
	public static int OFFSET_X = 60;
	private GameInfo chosenIndex;
	private GameInfo hoveringIndex;
	private ArrayList<String> changelog;
	private int logWindow = 30;
	private int logIndex = 0;
	private Timer timer;
	private ArrayList<GameInfo> gameList;
	private String version;
	
	private BufferedImage buffer;
	private Graphics bufferG;
	
	public GameLoader() {
		
		changelog = Render.readFile(new File("changelog.txt"));
		
		Iterator<String> lines = changelog.iterator();
		while (lines.hasNext()) {
			String line = lines.next();
			if (line.length() > 1 && line.charAt(0) == 'V' && line.charAt(1) == ' ') {
				version = line.substring(2);
				break;
			}
		}
		
		gameList = populateGameList();
		
		setVisible(true);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setTitle("Game Chooser");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
				
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
		
		timer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		timer.start();
	}
	
	public Game chooseGame() {
		if (chosenIndex == null) return null;
		timer.stop();
		setVisible(false);
		Game loaded = chosenIndex.getLoadedGame();
		if (loaded == null) return getGame(chosenIndex);
		return loaded;
	}
	
	public static Game getGame(GameInfo file) {
		if (!file.isValid()) return null;
		String packageName = file.getPath();
		String className = file.getName();
		Game game = null;
		
		// Load the class at className
		ClassLoader classLoader = Game.class.getClassLoader();
	    try {
	        String path = packageName.replace('/', '.').replace('\\', '.').replaceAll("src.", "") + "." + className;
	        @SuppressWarnings("unchecked")
			Class<? extends Game> aClass = (Class<? extends Game>)classLoader.loadClass(path);
	        game = aClass.newInstance();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return game;
	}
	
	private static ArrayList<GameInfo> populateGameList() {
		ArrayList<GameInfo> games = new ArrayList<GameInfo>();
		
		File gameFolder = FileGetter.getFile("games");
		if (!gameFolder.exists()) throw new RuntimeException("Game Folder does not exist!");
		
		File[] gameList = gameFolder.listFiles();
		// Check each game is valid.
		for (int i = 0; i < gameList.length; i++) {
			File game = gameList[i];
			
			// File must exist and must be a folder
			if (game.exists() && game.isDirectory()) {
				GameInfo info = new GameInfo(game.getPath());
				if (info != null) {
					games.add(info);
				}
			}
		}
		
		return games;
	}
	
	@Override
	public void paint(Graphics gr) {
		if (buffer == null) {
			buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			bufferG = buffer.getGraphics();
		}
		Graphics g = bufferG;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		int posX = OFFSET_X;
		int posY = OFFSET_Y;
		for (int i = 0; i < gameList.size(); i++) {
			GameInfo info = gameList.get(i);
			if (info != hoveringIndex)
				info.drawOn(g, posX, posY, buttonWidth, buttonHeight, info == hoveringIndex);
			posY += buttonHeight;
			if (posY >= SCREEN_HEIGHT-buttonHeight*2) {
				posY = OFFSET_Y;
				posX += buttonWidth;
			}
		}
		posX = OFFSET_X;
		posY = OFFSET_Y;
		for (int i = 0; i < gameList.size(); i++) {
			GameInfo info = gameList.get(i);
			if (info == hoveringIndex)
				info.drawOn(g, posX, posY, buttonWidth, buttonHeight, info == hoveringIndex);
			posY += buttonHeight;
			if (posY >= SCREEN_HEIGHT-buttonHeight*2) {
				posY = OFFSET_Y;
				posX += buttonWidth;
			}
		}
		
		int halfScreen = SCREEN_WIDTH/2;
		int logHeight = SCREEN_HEIGHT-80;
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(halfScreen, 40, halfScreen-10, logHeight);
		g.setColor(Color.WHITE);
		g.drawRect(halfScreen, 40, halfScreen-10, logHeight);
		
		g.setColor(Color.BLACK);
		int startX = halfScreen+10;
		int startY = 40;
		double stepY = logHeight / logWindow;
		
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		for (int i = 0; i < logWindow; i++) {
			try {
				String line = changelog.get(i+logIndex);
				int x = startX;
				int y = (int)(startY + stepY * i);
				g.drawString(line, x, y);
			} catch (Exception e) {
				
			}
		}
		
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		g.drawString("Version " + version, 20, 40);
		
		gr.drawImage(buffer, 0, 0, null);
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
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		chosenIndex = getGameAt(x, y);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}
	
	public GameInfo getGameAt(int x, int y) {
		Iterator<GameInfo> games = gameList.iterator();
		while (games.hasNext()) {
			GameInfo game = games.next();
			if (game.contains(x, y)) {
				return game;
			}
		}
		
		return null;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		
		hoveringIndex = getGameAt(x, y);
		
		if (hoveringIndex != null) {
			for (GameInfo g : gameList) {
				if (g != hoveringIndex) g.setLoad(false);
			}
			hoveringIndex.setLoad(true);
		}
		
		repaint();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		SCREEN_WIDTH = getWidth();
		SCREEN_HEIGHT = getHeight();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {

	}
	
	
}
