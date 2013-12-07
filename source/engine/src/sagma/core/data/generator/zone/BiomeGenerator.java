package sagma.core.data.generator.zone;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import sagma.core.data.generator.array.ArrayGenerator;
import sagma.core.data.generator.array.DefinedArrayGenerator;
import sagma.core.data.generator.field.ArrayFieldGenerator;
import sagma.core.data.generator.field.ImageFieldGenerator;
import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.data.generator.planetoid.PlanetoidGenerator;
import sagma.core.data.generator.zone.biome.BiomeType;
import sagma.core.data.generator.zone.biome.Desert;
import sagma.core.data.generator.zone.biome.Forest;
import sagma.core.data.generator.zone.biome.Lava;
import sagma.core.data.generator.zone.biome.Ocean;
import sagma.core.data.generator.zone.biome.Snow;
import sagma.core.material.Material;
import sagma.core.material.Texture;
import sagma.core.math.Quad;
import sagma.core.math.Vec2;
import sagma.core.math.Vec3;
import sagma.core.model.Constructable;
import sagma.core.model.ModelConstructor;

public class BiomeGenerator implements Constructable {
	int width;
	int height;
	private float[][] heightMap;
	private ArrayGenerator heightMapMaker;
	private ModelConstructor maker;
	private BufferedImage texture;

	public int NUMBER_OF_BIOMES = 100;
	
	private ArrayList<BiomeEmitter> biomeEmitters;
	
	public BiomeGenerator(int w, int h) {
		width = w;
		height = h;
		
		generateNextZone();
	}
	
	private void generateNextZone() {
		maker = new ModelConstructor("biome");
		biomeEmitters = new ArrayList<BiomeEmitter>();
		
		// Compiler is WRONG. This value MUST be declared (but is not directly used)
		@SuppressWarnings("unused")
		BiomeManager biomeInit = new BiomeManager();
		
		Vec2 pos = new Vec2(0, 0);
		
		
		for (int i = 0; i < NUMBER_OF_BIOMES; i++) {
			pos = new Vec2((float)Math.random()*width, (float)Math.random()*height);
			BiomeType up = getBiomeType((int)pos.x, (int)pos.y-1);
			BiomeType down = getBiomeType((int)pos.x, (int)pos.y+1);
			BiomeType right = getBiomeType((int)pos.x+1, (int)pos.y);
			BiomeType left = getBiomeType((int)pos.x-1, (int)pos.y);
			BiomeType cur = blend(up, down, left, right);
			fillBiome(cur, pos);
			/*double d = Math.random();
			if (d < 0.2)
				fillBiome(BiomeManager.ocean, pos);
			else if (d < 0.4)
				fillBiome(BiomeManager.lava, pos);
			else if (d < 0.6)
				fillBiome(BiomeManager.snow, pos);
			else if (d < 0.8) 
				fillBiome(BiomeManager.forest, pos);
			else 
				fillBiome(BiomeManager.desert, pos);*/
		}
		fillBiome(BiomeManager.snow, new Vec2(0, 0));
		fillBiome(BiomeManager.snow, new Vec2(0, height));
		
		// Convert to array height-map
		heightMapMaker = makeGenerator();
		
		heightMap = heightMapMaker.nextArray2D();
	}
	
	BiomeType getBiomeType(float x, float y) {
		if (biomeEmitters.size() == 0) return null;
		
		float closestDist = Float.MAX_VALUE;
		BiomeEmitter closest = null;
		
		for (BiomeEmitter b : biomeEmitters) {
			Vec2 v = b.location;
			float vx = v.x;
			float vy = v.y;

			float xD = vx - x;
			float yD = vy - y;
			float dist = xD*xD + yD*yD;
			
			if (dist < closestDist) {
				closestDist = dist;
				closest = b;
			}
		}
		
		if (closest == null) return BiomeManager.field;
		return closest.type;
	}
	
	private static BiomeType blend(BiomeType a, BiomeType b, BiomeType c, BiomeType d) {
		if (a == null && b == null && c == null && d == null) {
			return BiomeManager.plains;
		}
		ArrayList<BiomeType> matches = new ArrayList<BiomeType>();
		BiomeType[] listA = null;
		BiomeType[] listB = null;
		BiomeType[] listC = null;
		BiomeType[] listD = null;
		if (a != null) listA = a.getRelatedBiomes();
		if (b != null) listB = b.getRelatedBiomes();
		if (c != null) listC = c.getRelatedBiomes();
		if (d != null) listD = d.getRelatedBiomes();
		
		if (listA != null) {
			for (BiomeType biome : listA) {
				if (biome != null) matches.add(biome);
			}
		}
		if (listB != null) {
			for (BiomeType biome : listB) {
				if (biome != null) matches.add(biome);
			}
		}
		if (listC != null) {
			for (BiomeType biome : listC) {
				if (biome != null) matches.add(biome);
			}
		}
		if (listD != null) {
			for (BiomeType biome : listD) {
				if (biome != null) matches.add(biome);
			}
		}

		if (matches.size() == 0) {
			return BiomeManager.plains;
		}
		
		int index = (int)(Math.random()*matches.size());
		return matches.get(index);
	}
	
