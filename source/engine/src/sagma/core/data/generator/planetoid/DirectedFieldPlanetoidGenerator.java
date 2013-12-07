package sagma.core.data.generator.planetoid;

import java.awt.image.BufferedImage;

import sagma.core.data.generator.field.ArrayFieldGenerator;
import sagma.core.data.generator.field.FieldGenerator;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.data.generator.array.RandomArrayGenerator;
import sagma.core.material.Material;
import sagma.core.math.Vec3;
import sagma.core.render.Game;

public class DirectedFieldPlanetoidGenerator extends PlanetoidGenerator {
	private FieldGenerator field;
	public final static float DEFAULT_BUMP = 0.0001f;
	public static float BUMP = DEFAULT_BUMP;

	public DirectedFieldPlanetoidGenerator(String name, Material m) {
		this(name, new ArrayFieldGenerator(new RandomArrayGenerator(1.0f).nextArray2D()), m);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, Material m, int divisions) {
		this(name, new ArrayFieldGenerator(new RandomArrayGenerator(1.0f).nextArray2D()), m, divisions);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, FieldGenerator field) {
		this(name, field, DEFAULT_DIVISIONS);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, FieldGenerator field, int divisions) {
		super(name, divisions);
		this.field = field;
		generateNextPlanetoid();
		if (Game.savedDrawable != null) build();
	}
	
	public DirectedFieldPlanetoidGenerator(String name, FieldGenerator field, Material m) {
		this(name, field, m, DEFAULT_DIVISIONS);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, FieldGenerator field, Material m, int divisions) {
		super(name, divisions);
		setMaterial(m);
		this.field = field;
		generateNextPlanetoid();
		if (Game.savedDrawable != null) build();
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename) {
		this(name, Game.getImage(heightMapFilename));
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename, int divisions) {
		this(name, Game.getImage(heightMapFilename), divisions);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename, String materialFilename) {
		this(name, Game.getImage(heightMapFilename), Material.getMaterial(materialFilename));
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename, String materialFilename, int divisions) {
		this(name, Game.getImage(heightMapFilename), Material.getMaterial(materialFilename), divisions);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename, Material material) {
		this(name, Game.getImage(heightMapFilename), material);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, String heightMapFilename, Material material, int divisions) {
		this(name, Game.getImage(heightMapFilename), material, divisions);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, BufferedImage heightMap) {
		this(name, new ImageFieldGenerator(heightMap));
	}
	
	public DirectedFieldPlanetoidGenerator(String name, BufferedImage heightMap, int divisions) {
		this(name, new ImageFieldGenerator(heightMap), divisions);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, BufferedImage heightMap, Material material) {
		this(name, new ImageFieldGenerator(heightMap), material);
	}
	
	public DirectedFieldPlanetoidGenerator(String name, BufferedImage heightMap, Material material, int divisions) {
		this(name, new ImageFieldGenerator(heightMap), material, divisions);
	}

	@Override
	protected void modify() {
		for (int i = index-1; i >= 0; i--) {
			Vec3 vec = verts[i];
			float val = field.nextField(vec)*BUMP + 1.0f - BUMP;
			vec.m_normalize();
			vec.m_scale(radius);
			vec.m_scale(val);
		}
	}

}
