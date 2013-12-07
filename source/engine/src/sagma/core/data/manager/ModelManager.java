package sagma.core.data.manager;

import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GLAutoDrawable;

import sagma.core.model.Model;
import sagma.core.model.ModelConstructor;
import sagma.core.render.Game;

public class ModelManager {
	private static HashMap<String,Model> loaded = new HashMap<String,Model>();
	private static ArrayList<ModelConstructor> constructs = new ArrayList<ModelConstructor>();
	private static ArrayList<String> waiting = new ArrayList<String>();
	private static ArrayList<ModelReference> references = new ArrayList<ModelReference>();
	
	public static void loadLater(String name) {
		if (!loaded.containsKey(name) && !waiting.contains(name))
			waiting.add(name);
	}
	
	public static void loadLater(ModelConstructor construct) {
		if (!constructs.contains(construct))
			constructs.add(construct);
	}
	
	public static void loadNow(GLAutoDrawable drawable, String name) {
		loaded.put(name, Game.makeModel(drawable, name));
	}
	
	public static void loadNext(GLAutoDrawable drawable) {
		String name = "";
		Model model = null;
		
		if (constructs.size() > 0) {
			ModelConstructor mc = constructs.remove(0);
			name = mc.getName();
			model = mc.getModel(drawable);
		} else {
			name = waiting.remove(waiting.size()-1);
			model = Game.makeModel(drawable, name);
		}
		
		System.out.println("LOAD: " + name);
		for (int i = references.size()-1; i >= 0; i--) {
			ModelReference ref = references.get(i);
			System.out.println("REF: " + ref.getName());
			if (ref.getName().equals(name)) {
				if (model == null) break;
				ref.fillReference(model.getClone());
				references.remove(ref);
			}
		}
	}
	
	public static void loadAll(GLAutoDrawable drawable) {
		while (hasMoreToLoad()) loadNext(drawable);
	}
	
	public static boolean hasMoreToLoad() {
		return waiting.size() > 0;
	}
	
	public static Model get(String name) {
		return loaded.get(name);
	}
	
	public static void addReference(ModelReference ref) {
		references.add(ref);
	}
	
	public static void add(String name, Model model) {
		loaded.put(name, model);
	}
}
