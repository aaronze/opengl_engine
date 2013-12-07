package sagma;


import com.jogamp.opengl.util.Animator;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import sagma.core.render.Game;
import sagma.core.render.GameLoader;
import sagma.core.render.Render;

/**
 *
 * @author Aaron Kison
 */
@SuppressWarnings("serial")
public class Driver extends JFrame {
	public static GLCanvas glCanvas;
	public static Driver application;

    public static void main(String[] args) {
    	GameLoader loader = new GameLoader();
    	Game game = null;
    	while (game == null) {
    		game = loader.chooseGame();
    		try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
        application = new Driver(game);

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                application.setUndecorated(true);
                application.setVisible(true);
                application.setSize(Render.WIDTH, Render.HEIGHT);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                animator.start();
            }
        });
    }
    static Animator animator = null;
    
    public final static void switchToFullScreen() {
    	GraphicsDevice monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	try {
            monitor.setFullScreenWindow(application);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Fullscreen not supported. Continuing Happily");
        }
    	int width = monitor.getDisplayMode().getWidth();
    	int height = monitor.getDisplayMode().getHeight();
    	Render.updateScreenSize(width, height);
    }

    public Driver(Game game) {
        super("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        glCanvas = new GLCanvas();
    
        Render render = new Render(game);
        if (game.isFullscreen()) {
        	setSize(Render.WIDTH, Render.HEIGHT);
        	setUndecorated(true);
        	switchToFullScreen();
        }
        glCanvas.addGLEventListener(render);
        glCanvas.addKeyListener(render);
        glCanvas.addMouseMotionListener(render);
        glCanvas.addMouseListener(render);
        add(glCanvas, BorderLayout.CENTER);
        animator = new Animator(glCanvas);
    }
    
    public Driver() {
    	
    }
}
