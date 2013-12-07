package sagma.core.data;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Work in progress, not fully functional yet.
 * 
 * @author Aaron Kison
 *
 */
@Deprecated
public class Extradicator {
    private ArrayList<ColorRectangle> colorRectangles;
    private boolean[][] mask;
    private boolean[][] clone;
    private int width;
    private int height;
    private BufferedImage image;
    private int maxDepth;

    public Extradicator(String filename) {
    	image = getImage(filename);
        colorRectangles = new ArrayList<ColorRectangle>(1000);

        width = image.getWidth();
        height = image.getHeight();

        mask = new boolean[width][height];
        clone = new boolean[width][height];
        
        //reduceColorDepth();
        reduceImage(0, 0, width, height, 1);

    }
    
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    
    private static BufferedImage getImage(String filename) {
    	Image i = Toolkit.getDefaultToolkit().getImage(filename);
        MediaTracker mt = new MediaTracker(new Frame());
        mt.addImage(i, 0);
        try {
            mt.waitForAll();
        } catch (Exception e) {
        }
        BufferedImage testImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        testImage.getGraphics().drawImage(i, 0, 0, null);
        return testImage;
	}

	public int getMaxDepth() { return maxDepth;}
    public void draw(Graphics g, int depth) {
        for (int j = 1; j <= depth; j++) {
            for (int i = 0; i < colorRectangles.size(); i++) {
                ColorRectangle cr = colorRectangles.get(i);
                if (cr.depth == j) {
                    g.setColor(new Color(cr.colour));
                    g.fillRect(cr.x, cr.y, cr.w, cr.h);
                }
            }
        }
    }
    
	@SuppressWarnings("unused")
	private void reduceColorDepth() {
        HashMap<Integer, ColorCounter> hashMap = buildHashMap(image, 0, 0, width, height);
        
        Iterator<ColorCounter> values = hashMap.values().iterator();
        ColorCounter[] colors = new ColorCounter[hashMap.values().size()];
        ColorCounter[] swaps = new ColorCounter[colors.length];
        
        int index = 0;
        while (values.hasNext()) {
            colors[index++] = values.next();
        }
        
        ColorCounter a, b, bestMatch;
        int rd, gd, bd, dif;
        
        for (int i = 0; i < colors.length; i++) {
            a = colors[i];
            bestMatch = a;
            int bestDif = Integer.MAX_VALUE;
            
            // Find best match in i+1 to length colors
            for (int j = i+1; j < colors.length; j++) {
                b = colors[j];
                rd = b.red-a.red;
                gd = b.green-a.green;
                bd = b.blue-a.blue;
                dif = Math.abs(rd) + Math.abs(gd) + Math.abs(bd);
                if (dif < 20) {
                    bestDif = dif;
                    bestMatch = b;
                    break;
                }
                if (dif < bestDif) {
                    bestDif = dif;
                    bestMatch = b;
                }
            }
            
            swaps[i] = bestMatch;
        }
        
        swap(colors, swaps);
    }
    
    private void swap(ColorCounter[] a, ColorCounter[] b) {
        HashMap<Integer, ColorCounter> map = new HashMap<Integer, ColorCounter>(a.length*2);
        for (int i = 0; i < a.length; i++) {
            a[i].depth = i;
            map.put(a[i].color, a[i]);
        }
        
        for (int y = height-1; y >= 0; y--) {
            for (int x = width-1; x >= 0; x--) {
                int col = image.getRGB(x, y);
                ColorCounter res = map.get(col);
                if (res != null) {
                    int depth = res.depth;
                    image.setRGB(x, y, b[depth].color);
                }
            }
        }
    }

    private void reduceImage(int x, int y, int w, int h, int depth) {
        if (depth > maxDepth) maxDepth = depth;
        if (w <= 1 && h <= 1) {
            int colour = image.getRGB(x, y);
            colorRectangles.add(new ColorRectangle(colour, x, y, 1, 1, depth));
            removeColourFromMask(colour, x, y, 1, 1);
            return;
        }

        int colour = findMostNumerousColour(image, x, y, w, h);
        colorRectangles.add(new ColorRectangle(colour, x, y, w, h, depth));
        removeColourFromMask(colour, x, y, w, h);

        // Find pockets of remaining colour
        ArrayList<ColorRectangle> pockets = decompose(x, y, w, h);
        
        for (int i = pockets.size() - 1; i >= 0; i--) {
            ColorRectangle cr = pockets.get(i);
            reduceImage(cr.x, cr.y, cr.w, cr.h, depth+1);
        }
    }

