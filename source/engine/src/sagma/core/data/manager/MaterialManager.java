package sagma.core.data.manager;

import java.io.IOException;
import java.util.ArrayList;

import sagma.core.data.Color4f;
import sagma.core.material.Material;
import sagma.core.material.Shader;
import sagma.core.material.SolidColor;
import sagma.core.material.Texture;

public class MaterialManager {
	private static ArrayList<Material> mats = new ArrayList<Material>();
	
	public static Material load(String filename) {
		for (Material mat : mats) {
			if (mat.getName().equals(filename))
				return mat;
		}
		
		try {
			if (Shader.isShader(filename)) {
				Shader shader = new Shader(filename);
				mats.add(shader);
				return shader;
			}
			if (Texture.isTexture(filename)) {
				Texture texture = new Texture(filename);
				mats.add(texture);
				return texture;
			}
		} catch (IOException e) {
			return null;
		}
		
		SolidColor color = new SolidColor(Color4f.WHITE);
		color.setName(filename);
		mats.add(color);
		return new SolidColor(Color4f.WHITE);
	}
}
