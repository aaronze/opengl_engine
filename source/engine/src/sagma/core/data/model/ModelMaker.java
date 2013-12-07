package sagma.core.data.model;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import sagma.core.io.FileIO;
import sagma.core.material.Material;
import sagma.core.math.Vec3;

public class ModelMaker {
	
	public static Model load(String filename) {
		String ext = filename.substring(filename.lastIndexOf(".")+1, filename.length());
		
		if (ext.equalsIgnoreCase("CMF")) return loadCMF(filename);
		if (ext.equalsIgnoreCase("OBJ")) return loadOBJ(filename);
		return null;
	}
	
	public static Model loadOBJ(String filename) {
		ArrayList<String> lines = FileIO.read(new File(filename));
		
		int vCounter = 0, nCounter = 0, tCounter = 0;
		int vICounter = 0, nICounter = 0, tICounter = 0;
		int vSize = 0, nSize = 0, tSize = 0, iSize = 0;
		Model model = new Model();
		
		for (String line : lines) {
			StringTokenizer tokey = new StringTokenizer(line, " ");
			String command = tokey.nextToken();
			
			if (command.equalsIgnoreCase("v")) {
				vSize++;
			} 
			else if (command.equalsIgnoreCase("vn")) {
				nSize++;
			} 
			else if (command.equalsIgnoreCase("vt")) {
				tSize++;
			} 
			else if (command.equalsIgnoreCase("f")) {
				iSize++;
			}
		}
		
		iSize *= 3;
		
		Vec3[] verticies = new Vec3[vSize];
		Vec3[] normals = new Vec3[nSize];
		Vec3[] texCoords = new Vec3[tSize];
		int[] vertIndex = new int[iSize];
		int[] normIndex = new int[iSize];
		int[] texIndex = new int[iSize];
		Mesh mesh = null;
		
		for (String line : lines) {
			StringTokenizer tokey = new StringTokenizer(line, " ");
			String command = tokey.nextToken();
			
			if (command.equalsIgnoreCase("v")) {
				float x = toFloat(tokey.nextToken());
				float y = toFloat(tokey.nextToken());
				float z = toFloat(tokey.nextToken());
				verticies[vCounter++] = new Vec3(x, y, z);
			} 
			else if (command.equalsIgnoreCase("vn")) {
				float x = toFloat(tokey.nextToken());
				float y = toFloat(tokey.nextToken());
				float z = toFloat(tokey.nextToken());
				normals[nCounter++] = new Vec3(x, y, z);
			} 
			else if (command.equalsIgnoreCase("vt")) {
				float x = toFloat(tokey.nextToken());
				float y = toFloat(tokey.nextToken());
				texCoords[tCounter++] = new Vec3(x, y, 0);
			}
			else if (command.equalsIgnoreCase("usemtl")) {
				Material mat = Material.getMaterial(tokey.nextToken());
				
				if (mesh != null) {
					mesh.addBuffer(PolygonBuffer.buffer(verticies, normals, texCoords, vertIndex, normIndex, texIndex));
					verticies = new Vec3[vSize];
					normals = new Vec3[nSize];
					texCoords = new Vec3[tSize];
					vertIndex = new int[iSize];
					normIndex = new int[iSize];
					texIndex = new int[iSize];
					vCounter = 0;
					nCounter = 0;
					tCounter = 0;
					model.add(mesh);
					mesh = null;
				}
				
				mesh = new Mesh(mat);
			}
			else if (command.equalsIgnoreCase("f")) {
				if (mesh == null) {
					mesh = new Mesh(Material.getMaterial("Texture/test.png"));
				}
				if (tokey.countTokens() >= 3) {
					String comp1 = tokey.nextToken();
					String comp2 = tokey.nextToken();
					String comp3 = tokey.nextToken();
					
					StringTokenizer tok1 = new StringTokenizer(comp1, "/");
					StringTokenizer tok2 = new StringTokenizer(comp2, "/");
					StringTokenizer tok3 = new StringTokenizer(comp3, "/");
					
					vertIndex[vICounter++] = toInt(tok1.nextToken())-1;
					if (tok1.hasMoreTokens()) texIndex[tICounter++] = toInt(tok1.nextToken())-1;
					if (tok1.hasMoreTokens()) normIndex[nICounter++] = toInt(tok1.nextToken())-1;
					
					vertIndex[vICounter++] = toInt(tok2.nextToken())-1;
					if (tok1.hasMoreTokens()) texIndex[tICounter++] = toInt(tok2.nextToken())-1;
					if (tok1.hasMoreTokens()) normIndex[nICounter++] = toInt(tok2.nextToken())-1;
					
					vertIndex[vICounter++] = toInt(tok3.nextToken())-1;
					if (tok1.hasMoreTokens()) texIndex[tICounter++] = toInt(tok3.nextToken())-1;
					if (tok1.hasMoreTokens()) normIndex[nICounter++] = toInt(tok3.nextToken())-1;
				}
			}
		}
		
		if (mesh != null) {
			mesh.addBuffer(PolygonBuffer.buffer(verticies, normals, texCoords, vertIndex, normIndex, texIndex));
			model.add(mesh);
		}
		
		return model;
	}
	
