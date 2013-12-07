package sagma.core.model;

import javax.media.opengl.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import sagma.core.data.VertexBuffer;
import sagma.core.material.Material;
import sagma.core.material.SolidColor;
import sagma.core.material.Texture;
import sagma.core.math.*;

/**
Allows the user to build one model piece by piece in a very neat manner
The next step should be to set a texture, shader or both using 'setTexture(Texture)' and 'setShader(Shader)'
Not setting these will leave it as a default WHITE fill
Then use 'addTriangle(Triangle)' or 'addQuad(Quad)' to make triangles and quads.
You could also use 'add(Polygon)' to generically add to triangle or quad, which works just fine.
When done you SHOULD declare completion by using finalize(), if anything accesses the model BEFORE
it is finalized, it will be done automatically. Considering the entire model will be converted
and will use significant CPU time, it is sometimes wise to do this at loading time and not on the fly.
 */
public class ModelConstructor {
    public final static int VERTS_IN_TRIGS = 3;
    public final static int FLOATS_IN_VERTS = 3;
    public final static int FLOATS_IN_TEX = 2;
    private Vec3[] verticies;
    private Vec3[] normals;
    private Vec3[] texCoords;
    private ArrayList<Triangle> triangles;
    private ArrayList<Material> materials;
    private Material currentMaterial;
    private VertexBuffer buffer;
    private Model model;
    private String name;
    /** 
    Starts the process of creating a new model
     */
    public ModelConstructor(String name) {
        triangles = new ArrayList<Triangle>(128);
        materials = new ArrayList<Material>(128);
        this.name = name;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setOctreeDepth(int i) {
    	model.getOctree().MAX_DEPTH = i;
    }

    /**
    Sets the current texture to be used on each and every TRIG and QUAD created until changed.
    Use 'null' as a parameter to set it back to the default WHITE fill.
     */
    public void setMaterial(Material material) {
        currentMaterial = material;
        if (!materials.contains(material))
        	materials.add(material);
    }
    
    public void add(Triangle triangle) {
    	triangle.setMaterial(currentMaterial);
    	triangles.add(triangle);
    }
    
    public void add(Quad quad) {
    	// Decompose into two triangles
    	Vec3[] points = quad.getPoints();
    	Vec3[] normArray = quad.getNormals();
    	Vec3[] texArray = quad.getTexCoords();
    	
    	Vec3[] v1 = new Vec3[] { points[0], points[1], points[2] };
    	Vec3[] n1 = new Vec3[] { normArray[0], normArray[1], normArray[2] };
    	Vec3[] t1 = new Vec3[] { texArray[0], texArray[1], texArray[2] };
    	
    	Vec3[] v2 = new Vec3[] { points[0], points[2], points[3] };
    	Vec3[] n2 = new Vec3[] { normArray[0], normArray[2], normArray[3] };
    	Vec3[] t2 = new Vec3[] { texArray[0], texArray[2], texArray[3] };
    	
    	add(new Triangle(v1, n1, t1));
    	add(new Triangle(v2, n2, t2));
    }

    /**
    Converts any information given about the model and converts it into something that can be displayed.
    For tech-heads, so far the Model has been loading from the Hard Disk to the RAM,
    finalizing transfers the needed model information directly to the graphics card, allowing Very Fast display.
    The downside to this is that models can't be changed on the graphics card, and thus need to be re-finalized
    if edited.
    This finalize method is automatically called if the model is attempted to be accessed for display. Not calling
    this method at LOADING time may result in significant and spontanious lag and Popping (Images suddenly changing).
    Used at convenient times may make Loading unnecesary, so a lot of thought should be put in to when to finalize.
     */
    public void build() {
        compile();
    }

    /**
    @see finalize
    
    Converts the model into a vertex buffer object ready for fast display.
     */
    private void compile() {
        int size = 1 + materials.size();
        buffer = new VertexBuffer(size);
        int bufferCounter = 0;
        Material material = null;

        for (int i = -1; i < materials.size(); i++) {
            if (i != -1) {
                material = materials.get(i);
            } 
            int count = count(Triangle.class, material);
            int trigs = count * VERTS_IN_TRIGS * FLOATS_IN_VERTS;
            int texs = count * VERTS_IN_TRIGS * FLOATS_IN_TEX;

            float[] trigVerts = new float[trigs];
            float[] trigNorms = new float[trigs];
            
            float[] trigTexCoords = new float[texs];
            
            int trigCounter = 0;
            int texCounter = 0;

            for (int k = 0; k < triangles.size(); k++) {             
                Triangle triangle = triangles.get(k);
                if (triangle.getMaterial() != material) continue;

                Vec3[] points = triangle.getPoints();
                Vec3[] normArray = triangle.getNormals();
                Vec3[] texArray = triangle.getTexCoords();

                for (int j = 0; j < 3; j++) {
                    Vec3 v = points[j];
                    Vec3 n = normArray[j];
                    Vec3 t = texArray[j];
                    
                    trigNorms[trigCounter] = n.getX();
                    trigVerts[trigCounter++] = v.getX();
                    
                    trigNorms[trigCounter] = n.getY();
                    trigVerts[trigCounter++] = v.getY();
                    
                    trigNorms[trigCounter] = n.getZ();
                    trigVerts[trigCounter++] = v.getZ();
                    
                    trigTexCoords[texCounter++] = t.getX();
                    trigTexCoords[texCounter++] = t.getY();
                }
            }
            
            if (trigs > 0) {
                buffer.buildVertexBufferSet(bufferCounter, material, trigVerts, trigNorms, trigTexCoords);
                bufferCounter++;
            }
        }

        model = new Model(buffer);
    }

    private int count(Class<?> c, Material material) {
        int counter = 0;
        for (Triangle poly : triangles) {
            if (poly.getMaterial() == material && poly.getClass().equals(c)) {
                counter++;
            }
        }
        return counter;
    }
    
    public Model getModel() {
        if (model == null) {
            compile();
        }
        return model;
    }

    /**
     * Replaced with getModel()
     * 
     * @param drawable
     * @return
     */
    public Model getModel(GLAutoDrawable drawable) {
        if (model == null) {
            compile();
        }
        return model;
    }

    public void loadFromFile(String filename) {
        String[] lines = readAsStringArray(filename);
        
        String[] vertList = filter(lines, "v", ',');
        String[] normList = filter(lines, "vn", ',');
        String[] texCoordList = filter(lines, "vt", ',');
        String[] faceList = filter(lines, "f,usemtl", ',');
        //String[] matList = filter(lines, "usemtl", ',');

        buildPolygons(vertList, faceList, normList, texCoordList);
    }

    public void loadAndCompileFromFile(String filename) {
        loadFromFile(filename);
        compile();
    }

    private static float getFloat(String s) {
        return (new Float(s)).floatValue();
    }

    private static int getInt(String s) {
        return (new Integer(s)).intValue();
    }

    private static String[] readAsStringArray(String filename) {
        ArrayList<String> strings = new ArrayList<String>(128);
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(new File(filename)));
            String s = "";
            while (s != null) {
                if (!"".equals(s)) {
                    strings.add(s);
                }
                s = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
            return new String[1];
        }

        String[] lines = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            lines[i] = strings.get(i);
        }
        return lines;
    }

    /**
     * @param lines Lines to be filtered
     * @param filter String of filters, each filter seperated by a splitter token
     * @param splitter The seperating character between each filter
     *
     *	Filters an array of strings, only choosing lines that begin with the designated filter
     *
     * For example, the filter ("a,b,cd", ',') would choose lines such as:
     *   a monkey left the store.
     *   b a b y!
     *   cd -f .
     * But would not choose:
     *   ab b cd d a
     * Because "ab" was not in the filter.
     */
    private static String[] filter(String[] lines, String filter, char splitter) {
        StringTokenizer filters = new StringTokenizer(filter, "" + splitter);
        String[] searchStrings = new String[filters.countTokens()];
        for (int i = 0; i < searchStrings.length; i++) {
            searchStrings[i] = filters.nextToken();
        }
        ArrayList<String> stringList = new ArrayList<String>(lines.length);
        searchingLines:
        for (int i = 0; i < lines.length; i++) {
            StringTokenizer tokey = new StringTokenizer(lines[i]);
            if (tokey.countTokens() <= 0) {
                continue searchingLines;
            }
            String s1 = tokey.nextToken();
            for (int j = 0; j < searchStrings.length; j++) {
                String s2 = searchStrings[j];
                if (s1.equals(s2)) {
                    stringList.add(lines[i]);
                    continue searchingLines;
                }
            }
        }
        String[] newLines = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            newLines[i] = stringList.get(i);
        }
        return newLines;
    }

    /**
    Builds polygons using information coded in (Wavefront Object??) format.
     */
    public void buildPolygons(String[] vertexList, String[] faceList, String[] normList, String[] texCoordList) {
    	verticies = new Vec3[vertexList.length];
        normals = new Vec3[normList.length];
        texCoords = new Vec3[texCoordList.length];
    	
    	// Parse verticies
        for (int i = 0; i < vertexList.length; i++) {
            StringTokenizer vertTokey = new StringTokenizer(vertexList[i]);
            vertTokey.nextToken();
            Vec3 vertVector = new Vec3(getFloat(vertTokey.nextToken()), getFloat(vertTokey.nextToken()), getFloat(vertTokey.nextToken()));
            verticies[i] = vertVector;
        }

        // Parse normals
        for (int i = 0; i < normList.length; i++) {
            StringTokenizer normTokey = new StringTokenizer(normList[i]);
            normTokey.nextToken();
            Vec3 normVector = new Vec3(getFloat(normTokey.nextToken()), getFloat(normTokey.nextToken()), getFloat(normTokey.nextToken()));
            normals[i] = normVector;
        }
        
        // Parse texture coords
        for (int i = 0; i < texCoordList.length; i++) {
        	StringTokenizer texTokey = new StringTokenizer(texCoordList[i]);
    		texTokey.nextToken();
    		Vec3 texVector = new Vec3(getFloat(texTokey.nextToken()), getFloat(texTokey.nextToken()), 0.0f);
    		texCoords[i] = texVector;
        }

        // Parse faces
        for (int i = 0; i < faceList.length; i++) {
            StringTokenizer faceTokey = new StringTokenizer(faceList[i]);
            String command = faceTokey.nextToken();
            if (command.equals("f") && faceTokey.countTokens() >= 3) {
                // TRIG 'f vi/ti/ni ...'
                
                String comp1 = faceTokey.nextToken();
                String comp2 = faceTokey.nextToken();
                String comp3 = faceTokey.nextToken();
                
                StringTokenizer tokey1 = new StringTokenizer(comp1, "/");
                StringTokenizer tokey2 = new StringTokenizer(comp2, "/");
                StringTokenizer tokey3 = new StringTokenizer(comp3, "/");
                
                Vec3[] v = null;
                Vec3[] n = null;
                Vec3[] t = null;
                
                int v1 = getInt(tokey1.nextToken()) - 1;
                int v2 = getInt(tokey2.nextToken()) - 1;
                int v3 = getInt(tokey3.nextToken()) - 1;
                
                int t1 = 0, t2 = 0, t3 = 0;
                int n1 = 0, n2 = 0, n3 = 0;
                
                if (texCoordList.length > 0) {
	            	t1 = getInt(tokey1.nextToken()) - 1;
	            	t2 = getInt(tokey2.nextToken()) - 1;
	            	t3 = getInt(tokey3.nextToken()) - 1;
	            	t = new Vec3[] { texCoords[t1], texCoords[t2], texCoords[t3] };
                }
            	
                if (normList.length > 0) {
	        		n1 = getInt(tokey1.nextToken()) - 1;
	        		n2 = getInt(tokey2.nextToken()) - 1;
	        		n3 = getInt(tokey3.nextToken()) - 1;
	        		n = new Vec3[] { normals[n1], normals[n2], normals[n3] };
                }
	        		
        		v = new Vec3[] { verticies[v1], verticies[v2], verticies[v3] };
        		
        		add(new Triangle(v, n, t));
        		
        		if (faceTokey.hasMoreTokens()) {
        			String comp4 = faceTokey.nextToken();
        			StringTokenizer tokey4 = new StringTokenizer(comp4, "/");
        			
        			int v4 = getInt(tokey4.nextToken()) - 1;
        			
        			if (texCoordList.length > 0) {
        				int t4 = getInt(tokey4.nextToken()) - 1;
        				t = new Vec3[] { texCoords[t3], texCoords[t1], texCoords[t4] };
        			}
        			
        			if (normList.length > 0) {
        				int n4 = getInt(tokey4.nextToken()) - 1;
        				n = new Vec3[] { normals[n3], normals[n1], normals[n4] };
        			}
        				      			
        			v = new Vec3[] { verticies[v3], verticies[v1], verticies[v4] };
            		
            		add(new Triangle(v, n, t));
        		}
            } else if (command.equals("usemtl")) {
            	String n = faceTokey.nextToken();
            	setMaterial(Material.getMaterial(n));
            }
        }
    }

    public static Model makeModel(GLAutoDrawable drawable, String filename, int scale) {
        ModelConstructor modelMaker = new ModelConstructor(filename);
        modelMaker.loadFromFile(filename);
        modelMaker.build();
        Model m = modelMaker.getModel(drawable);
        m.setScale(new Vec3(scale, scale, scale));
        return m;
    }

    public static Instance makeInstance(GLAutoDrawable drawable, String filename, int scale) {
        ModelConstructor modelMaker = new ModelConstructor(filename);
        modelMaker.loadFromFile(filename);
        modelMaker.build();
        Model m = modelMaker.getModel(drawable);
        m.setScale(new Vec3(scale, scale, scale));
        return new Instance(m);
    }

    public static Model makeModel(GLAutoDrawable drawable, Constructable c) {
        ModelConstructor modelMaker = c.getModelConstructor();
        modelMaker.build();
        return modelMaker.getModel(drawable);
    }

    public static Instance makeInstance(GLAutoDrawable drawable, Constructable c) {
        ModelConstructor modelMaker = c.getModelConstructor();
        modelMaker.build();
        Model m = modelMaker.getModel(drawable);
        return new Instance(m);
    }
    
    public static Model makeSprite(GLAutoDrawable drawable, String filename) {
    	Material mat = Material.getMaterial(filename);
    	if (mat instanceof SolidColor) {
    		System.out.println("Failed to load: " + filename);
    		return null;
    	}
    	Texture texture = (Texture)mat;
    	return makeSprite(drawable, texture);
    }
    
    
    public static Model makeSprite(GLAutoDrawable drawable, Texture texture) {
    	ModelConstructor modelMaker = new ModelConstructor(texture.getName());
    	modelMaker.setMaterial(texture);
    	
    	int w = texture.getWidth();
    	int h = texture.getHeight();
    	float x = 1.0f;
    	float y = w * 1.0f / h;
    	
    	Vec3 pnts[] = new Vec3[] {
    			new Vec3(x, 0, -y),
    			new Vec3(x, 0, y),
    			new Vec3(-x, 0, y),
    			new Vec3(-x, 0, -y)
    	};
    	Vec3 norm = new Vec3(0, 1, 0);
    	Vec3 tex[] = new Vec3[] {
    			new Vec3(1, 0, 0),
    			new Vec3(0, 0, 0),
    			new Vec3(0, 1, 0),
    			new Vec3(1, 1, 0)
    	};
    	
    	Vec3[] pntsA = new Vec3[] { pnts[0], pnts[3], pnts[2] };
    	Vec3[] pntsB = new Vec3[] { pnts[0], pnts[1], pnts[2] };
    	Vec3[] normA = new Vec3[] { norm, norm, norm };
    	Vec3[] normB = new Vec3[] { norm, norm, norm };
    	Vec3[] texA = new Vec3[] { tex[0], tex[3], tex[2] };
    	Vec3[] texB = new Vec3[] { tex[0], tex[1], tex[2] };
    	
    	modelMaker.add(new Triangle(pntsA, normA, texA));
    	modelMaker.add(new Triangle(pntsB, normB, texB));
    	
    	modelMaker.build();
    	Model m = modelMaker.getModel(drawable);
    	m.setRotation(new Vec3(180, 270, 0));
    	return m;
    }
    
    public static Instance makeSpriteInstance(GLAutoDrawable drawable, String filename) {
    	Model m = makeSprite(drawable, filename);
    	return new Instance(m);
    }
    
    public Triangle[] getTriangles() {
    	Triangle[] trigs = new Triangle[triangles.size()];
    	for (int i = 0; i < triangles.size(); i++) {
    		trigs[i] = triangles.get(i);
    	}
    	return trigs;
    }
}
