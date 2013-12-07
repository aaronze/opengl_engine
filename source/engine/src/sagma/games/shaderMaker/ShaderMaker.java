package sagma.games.shaderMaker;

import java.io.File;
import java.util.ArrayList;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.data.Color4f;
import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.io.FileIO;
import sagma.core.material.Material;
import sagma.core.model.Instance;
import sagma.core.render.Game;

public class ShaderMaker extends Game {
	private Instance sphere;
	
	private String name;
	
	private Color4f base;
	private Color4f diffuse;
	private int specularIntensity = 32;

	@Override
	public void init(GLAutoDrawable drawable) {
		setGameMode(GAMEMODE_3D);
		setEscapeKeyToExit();
		
		setCameraLocation(0, 0, -4);
		
		sphere = make(new DirectedFieldPlanetoidGenerator("SKY",Material.getMaterial("Shaders/sky")));
	}
	
	public void setWorkingMaterial(Material m) {
		remove(sphere);
		sphere = make(new DirectedFieldPlanetoidGenerator("MAT",m));
	}
	
	public void writeOutShader() {
		File vertexShader = new File(name + ".vert");
		File fragmentShader = new File(name + ".frag");
		
		// Vertex Shader
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("varying vec3 position;");
		lines.add("varying float NdotL;");
		lines.add("varying vec3 ReflectVec;");
		lines.add("varying vec3 ViewVec;");
		lines.add("void main () {");
		lines.add("\tposition = gl_Vertex.xyz");
		lines.add("\tvec3 ecPos		= vec3(gl_ModelViewMatrix * gl_Vertex);");
		lines.add("\tvec3 tnorm		= normalize(gl_NormalMatrix * gl_Normal);");
		lines.add("\tvec3 lightVec	= normalize(gl_LightSource[0].position.xyz - ecPos);");
		lines.add("\tReflectVec		= normalize(reflect(-lightVec, tnorm));");
		lines.add("\tViewVec			= normalize(-ecPos);");
		lines.add("\tNdotL			= (dot(lightVec, tnorm) + 1.0) * 0.5;");
		lines.add("\tgl_Position		= ftransform();");
		lines.add("}");
		
		FileIO.writeOver(vertexShader, lines);
		
		// Fragment Shader
		lines = new ArrayList<String>();
		lines.add("varying vec3 position;");
		lines.add("void main()");
		lines.add("{");
		writeBase(lines);
		writeDiffuse(lines);
		writeSpecular(lines);
		// TODO:Add highlights
		// TODO:Add transparency
		// TODO:Add reflectivity
		// TODO:Add soft light
		lines.add("}");
		FileIO.writeOver(fragmentShader, lines);
	}
	
	public void writeBase(ArrayList<String> lines) {
		lines.add("float BASE = vec3(" + base.getRed() + ", " + base.getGreen() + ", " 
				+ base.getBlue() + ")");
	}
	
	public void writeDiffuse(ArrayList<String> lines) {
		lines.add("float DIFFUSE = vec3(" + diffuse.getRed() + ", " + diffuse.getGreen() + ", " 
				+ diffuse.getBlue() + ")");
	}
	
	public void writeSpecular(ArrayList<String> lines) {
		lines.add("float SPECULAR = max(dot(nreflect, nview), 0.0);");
		lines.add("SPECULAR = pow(SPECULAR, " + specularIntensity + ");");
	}

}
