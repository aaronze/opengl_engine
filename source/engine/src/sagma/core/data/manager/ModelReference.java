package sagma.core.data.manager;

import sagma.core.model.Instance;
import sagma.core.model.Model;

public class ModelReference {
	private Instance instance;
	private String name;
	
	public ModelReference(String name, Instance instance) {
		this.name = name;
		this.instance = instance;
	}
	
	public String getName() {
		return name;
	}
	
	public void fillReference(Model model) {
		instance.setModel(model);
	}
}