    private ArrayList<ColorRectangle> decompose(int xi, int yi, int w, int h) {
        ArrayList<ColorRectangle> rectangles = new ArrayList<ColorRectangle>(128);

        // Copy the mask and use the copy.
        for (int x = xi; x < xi+w; x++) {
            System.arraycopy(mask[x], yi, clone[x], yi, h);
        }

        ColorRectangle cr;
        for (int y = yi; y < yi + h; y++) {
            for (int x = xi; x < xi + w; x++) {
                if (!clone[x][y]) {
                    // Colour remains
                    cr = getRectangle(x, y, xi+w-x, yi+h-y, clone);
                    rectangles.add(cr);
                    for (int j = cr.y; j < cr.y + cr.h; j++) {
                        for (int i = cr.x; i < cr.x + cr.w; i++) {
                            clone[i][j] = true;
                        }
                    }
                }
            }
        }

        return rectangles;
    }

    private ColorRectangle getRectangle(int x, int y, int maxW, int maxH, boolean[][] cloneData) {
        int w = 1;
        int h = 1;
        
        // Find biggest square
        mainLoop:
        while (++w < maxW && ++h < maxH) {
            int xi = x+w-1;
            for (int yi = y; yi < y+h; yi++)
                if (cloneData[xi][yi]) break mainLoop;
            int yi = y+h-1;
            for (xi = x; xi < x+w-1; xi++)
                if (cloneData[xi][yi]) break mainLoop;
        }

        return new ColorRectangle(-1, x, y, w-1, h-1, -1);
    }

    private void removeColourFromMask(int colour, int xi, int yi, int w, int h) {
        for (int y = yi; y < yi + h; y++) {
            for (int x = xi; x < xi + w; x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == colour) {
                    mask[x][y] = true;
                }
            }
        }
    }

    @SuppressWarnings("unused")
	private static int getCloseColor(Iterator<ColorCounter> values, ColorCounter worst) {
        int color = worst.color;
        int closest = 10000;
        while (values.hasNext()) {
            ColorCounter cc = values.next();
            if (cc.color == worst.color) continue;
            int rd = cc.red - worst.red;
            int gd = cc.green - worst.green;
            int bd = cc.blue - worst.blue;
            int dif = rd*rd + gd*gd + bd*bd;
            if (dif < closest) {
                closest = dif;
                color = cc.color;
            }
        }
        return color;
    }

    @SuppressWarnings("unused")
	private static void swapColors(BufferedImage image, int colorA, int colorB, int xi, int yi, int w, int h) {
        //Swap worst with color
        for (int y = yi; y < yi+h; y++) {
            for (int x = xi; x < xi+w; x++) {
                int rgb = image.getRGB(x, y);
                if (rgb == colorA) {
                    image.setRGB(x, y, colorB);
                }
            }
        }
    }

    public class ColorRectangle {

        int colour, x, y, w, h, depth;

        public ColorRectangle(int colour, int x, int y, int width, int height, int depth) {
            this.colour = colour;
            this.x = x;
            this.y = y;
            this.w = width;
            this.h = height;
            this.depth = depth;
        }
    }
    
    private HashMap<Integer, ColorCounter> buildHashMap(BufferedImage img, int xi, int yi, int w, int h) {
        HashMap<Integer, ColorCounter> hashMap = new HashMap<Integer, ColorCounter>(1000);

        ColorCounter col, cc;
        int yij = 0;
        int colour = 0;
        int i, j;
        
        for (j = h-1; j >= 0; j--) {
            yij = yi + j;
            for (i = w-1; i >= 0; i--) {
                if (!mask[xi+i][yij]) {
                    colour = img.getRGB(xi+i, yij);
                    col = new ColorCounter(colour);
                    cc = hashMap.get(col.hash);
                    if (cc == null) {
                        hashMap.put(col.hash, col); 
                    } else {
                        cc.count++;
                    }
                }
            }
        }
        
        return hashMap;
    }
    
    private int findMostNumerousColour(BufferedImage img, int xi, int yi, int w, int h) {
        HashMap<Integer, ColorCounter> colourTable = buildHashMap(img, xi, yi, w, h);

        int highest = 0;
        ColorCounter best = null;
        Iterator<ColorCounter> values = colourTable.values().iterator();
        while (values.hasNext()) {
            ColorCounter cc = values.next();
            if (cc.count > highest) {
                highest = cc.count;
                best = cc;
            }
        }

        if (best == null) {
            return -1;
        }
        return best.color;
    }
    
    public class ColorCounter {
        int color;
        int count;
        int hash;
        int red;
        int green;
        int blue;
        int depth;
        
        public ColorCounter(int col) {
            color = col;
            count = 1;
            red = color >> 16 & 0xFF;
            green = color >> 8 & 0xFF;
            blue = color & 0xFF;
            hash = red*green*blue;
        }
        
        @Override
        public int hashCode() {
            return hash;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o.getClass() == ColorCounter.class)
                return (o.hashCode() == hashCode());
            return false;
        }
    }
}
