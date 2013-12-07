package sagma.core.data.utility;

import java.io.File;
import java.util.ArrayList;

import sagma.core.data.structures.TriangleStrip;
import sagma.core.io.FileIO;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;
import sagma.core.model.ModelConstructor;

public class Conversion {
	public static void convertOBJtoCMF(String source, String dest) {
		ModelConstructor mc = new ModelConstructor(dest);
		mc.loadAndCompileFromFile(source);
		Triangle[] t = mc.getTriangles();
		ArrayList<Triangle> list = new ArrayList<Triangle>(t.length);
		for (Triangle trig : t) list.add(trig);
		
		TriangleStrip strip = TriangleStrip.convert(list);
		ArrayList<Vec3> vec = strip.getVertexArray();
		ArrayList<Vec3> norm = strip.getNormalArray();
		ArrayList<Vec3> tex = strip.getTexCoordArray();
		
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add("VERTEX_LIST " + vec.size());
		for (Vec3 v : vec) lines.add(v.x + " " + v.y + " " + v.z);
				
		lines.add("NORMAL_LIST " + norm.size());
		for (Vec3 v : norm) lines.add(v.x + " " + v.y + " " + v.z);
				
		lines.add("TEX_COORD_LIST " + tex.size());
		for (Vec3 v : tex) lines.add(v.x + " " + v.y);
				
		lines.add("MESH 0");
		lines.add("MATERIAL Texture/test.png");
		int size = vec.size();
		
		lines.add("INDEX " + size + " V");
		lines.add("0 .. " + size);
		lines.add("INDEX " + size + " N");
		lines.add("0 .. " + size);
		lines.add("INDEX " + size + " T");
		lines.add("0 .. " + size);
		
		FileIO.writeOver(new File(dest), lines);
	}
}
