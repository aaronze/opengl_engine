package sagma.core.data.generator.volume;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sagma.core.data.generator.noise.PerlinNoiseGenerator;
import sagma.core.material.Material;
import sagma.core.material.Texture;

public class CloudVolumeGenerator extends VolumeGenerator {
	public int WIDTH = 128;
	public int HEIGHT = 128;
	
	{
		TIME = 1;
	}
	
	private Material[][] layers;
	private PerlinNoiseGenerator cloudGen;
	
    public CloudVolumeGenerator() {
    	remake();
    }
    
    public CloudVolumeGenerator(int depth, int time) {
    	DEPTH = depth;
    	TIME = time;
    	remake();
    }
    
    @Override
	public void remake() {
    	cloudGen = new PerlinNoiseGenerator(TIME, WIDTH, HEIGHT, DEPTH);
    	cloudGen.FALLOFF = 0.4f;
    	cloudGen.SHARPNESS = 0.2f;
    	
    	layers = new Material[TIME][DEPTH];
    	float[][][][] f4;
    	
    	if (TIME > 1) 
    		f4 = cloudGen.nextArray4D();
    	else {
    		cloudGen = new PerlinNoiseGenerator(WIDTH, HEIGHT, DEPTH);
    		f4 = new float[1][WIDTH][HEIGHT][DEPTH];
    		f4[0] = cloudGen.nextArray3D();
    	}
    	
    	for (int t = 0; t < TIME; t++) {
    		float[][][] f = f4[t];
    		
	    	for (int i = 0; i < DEPTH; i++) {
	    		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	            
	    		for (int z = 0; z < DEPTH; z++) {
		    		for (int y = 0; y < HEIGHT; y++) {
		    			for (int x = 0; x < WIDTH; x++) {
		                	float gray = f[x][y][z] / 10;
		                    image.setRGB(x, y, new Color(gray, gray, gray, gray).getRGB());
		                }
		            }
	    		}
	            
	            layers[t][i] = new Texture(image);
	    	}
    	}
    }

	@Override
	public Material generateNextMaterial(int depth) {
		return generateNextMaterial(depth, 0);
	}
	
	@Override
	public Material generateNextMaterial(int depth, int time) {
		return layers[time][depth];
	}

}
