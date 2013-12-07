package sagma.core.model;


import java.io.IOException;

import sagma.core.material.Texture;
import sagma.core.math.Triangle;
import sagma.core.math.Vec3;

/**
 * Creates a sky context by building a textured cube at a given point with a given size.
 * 
 * @author Aaron Kison
 *
 */

public class Sky implements Constructable {
	private ModelConstructor modelMaker;
	
	public Sky(Vec3 centre, float size, String rootFolder, String name, String ext) throws IOException {
		modelMaker = new ModelConstructor(name);
		
		Vec3[] points = new Vec3[] {
			new Vec3(centre.x+size, centre.y-size, centre.z-size),
			new Vec3(centre.x+size, centre.y-size, centre.z+size),
			new Vec3(centre.x-size, centre.y-size, centre.z+size),
			new Vec3(centre.x-size, centre.y-size, centre.z-size),
			new Vec3(centre.x+size, centre.y+size, centre.z-size),
			new Vec3(centre.x+size, centre.y+size, centre.z+size),
			new Vec3(centre.x-size, centre.y+size, centre.z+size),
			new Vec3(centre.x-size, centre.y+size, centre.z-size),
		};
		
		Vec3[] normals = new Vec3[] {
			new Vec3(0, 0, -1),
			new Vec3(-1, 0, 0),
			new Vec3(0, 0, 1),
			new Vec3(1, 0, 0),
			new Vec3(1, 0, 0),
			new Vec3(0, 1, 0),
			new Vec3(0, -1, 0)
		};
		
		Vec3[] tex = new Vec3[] {
			new Vec3(0, 0, 0), // 0
			new Vec3(1, 0, 0), // 1
			new Vec3(0, 1, 0), // 2
			new Vec3(1, 1, 0)  // 3
		};
		
		// Front Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_front" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[0], points[3]},
			new Vec3[] {normals[0], normals[0], normals[0]},
			new Vec3[] {tex[1], tex[3], tex[2]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[3], points[7]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[1], tex[2], tex[0]}));
		
		// Left Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_left" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[2], points[6], points[7]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[2], tex[0], tex[1]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[2], points[7], points[3]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[2], tex[1], tex[3]}));
		
		// Back Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_back" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[1], points[5], points[2]},
			new Vec3[] {normals[2], normals[2], normals[2]},
			new Vec3[] {tex[2], tex[0], tex[3]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[5], points[6], points[2]},
			new Vec3[] {normals[2], normals[2], normals[2]},
			new Vec3[] {tex[0], tex[1], tex[3]}));
		
		// Right Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_right" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[4], points[1]},
			new Vec3[] {normals[3], normals[3], normals[3]},
			new Vec3[] {tex[2], tex[0], tex[3]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[5], points[1]},
			new Vec3[] {normals[4], normals[4], normals[4]},
			new Vec3[] {tex[0], tex[1], tex[3]}));
		
		// Bottom Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_bottom" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[7], points[5]},
			new Vec3[] {normals[5], normals[5], normals[5]},
			new Vec3[] {tex[3], tex[2], tex[1]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[7], points[6], points[5]},
			new Vec3[] {normals[5], normals[5], normals[5]},
			new Vec3[] {tex[2], tex[0], tex[1]}));
		
		// Top Face
		modelMaker.setMaterial(new Texture(rootFolder + name + "_top" + ext));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[1], points[2]},
			new Vec3[] {normals[6], normals[6], normals[6]},
			new Vec3[] {tex[1], tex[3], tex[2]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[2], points[3]},
			new Vec3[] {normals[6], normals[6], normals[6]},
			new Vec3[] {tex[1], tex[2], tex[0]}));

		modelMaker.build();
	}

	public Sky(Vec3 centre, float size) throws IOException {
		modelMaker = new ModelConstructor("sky");
		
		Vec3[] points = new Vec3[] {
			new Vec3(centre.x+size, centre.y-size, centre.z-size),
			new Vec3(centre.x+size, centre.y-size, centre.z+size),
			new Vec3(centre.x-size, centre.y-size, centre.z+size),
			new Vec3(centre.x-size, centre.y-size, centre.z-size),
			new Vec3(centre.x+size, centre.y+size, centre.z-size),
			new Vec3(centre.x+size, centre.y+size, centre.z+size),
			new Vec3(centre.x-size, centre.y+size, centre.z+size),
			new Vec3(centre.x-size, centre.y+size, centre.z-size),
		};
		
		Vec3[] normals = new Vec3[] {
			new Vec3(0, 0, -1),
			new Vec3(-1, 0, 0),
			new Vec3(0, 0, 1),
			new Vec3(1, 0, 0),
			new Vec3(1, 0, 0),
			new Vec3(0, 1, 0),
			new Vec3(0, -1, 0)
		};
		
		Vec3[] tex = new Vec3[] {
			new Vec3(0, 0, 0), // 0
			new Vec3(1, 0, 0), // 1
			new Vec3(0, 1, 0), // 2
			new Vec3(1, 1, 0)  // 3
		};
		
		// Front Face
		modelMaker.setMaterial(new Texture("Texture/alpine_south.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[0], points[3]},
			new Vec3[] {normals[0], normals[0], normals[0]},
			new Vec3[] {tex[1], tex[3], tex[2]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[3], points[7]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[1], tex[2], tex[0]}));
		
		// Left Face
		modelMaker.setMaterial(new Texture("Texture/alpine_east.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[2], points[6], points[7]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[2], tex[0], tex[1]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[2], points[7], points[3]},
			new Vec3[] {normals[1], normals[1], normals[1]},
			new Vec3[] {tex[2], tex[1], tex[3]}));
		
		// Back Face
		modelMaker.setMaterial(new Texture("Texture/alpine_north.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[1], points[5], points[2]},
			new Vec3[] {normals[2], normals[2], normals[2]},
			new Vec3[] {tex[2], tex[0], tex[3]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[5], points[6], points[2]},
			new Vec3[] {normals[2], normals[2], normals[2]},
			new Vec3[] {tex[0], tex[1], tex[3]}));
		
		// Right Face
		modelMaker.setMaterial(new Texture("Texture/alpine_west.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[4], points[1]},
			new Vec3[] {normals[3], normals[3], normals[3]},
			new Vec3[] {tex[2], tex[0], tex[3]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[5], points[1]},
			new Vec3[] {normals[4], normals[4], normals[4]},
			new Vec3[] {tex[0], tex[1], tex[3]}));
		
		// Bottom Face
		modelMaker.setMaterial(new Texture("Texture/alpine_up.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[4], points[7], points[5]},
			new Vec3[] {normals[5], normals[5], normals[5]},
			new Vec3[] {tex[3], tex[2], tex[1]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[7], points[6], points[5]},
			new Vec3[] {normals[5], normals[5], normals[5]},
			new Vec3[] {tex[2], tex[0], tex[1]}));
		
		// Top Face
		modelMaker.setMaterial(new Texture("Texture/alpine_down.bmp"));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[1], points[2]},
			new Vec3[] {normals[6], normals[6], normals[6]},
			new Vec3[] {tex[1], tex[3], tex[2]}));
		modelMaker.add(new Triangle(
			new Vec3[] {points[0], points[2], points[3]},
			new Vec3[] {normals[6], normals[6], normals[6]},
			new Vec3[] {tex[1], tex[2], tex[0]}));

		modelMaker.build();
	}

	@Override
	public ModelConstructor getModelConstructor() {
		return modelMaker;
	}
}