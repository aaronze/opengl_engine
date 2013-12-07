package sagma.core.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import sagma.core.render.Game;
import sagma.core.render.GameLoader;

/**
 * Represents the information about a game
 * 
 * @author Aaron Kison
 *
 */
public class GameInfo {
	public static Font normal = new Font("Arial", Font.PLAIN, 14);
	public static Font italics = new Font("Arial", Font.ITALIC, 10);
	
	private String name = "";
	private String path;
	private Image icon;
	private String author;
	private float progress = 1.0f;
	private File game;
	private File configFile;
	private String mainClass;
	
	private int xPosition;
	private int yPosition;
	private int width;
	private int height;
	
	private boolean isValid = true;
	private boolean isLoading = false;
	boolean isFullscreen = false;
	Game gameInstance;
	
	public GameInfo(String gameRoot) {
		path = gameRoot;
		
		configFile = getConfigFile();
	}
	
	public boolean isValid() {
		return isValid && progress == 1.0f;
	}
	
	public boolean contains(int x, int y) {
		return (x > xPosition && x < xPosition+width && y > yPosition && y < yPosition+height);
	}
	
	public static void drawBalloon(Graphics graphics, BufferedImage img, int x, int y, int w, int h, float balloonFactor) {
		BufferedImage area = img.getSubimage(x, y, w, h);
		
		int newWidth = (int)(w * balloonFactor);
		int newHeight = (int)(h * balloonFactor);
		int startX = x - (newWidth-w)/2;
		int startY = y - (newHeight-h)/2;
		
		graphics.drawImage(area.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), startX, startY, null);
	
		
	}
	
	public void drawOn(Graphics graphics, int x, int y, int w, int h, boolean selected) {
		BufferedImage buffer = new BufferedImage(GameLoader.SCREEN_WIDTH, GameLoader.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		
		FontMetrics italicMetrics = g.getFontMetrics(italics);
		
		xPosition = x;
		yPosition = y;
		
		width = w;
		height = h;
		
		if (selected) {
			PointerInfo pointer = MouseInfo.getPointerInfo();
			int mouseY = pointer.getLocation().y-24;
			int dif = Math.abs(mouseY - (y+h/2));
			float scale = 1.4f - (dif * 0.4f / (h/2));
			if (scale > 1.4f) scale = 1.4f;
			if (scale < 1.0f) scale = 1.0f;
			int newWidth = (int)(w * scale);
			int newHeight = (int)(h * scale);
			x = x - (newWidth-w)/2;
			y = y - (newHeight-h)/2;
			w = newWidth;
			h = newHeight;
		}
		
		if (progress < 1.0f) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(x+1, y+1, w-2, h-2);
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x+2, y+2, (int)((w-4)*progress), h-4);
			
			g.setColor(Color.WHITE);
			int perc = (int)(100 * progress);
			g.setFont(italics);
			String string = name + " ..  " + perc + "%";
			int stringW = italicMetrics.stringWidth(string);
			g.drawString(name + " ..  " + perc + "%", x+(w/2)-(stringW/2), y + (h/2)+1);
			return;
		}
		
		// Draw background
		if (selected)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x+1, y+1, w-2, h-2);
		
		if (selected)
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(Color.DARK_GRAY);
		g.fillRect(x+2, y+2, w-4, h-4);
		
		// Draw the information
		g.setColor(Color.WHITE);
		
		if (icon != null) {
			if (author == null) {
				g.drawImage(icon, x+3, y+3, h-6, h-6, null);
				
				g.setFont(normal);
				g.drawString(name, x + h, y + (h/2)+1);
			} else {
				g.drawImage(icon, x+3, y+3, h-6, h-6, null);
				
				g.setFont(normal);
				g.drawString(name, x + h, y + (h/3)+1);
				
				g.setFont(italics);
				int endPos = x + w - italicMetrics.stringWidth(author) - 5;
				g.drawString(author, endPos, y + (h*2/3));
			}
		} else {
			if (author == null) {
				g.setFont(normal);
				g.drawString(name, x + 6, y + (h/2)+1);
			} else {
				g.setFont(normal);
				g.drawString(name, x + 6, y + (h/3+1));
				
				g.setFont(italics);
				int endPos = x + w - italicMetrics.stringWidth(author) - 5;
				g.drawString(author, endPos, y + (h*2/3));
			}
		}
		
		/*if (selected) {
			float scale = 1.2f;
			drawBalloon(graphics, buffer, x, y, w, h, scale);
		} else {*/
			graphics.drawImage(buffer.getSubimage(x, y, w, h), x, y, null);
		//}
	}
	
	public File getConfigFile() {
		configFile = new File(path + "/config.ini");
		if (!configFile.exists()) {
			// Attempt recovery methods - assume lazy people havent created one.
			
			// Step 1: Attempt to find the main class
			
			int lastSlash = path.lastIndexOf('/');
			String mainName = path.substring(lastSlash+1, path.length());
			mainName = (""+mainName.charAt(0)).toUpperCase() + mainName.substring(1, mainName.length());
			String className = path + "/" + mainName + ".java";
			game = new File(className);
			
			if (!game.exists()) {
				// Unrecoverable
				System.out.println(path + " entry point could not be found. You need a config.ini file.");
				isValid = false;
				return null;
			}
			
			// Step 2: Create the appropriate configuration file
			try {
				configFile.createNewFile();
				PrintWriter writer = new PrintWriter(new FileWriter(configFile));
				writer.println("Main-Class: " + mainName);
				writer.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		// Retrieve data
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configFile));
			String line;
			while ((line = reader.readLine()) != null) {
				int index = line.indexOf(':');
				String command = line.substring(0, index);
				String value = line.substring(index+1, line.length());
				
				if (command.equalsIgnoreCase("Main-Class")) {
					mainClass = trimmed(value);
					if (name.equals("")) {
						name = mainClass;
					}
				}
				if (command.equalsIgnoreCase("Name")) {
					name = value;
				}
				if (command.equalsIgnoreCase("Icon")) {
					icon = Toolkit.getDefaultToolkit().getImage(path + "/" + trimmed(value));
				}
				if (command.equalsIgnoreCase("Author")) {
					author = trimmed(value);
				}
				if (command.equalsIgnoreCase("fullscreen")) {
					isFullscreen = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return configFile;
	}
	
	public String getName() {
		return mainClass;
	}
	
	public String getPath() {
		return path;
	}
	
	private static String trimmed(String s) {
		return new StringTokenizer(s).nextToken();
	}
	
	public void setLoad(boolean b) {
		if (isLoading && !b) {
			stopLoading();
		}
		if (!isLoading && b) {
			startLoading();
		}
	}
	
	public Game getLoadedGame() {
		return gameInstance;
	}
	
	private void startLoading() {
		progress = 1.0f;
		isLoading = true;
		final GameInfo info = this;
		
		new Thread() {
			@Override
			public void run() {
				gameInstance = GameLoader.getGame(info);
				gameInstance.setFullscreen(isFullscreen);
				if (gameInstance != null) gameInstance.preLoadAll();
			}
		}.start();
		
	}
	
	private void stopLoading() {
		isLoading = false;
		
		gameInstance = null;
	}
	
}
