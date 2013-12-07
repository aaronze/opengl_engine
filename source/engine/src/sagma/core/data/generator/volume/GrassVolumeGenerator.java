package sagma.core.data.generator.volume;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import sagma.core.data.generator.array.RandomArrayGenerator;
import sagma.core.data.generator.number.BundledRandomNumberGenerator;
import sagma.core.material.Material;
import sagma.core.material.Texture;
import sagma.core.render.Render;

public class GrassVolumeGenerator extends VolumeGenerator {
	private int[][][] alphaMap;
    public int WIDTH = 512, HEIGHT = 512, MAX_HEIGHT = DEPTH;
    public float DENSITY = 256;
    public float PUSH_SCALE = 0.002f, SIZE_SCALE = 1.0f / 2.0f;

    public GrassVolumeGenerator() {
        remake();
    }
    
    @Override
	public void remake() {
    	DEPTH = 40;
    	alphaMap = new int[DEPTH][WIDTH][HEIGHT];

        float xStep = WIDTH / DENSITY;
        float yStep = HEIGHT / DENSITY;
        
        float[][] valMap = new RandomArrayGenerator(
        		new BundledRandomNumberGenerator(3, 5, 0.1f), WIDTH, HEIGHT).nextArray2D();

        for (int i = 0; i < DEPTH; i++) {
            for (float x = xStep / 2; x < WIDTH; x += xStep) {
                for (float y = yStep / 2; y < HEIGHT; y += yStep) {
                    int xPos = (int) (x + i * i * PUSH_SCALE);
                    double size = (DEPTH - i) * SIZE_SCALE;

                    int val = (int)(valMap[(int)x][(int)y]);
                    fillCircle(i, xPos, (int)y, (int) (size), val);
                }
            }
        }
    }

    private void fillCircle(int i, int x, int y, int size, int value) {
        for (int xi = -size; xi < size; xi++) {
            for (int yi = -size; yi < size; yi++) {
                if (((xi * xi) + (yi * yi)) < (size * size)) {
                    fillDot(i, xi + x, yi + y, value);
                }
            }
        }
    }

    private void fillDot(int i, int x, int y, int value) {
        if (i >= 0 && i < DEPTH && x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            alphaMap[i][x][y] = value;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);

        for (int i = 0; i < DEPTH; i++) {
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    int color = alphaMap[i][x][y];
                    if (color > 0) {
                        int xTran = x + y / 3;
                        int yTran = Render.HEIGHT - y - i;

                        g.drawLine(xTran, yTran, xTran, yTran);
                    }
                }
            }
        }
    }

    public int[][][] getVolume() {
        return alphaMap;
    }

    public BufferedImage getImageSlice(int index) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int col = alphaMap[index][x][y];
                int alpha = 0;
                if (col > 0) alpha = 255;
                image.setRGB(x, y, new Color(0, col/255f, 0, alpha/255f).getRGB());
            }
        }
        return image;
    }

	@Override
	public Material generateNextMaterial(int depth) {
		return new Texture(getImageSlice(depth));
	}

}