	@SuppressWarnings("null")
	public static Model loadCMF(String filename) {
		ArrayList<String> lines = FileIO.read(new File(filename));
		
		Vec3[] verticies = null;
		Vec3[] normals = null;
		Vec3[] texCoords = null;
		int[] vertIndex = null;
		int[] normIndex = null;
		int[] texIndex = null;
		
		int type = -1;
		int vCounter = 0, nCounter = 0, tCounter = 0;
		final int VERTEX_LIST = 0;
		final int NORMAL_LIST = 1;
		final int TEX_COORD_LIST = 2;
		final int VERTEX_INDEX = 3;
		final int NORMAL_INDEX = 4;
		final int TEX_COORD_INDEX = 5;
		
		Mesh mesh = null;
		Model model = new Model();
		
		for (String line : lines) {
			StringTokenizer tokey = new StringTokenizer(line, " ");
			String command = tokey.nextToken();
			
			if (command.equalsIgnoreCase("VERTEX_LIST")) {
				int verts = toInt(tokey.nextToken());
				verticies = new Vec3[verts];
				vCounter = 0;
				type = VERTEX_LIST;
			} 
			else if (command.equalsIgnoreCase("NORMAL_LIST")) {
				int norms = toInt(tokey.nextToken());
				normals = new Vec3[norms];
				nCounter = 0;
				type = NORMAL_LIST;
			}
			else if (command.equalsIgnoreCase("TEX_COORD_LIST")) {
				int texs = toInt(tokey.nextToken());
				texCoords = new Vec3[texs];
				tCounter = 0;
				type = TEX_COORD_LIST;
			}
			else if (command.equalsIgnoreCase("INDEX")) {
				int size = toInt(tokey.nextToken());
				String flag = tokey.nextToken();
				if (flag.equalsIgnoreCase("N")) {
					normIndex = new int[size];
					nCounter = 0;
					type = NORMAL_INDEX;
				} else if (flag.equalsIgnoreCase("T")) {
					texIndex = new int[size];
					tCounter = 0;
					type = TEX_COORD_INDEX;
				} else {
					vertIndex = new int[size];
					vCounter = 0;
					type = VERTEX_INDEX;
				}
			}
			else if (command.equalsIgnoreCase("MESH")) {
				if (mesh != null) {
					PolygonBuffer buffer = PolygonBuffer.buffer(verticies, normals, texCoords, vertIndex, normIndex, texIndex);
					mesh.addBuffer(buffer);
					model.add(mesh);
					mesh = null;
				}
			} 
			else if (command.equalsIgnoreCase("MATERIAL")) {
				String name = tokey.nextToken();
				while (tokey.hasMoreTokens()) name += " " + tokey.nextToken(); 
				
				Material mat = Material.getMaterial(name);
				mesh = new Mesh(mat);
			}
			else {
				if (type == VERTEX_LIST || type == NORMAL_LIST) {
					float x = toFloat(command);
					float y = toFloat(tokey.nextToken());
					float z = toFloat(tokey.nextToken());
					
					if (type == VERTEX_LIST) verticies[vCounter++] = new Vec3(x, y, z);
					else normals[nCounter++] = new Vec3(x, y, z);
					
					while (tokey.hasMoreTokens()) {
						x = toFloat(tokey.nextToken());
						y = toFloat(tokey.nextToken());
						z = toFloat(tokey.nextToken());
						
						if (type == VERTEX_LIST) verticies[vCounter++] = new Vec3(x, y, z);
						else normals[nCounter++] = new Vec3(x, y, z);
					}
				}
				else if (type == TEX_COORD_LIST) {
					float x = toFloat(command);
					float y = toFloat(tokey.nextToken());
					
					texCoords[tCounter++] = new Vec3(x, y, 0);
					
					while (tokey.hasMoreTokens()) {
						x = toFloat(tokey.nextToken());
						y = toFloat(tokey.nextToken());
						
						texCoords[tCounter++] = new Vec3(x, y, 0);
					}
				}
				else if (type == VERTEX_INDEX || type == NORMAL_INDEX || type == TEX_COORD_INDEX) {
					if (command.equalsIgnoreCase("..")) {
						int index = toInt(tokey.nextToken());
						int prevIndex = 0;
						if (type == VERTEX_INDEX) prevIndex = vertIndex[vCounter-1];
						else if (type == NORMAL_INDEX) prevIndex = normIndex[nCounter-1];
						else prevIndex = texIndex[tCounter-1];
						for (int i = prevIndex+1; i < index; i++) {
							if (type == VERTEX_INDEX) vertIndex[vCounter++] = i;
							else if (type == NORMAL_INDEX) normIndex[nCounter++] = i;
							else texIndex[tCounter++] = i;
						}
					} else {
						int index = toInt(command);
						
						if (type == VERTEX_INDEX) vertIndex[vCounter++] = index;
						else if (type == NORMAL_INDEX) normIndex[nCounter++] = index;
						else texIndex[tCounter++] = index;
						
						while (tokey.hasMoreTokens()) {
							String token = tokey.nextToken();
							if (token.equalsIgnoreCase("..")) {
								index = toInt(tokey.nextToken());
								int prevIndex = 0;
								if (type == VERTEX_INDEX) prevIndex = vertIndex[vCounter-1];
								else if (type == NORMAL_INDEX) prevIndex = normIndex[nCounter-1];
								else prevIndex = texIndex[tCounter-1];
								for (int i = prevIndex+1; i < index; i++) {
									if (type == VERTEX_INDEX) vertIndex[vCounter++] = i;
									else if (type == NORMAL_INDEX) normIndex[nCounter++] = i;
									else texIndex[tCounter++] = i;
								}
							} else {
								index = toInt(tokey.nextToken());
								
								if (type == VERTEX_INDEX) vertIndex[vCounter++] = index;
								else if (type == NORMAL_INDEX) normIndex[nCounter++] = index;
								else texIndex[tCounter++] = index;
							}
						}
					}
				}
			}
		}
		
		if (mesh != null) {
			PolygonBuffer buffer = PolygonBuffer.buffer(verticies, normals, texCoords, vertIndex, normIndex, texIndex);
			mesh.addBuffer(buffer);
			model.add(mesh);
			mesh = null;
		}
		
		return model;
	}
	
	private final static float toFloat(String s) {return new Float(s).floatValue();}
	private final static int toInt(String s) {return new Integer(s).intValue();}
}
