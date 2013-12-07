package sagma.core.material;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import sagma.core.render.Game;

/**
 *
 * @author Aaron Kison
 */

public class Texture extends Material {
	private int id;
	private int maskId;
	private int width;
	private int height;
	private Shader maskShader;
	
	public final static int GL_TEXTURE_ANTISO = 0x84FE;
	public final static int GL_MAX_TEXTURE_ANTISO = 0x84FF;
	
	/**
	 * Drawable is kind-of unnecesary. See {@link #Texture(String) Texture(String)}
	 * 
	 * @param drawable
	 * @param filename
	 */
	@Deprecated
	public Texture(GLAutoDrawable drawable, String filename) throws IOException {
		this(filename);
	}
	
	public Texture(String filename) throws IOException {
		GLAutoDrawable drawable = Game.savedDrawable;
        GL2 gl = drawable.getGL().getGL2();
        this.name = filename;
        
        BufferedImage img = getPNG(filename);
        int dotIndex = filename.indexOf('.');
        String texName = filename.substring(0, dotIndex);
        String ext = filename.substring(dotIndex+1, filename.length());
        
        width = img.getWidth();
        height = img.getHeight();
        
        if (width == -1 && height == -1) {
        	// Texture does not exist. Abort.
        	throw new IOException("Texture was not found.");
        }
        
        BufferedImage mask = null;
        if (new File(texName + "_." + ext).exists())
        	mask = getPNG(texName + "_." + ext);
        
        if (img != null) {
        	if (mask != null) {
        		maskId = generateTexture(gl);
        		gl.glBindTexture(GL.GL_TEXTURE_2D, maskId);
        		makeTexture(gl, mask, GL.GL_TEXTURE_2D);
                init(gl);
                hasMask = true;
                
                maskShader = new Shader("Shaders/texture_discarder");
        	}
        	id = generateTexture(gl);
            gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        	makeTexture(gl, img, GL.GL_TEXTURE_2D);
            init(gl);
        }
    }
	
	@Override
	public String getName() {return name;}
	
	public Texture(BufferedImage img) {
        GL2 gl = Game.savedDrawable.getGL().getGL2();
        id = generateTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        makeTexture(gl, img, GL.GL_TEXTURE_2D);
        init(gl);
    }
	
	public Texture(GL2 gl, BufferedImage img) {
        id = generateTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        makeTexture(gl, img, GL.GL_TEXTURE_2D);
        init(gl);
    }
	
	
	public Texture(GLAutoDrawable drawable, BufferedImage img, String name) {
        GL2 gl = drawable.getGL().getGL2();
        this.name = name;
        id = generateTexture(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        makeTexture(gl, img, GL.GL_TEXTURE_2D);
        init(gl);
    }
	
	public void init(GL2 gl) {
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
	}
	
	private static BufferedImage getPNG(String filename) {
        try {
            URL url = getResource(filename);
            if (url == null) {
                throw new RuntimeException("Can not read " + filename);
            }
            BufferedImage img = ImageIO.read(url);
            return img;
        } catch (Exception e) {
        	System.out.println(e);
            System.out.println("Error loading texture, can't find: " + filename);
        }
        return null;
    }
	
	private static URL getResource(String filename) {
        URL url = ClassLoader.getSystemResource(filename);
        if (url == null) {
            try {
                url = new URL("file", "localhost", filename);
            } catch (Exception e) {
            }
        }
        return url;
    }

    private static int generateTexture(GL2 gl) {
        final int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }
    
    public void makeTexture(GL2 gl, BufferedImage image, int target) {
        Buffer destination = null;
       
        int w = image.getWidth();
        int h = image.getHeight();
        int[] textureData = image.getData().getPixels(0, 0, w, h, (int[])null);
        int length = w*h;
        int[] data = new int[length];
        
        if (textureData.length / data.length == 3) {
        	int pos = 0;
	        for (int i = 0; i < length; i++) {
	        	int red = textureData[pos++];
        		int green = textureData[pos++];
        		int blue = textureData[pos++];
        		data[i] = red | (green << 8) | (blue << 16) | (255 << 24);
	        }
        } else if (textureData.length / data.length == 4){
        	int pos = 0;
        	for (int i = 0; i < length; i++) {
        		int red = textureData[pos++];
        		int green = textureData[pos++];
        		int blue = textureData[pos++];
        		int alpha = textureData[pos++];
        		data[i] = red | (green << 8) | (blue << 16) | (alpha << 24);
        	}
        } else if (textureData.length == data.length) {
        	data = textureData;
        }

        destination = IntBuffer.wrap(data);
        gl.glTexImage2D(target, 0, GL.GL_RGBA, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, destination);
    }
    
    public void updateTexture(BufferedImage image) {
    	GL2 gl = Game.savedDrawable.getGL().getGL2();
    	Buffer destination = null;

        int w = image.getWidth(), h = image.getHeight();
        int[] textureData = new int[w * h];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = image.getRGB(x, y);
                Color c = new Color(rgb);
                Color res = new Color(c.getBlue(), c.getGreen(), c.getRed(), c.getAlpha());
                textureData[x + y * w] = res.getRGB();
            }
        }

