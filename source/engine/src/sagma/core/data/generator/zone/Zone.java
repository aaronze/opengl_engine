package sagma.core.data.generator.zone;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.generator.array.ArrayGenerator;
import sagma.core.material.Material;
import sagma.core.material.Shader;
import sagma.core.math.Quad;
import sagma.core.math.Vec3;
import sagma.core.model.Constructable;
import sagma.core.model.ModelConstructor;
import sagma.core.render.Game;

public class Zone implements Constructable {
	private ModelConstructor modelMaker;
	private Material material;
	private ArrayGenerator seed;
	private float[][] heightMap;
	private int width;
	private int height;
	
	/**
	 * Replaced with {@link #Zone(ArrayGenerator) Zone(ArrayGenerator)}
	 * @param drawable
	 * @param seed
	 */
	@Deprecated
	public Zone(GLAutoDrawable drawable, ArrayGenerator seed) {
		this.seed = seed;
		
		material = new Shader("Shaders/001");
		
		generateNextZone(1.0f);
	}
	
	public Zone(Material mat, ArrayGenerator seed, float size) {
		this.seed = seed;
		
		material = mat;
		
		generateNextZone(size);
	}
	
	public Zone(Material mat, ArrayGenerator seed) {
		this.seed = seed;
		
		material = mat;
		
		generateNextZone(1.0f);
	}
	
	public Zone(ArrayGenerator seed) {
		this.seed = seed;
		
		generateNextZone(1.0f);
	}
	
	public void setMaterial(Material m) {
		material = m;
	}
	
	public void generateNextZone() {
		generateNextZone(1.0f);
	}
	
	public void generateNextZone(float scale) {
		generateNextZone(Game.savedDrawable, scale);
	}
	
	/**
	 * Replaced with {@link #generateNextZone(size) generateNextZone(size)}
	 * 
	 * @param drawable
	 * @param scale
	 */
	@Deprecated
	public void generateNextZone(GLAutoDrawable drawable, float scale) {
		boolean useDefault = false;
		if (material == null) {
			useDefault = true;
		}
		
		heightMap = seed.nextArray2D();
		width = heightMap.length;
		height = heightMap[0].length;
		float max = heightMap[0][0], min = heightMap[0][0], avg = 0, total = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float val = heightMap[x][y];
                total += val;
                if (val < min) {
                    min = val;
                }
                if (val > max) {
                    max = val;
                }
            }
        }
        avg = total / (width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                heightMap[x][y] = (avg - heightMap[x][y]) * 4;
            }
        }

        /* SMOOTH ZONE */
        // Each point is the average of itself and all points around it, not including already averaged results
        float[][] map = new float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float tot = 0;
                int counter = 0;
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (i >= 0 && i < width && j >= 0 && j < height) {
                            tot += heightMap[i][j];
                            counter++;
                        }
                    }
                }
                map[x][y] = tot / counter / 3;
            }
        }
        heightMap = map;
        
        Vec3[][] vertexTable = new Vec3[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vertexTable[x][y] = new Vec3(x * scale, y * scale, heightMap[x][y]);
            }
        }

        Quad[][] polygonTable = new Quad[width - 1][height - 1];
        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                Vec3[] points = new Vec3[]{
                		vertexTable[x][y + 1],
                		vertexTable[x + 1][y + 1],
                		vertexTable[x + 1][y],
                		vertexTable[x][y]};
                polygonTable[x][y] = new Quad(points, null, null);
            }
        }

        Vec3[][] normalTable = new Vec3[width - 2][height - 2];
        for (int x = 1; x < width - 2; x++) {
            for (int y = 1; y < height - 2; y++) {
                float avgX = 0, avgY = 0, avgZ = 0;
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        Vec3 normal = polygonTable[i][j].getNormals()[0];
                        avgX += normal.getX();
                        avgY += normal.getY();
                        avgZ += normal.getZ();
                    }
                }
                normalTable[x][y] = new Vec3(avgX / 9, avgY / 9, avgZ / 9);
            }
        }
        
        Vec3[][] textureTable = new Vec3[width][height];
        for (int y = height-1; y >= 0; y--) {
        	for (int x = width-1; x >= 0; x--) {
        		float xVal = x * 1.0f / width;
        		float yVal = y * 1.0f / height;
        		textureTable[x][y] = new Vec3(xVal, yVal, 0);
        	}
        }
        
        modelMaker = new ModelConstructor("zone");
        if (useDefault)
        	modelMaker.setMaterial(new Shader("Shaders/gooch"));
        else
        	modelMaker.setMaterial(material);

        for (int x = 1; x < width - 3; x++) {
            for (int y = 1; y < height - 3; y++) {
                Quad quad = polygonTable[x][y];
                Vec3[] normals = new Vec3[4];
                normals[3] = normalTable[x][y];
                normals[2] = normalTable[x + 1][y];
                normals[1] = normalTable[x + 1][y + 1];
                normals[0] = normalTable[x][y + 1];
                quad.setNormals(normals);
                Vec3[] tex = new Vec3[4];
                tex[3] = textureTable[x][y];
                tex[2] = textureTable[x+1][y];
                tex[1] = textureTable[x + 1][y + 1];
                tex[0] = textureTable[x][y + 1];
                quad.setTextureCoords(tex);
                modelMaker.add(quad);
            }
        }
        
        modelMaker.build();
	}

	@Override
	public ModelConstructor getModelConstructor() {
		return modelMaker;
	}

}
