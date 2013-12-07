package sagma.core.material;

import java.util.ArrayList;

import javax.media.opengl.GL2;

import sagma.core.data.manager.MaterialManager;

/**
 * Represents a material to be displayed over applicable polygons
 * 
 * @author Aaron Kison
 *
 */
public class Material {
	protected boolean hasMask = false;
	protected String name;
	private ArrayList<Material> materials;
	
	public Material() {
		materials = new ArrayList<Material>(4);
	}
	
	/**
	 * @param gl Can be implemented by children 
	 */
	public void activateMask(GL2 gl) {
		
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @param gl Can be implemented by children 
	 */
	public void deactivateMask(GL2 gl) {
		
	}
	
	public boolean hasMask() {
		return hasMask;
	}
	
	public static Material getMaterial(String filename) {
		return MaterialManager.load(filename);
	}
	
	public void activate(GL2 gl) {
		for (int i = 0; i < materials.size(); i++) {
			Material mat = materials.get(i);
			mat.activate(gl);
		}
	}
	public void deactivate(GL2 gl) {
		for (int i = 0; i < materials.size(); i++) {
			Material mat = materials.get(i);
			mat.deactivate(gl);
		}
	}
	
	public void add(Material mat) {
		materials.add(mat);
	}
	
	public void destroy() {
		
	}
	
	public void setName(String s) {
		name = s;
	}
}
