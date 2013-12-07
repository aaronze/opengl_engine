package sagma.core.model;

/**
 *
 * @author Aaron Kison
 */
import javax.media.opengl.*;
import sagma.core.render.Drawable;

public class ModelBatch implements Drawable {

    private Model model;
    private float[][] locations, rotations;

    public ModelBatch(Model m, float[][] locs, float[][] rots) {
        model = m;
        locations = locs;
        rotations = rots;
    }

    @Override
	public void draw(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        float[] location, rotation;

        for (int i = 0; i < locations.length; i++) {

            location = locations[i];
            rotation = rotations[i];

            gl.glPushMatrix();
            gl.glTranslatef(location[0], location[1], location[2]);
            gl.glRotatef(rotation[0], 1, 0, 0);
            gl.glRotatef(rotation[1], 0, 1, 0);
            gl.glRotatef(rotation[2], 0, 0, 1);

            model.draw(drawable);
            gl.glPopMatrix();
        }
    }
}