        destination = IntBuffer.wrap(textureData);
        
        gl.glDeleteBuffers(1, new int[]{id}, 0);
        
        int[] i = new int[1];
        gl.glGenBuffers(1, i, 0);
        id = i[0];
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        init(gl);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, destination);
    
    }
    
    @Override
	public void activateMask(GL2 gl) {
    	maskShader.activate(gl);
    	
    	gl.glEnable(GL.GL_TEXTURE_2D);
    	
    	gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
        int texLoc = gl.glGetUniformLocation(getShaderID(), "textureImage");
        gl.glUniform1iARB(texLoc, 0);
        
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, maskId);
        int maskLoc = gl.glGetUniformLocation(getShaderID(), "maskImage");
        gl.glUniform1iARB(maskLoc, 1);
        
    }
    
    @Override
	public void deactivateMask(GL2 gl) {
    	maskShader.deactivate(gl);
    	
    	gl.glActiveTexture(GL.GL_TEXTURE0);
		gl.glDisable(GL.GL_TEXTURE_2D);
	}

	@Override
	public void activate(GL2 gl) {
		if (hasMask()) {
			activateMask(gl);
		} else {
			gl.glEnable(GL.GL_TEXTURE_2D);
	        gl.glBindTexture(GL.GL_TEXTURE_2D, id);
		}
	}

	@Override
	public void deactivate(GL2 gl) {
		if (hasMask()) {
			deactivateMask(gl);
		} else {
			gl.glDisable(GL.GL_TEXTURE_2D);
		}
	}

	public static boolean isTexture(String name) {
		File file = new File(name);
		return file.exists();
	}
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getID() {
		return id;
	}
	
	public int getShaderID() {
		return maskShader.getID();
	}
	
	@Override
	public void destroy() {
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		
		gl.glDeleteBuffers(1, new int[]{id}, 0);
	}
	
	/**
	 * I think this is what it should do.
	 * 
	 * @return
	 */
	public float getAspectRatio() {
		return width*1.0f/height;
	}
	
	public final static int makeEmptyTexture(GL2 gl, int width, int height) {
		int[] texNumber = new int[1];
		int[] data = new int[width*height*4];
		IntBuffer buffer = IntBuffer.wrap(data);
		
		gl.glGenBuffers(1, texNumber, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texNumber[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);   
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
	    gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		
		return texNumber[0];
	}
	
	public Texture(GL2 gl, int width, int height) {
		int[] texNumber = new int[1];
		int[] data = new int[width*height*4];
		IntBuffer buffer = IntBuffer.wrap(data);
		
		gl.glGenBuffers(1, texNumber, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, texNumber[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);   
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
	    gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
	
	    id = texNumber[0];
	}
	
}