	private void fillBiome(BiomeType type, Vec2 pos) {
		biomeEmitters.add(new BiomeEmitter(type, pos));
	}
	
	private ArrayGenerator makeGenerator() {
		float[][] a = new float[width][height];
		float scale = 1.0f;
		texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = height-1; y >= 0; y --) {
			for (int x = width-1; x >= 0; x --) {
				float u = x * 1.0f / width;
				float v = y * 1.0f / height;
				a[x][y] = (getBiomeType(x, y).getHeightAtLocation(u, v) * scale);
				int rgb = (getBiomeType(x, y).getRGBAtLocation(u, v));
				texture.setRGB(x, y, rgb);
			}
		}
		return new DefinedArrayGenerator(a);
	}

	@Override
	public ModelConstructor getModelConstructor() {
		return maker;
	}
	
	public void generateNextZone(float scale) {
		float[][] mapData = new float[width][height];
		for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	mapData[x][y] = this.heightMap[x][y];
            }
		}
		
		float max = mapData[0][0], min = mapData[0][0], avg = 0, total = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float val = mapData[x][y];
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
                mapData[x][y] = (avg - mapData[x][y]) * 4;
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
                            tot += mapData[i][j];
                            counter++;
                        }
                    }
                }
                map[x][y] = tot / counter / 3;
            }
        }
        mapData = map;
        
        Vec3[][] vertexTable = new Vec3[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                vertexTable[x][y] = new Vec3(x * scale, y * scale, mapData[x][y]);
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
        
        for (int y = 1; y < height - 3; y++) {
        	System.out.println(y + " / " + height);
        	for (int x = 1; x < width - 3; x++) {
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
                maker.setMaterial(getBiomeType(x, y).getMaterialForHeight(vertexTable[x][y].y));
                maker.add(quad);
            }
        }
        
        maker.build();
	}
	
	/**
	 * @param scale unused because information is dealt with at shader level 
	 */
	public BiomeGenerator generateNextShadedPlanetoid(String name, float scale) {
		PlanetoidGenerator generator = new DirectedFieldPlanetoidGenerator(name,
				new ArrayFieldGenerator(heightMapMaker.nextArray2D())) {
			@Override
			public Material getMaterial(Vec3[] vertex) {
				Vec3 pos = ImageFieldGenerator.getUVOfVector(vertex[0]);
				int x = (int)(pos.x * width);
				int y = (int)(pos.y * height);
				
				if (x >= 0 && x < width && y >= 0 && y < height) {
					return getBiomeType(x, y).getMaterialForHeight(vertex[0].length());
				}
				return BiomeType.DEFAULT;
			}
		};
		maker = generator.getModelConstructor();
		return this;
	}
	
	/**
	 * @param scale unused because of shader 
	 */
	public BiomeGenerator generateNextPlanetoid(String name, float scale) {
		PlanetoidGenerator generator = new DirectedFieldPlanetoidGenerator(name,
				new ArrayFieldGenerator(heightMapMaker.nextArray2D()),
				new Texture(texture), 5);
		maker = generator.getModelConstructor();
		return this;
	}
	
	private class BiomeEmitter {
		public BiomeType type;
		public Vec2 location;
		
		public BiomeEmitter(BiomeType t, Vec2 v) {
			type = t;
			location = v;
		}
	}

	public void displayPerc() {
		int forestCount = 0;
		int snowCount = 0;
		int lavaCount = 0;
		int waterCount = 0;
		int desertCount = 0;
		
		for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	BiomeType type = getBiomeType(x, y);
            	if (type.getClass().equals(Forest.class)) forestCount++;
            	if (type.getClass().equals(Snow.class)) snowCount++;
            	if (type.getClass().equals(Lava.class)) lavaCount++;
            	if (type.getClass().equals(Ocean.class)) waterCount++;
            	if (type.getClass().equals(Desert.class)) desertCount++;
            }
		}
		
		int max = width*height;
		
		System.out.println("Forest: "+(forestCount * 100 / max) + "%");
		System.out.println("Snow: "+(snowCount * 100 / max) + "%");
		System.out.println("Desert: "+(desertCount * 100 / max) + "%");
		System.out.println("Water: "+(waterCount * 100 / max) + "%");
		System.out.println("Lava: "+(lavaCount * 100 / max) + "%");
	}
}
