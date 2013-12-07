package sagma.core.material;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import sagma.core.render.Game;

public class Displacement extends Material {
	private int id;
	
	public Displacement(BufferedImage image) {
		int[] ids = new int[1];
		GL2 gl = Game.savedDrawable.getGL().getGL2();
		gl.glGenBuffers(1, ids, 0);
		id = ids[0];
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, id);
		
		Buffer destination = null;

        int w = image.getWidth();
        int h = image.getHeight();
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
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, w, h, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, destination);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
	}
	
	@Override
	public void activate(GL2 gl) {
		
	}
	
	@Override
	public void deactivate(GL2 gl) {
		
	}
}
