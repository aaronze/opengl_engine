package sagma.core.material;

import java.io.File;
import java.util.StringTokenizer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import sagma.core.data.FileStringReader;
import sagma.core.render.Game;

/**
 *
 * @author Aaron Kison
 */

public class Shader extends Material {
	private int id;
	
	public Shader(String name) {
        this(name + ".vert", name + ".frag");
        this.name = name;
    }
	
	private Shader(String vertexFilename, String fragmentFilename) {
		GL2 gl = Game.savedDrawable.getGL().getGL2();

        int vShader = 0;
        int fShader = 0;

        try {
            id = gl.glCreateProgram();
            String vSource = FileStringReader.readFileAsString(vertexFilename);
            String[] v = {vSource};
            vShader = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
            gl.glShaderSource(vShader, 1, v, (int[]) null, 0);
            gl.glAttachShader(id, vShader);
            gl.glCompileShader(vShader);

            String fSource = FileStringReader.readFileAsString(fragmentFilename);
            String[] f = {fSource};
            fShader = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
            gl.glShaderSource(fShader, 1, f, (int[]) null, 0);
            gl.glAttachShader(id, fShader);
            gl.glCompileShader(fShader);

            gl.glLinkProgram(id);
            gl.glValidateProgram(id);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("FAIL: Could not load shader at " + vertexFilename);
            throw new RuntimeException(e);
        }
	} 

    public static String cut(String s) {
        StringTokenizer tokey = new StringTokenizer(s, "./");
        tokey.nextToken();
        if (tokey.hasMoreTokens()) {
            return tokey.nextToken();
        }
        return "";
    }

	@Override
	public void activate(GL2 gl) {
		gl.glUseProgram(id);
        gl.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_POLYGON_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void deactivate(GL2 gl) {
		gl.glPopAttrib();
        gl.glUseProgram(0);
	}

	public static boolean isShader(String name) {
		File file = new File(name + ".vert");
		return file.exists();
	}
	
	public int getID() {
		return id;
	}

}
