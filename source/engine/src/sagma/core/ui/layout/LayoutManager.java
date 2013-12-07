package sagma.core.ui.layout;

import sagma.core.ui.UIContainer;

public abstract class LayoutManager {
	protected UIContainer container;
	
	public abstract void update();
	
	public LayoutManager(UIContainer container) {
		this.container = container;
	}
}
