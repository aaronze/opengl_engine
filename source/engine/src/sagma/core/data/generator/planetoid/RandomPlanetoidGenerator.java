package sagma.core.data.generator.planetoid;

import sagma.core.data.generator.number.ConstantNumberGenerator;
import sagma.core.data.generator.number.NumberGenerator;
import sagma.core.data.generator.number.RandomNumberGenerator;
import sagma.core.render.Game;

public class RandomPlanetoidGenerator extends PlanetoidGenerator  {
	private NumberGenerator number;
	
	public RandomPlanetoidGenerator(String name, float f) {
		this(name, new ConstantNumberGenerator(f));
	}
	
	public RandomPlanetoidGenerator(String name, float min, float max) {
		this(name, new RandomNumberGenerator(min, max));
	}
	
	public RandomPlanetoidGenerator(String name, float f, int divisions) {
		this(name, new ConstantNumberGenerator(f), divisions);
	}
	
	public RandomPlanetoidGenerator(String name, float min, float max, int divisions) {
		this(name, new RandomNumberGenerator(min, max), divisions);
	}
	
	public RandomPlanetoidGenerator(String name, NumberGenerator num) {
		super(name);
		number = num;
		generateNextPlanetoid();
		if (Game.savedDrawable != null) build();
	}
	
	public RandomPlanetoidGenerator(String name, NumberGenerator num, int divisions) {
		super(name, divisions);
		number = num;
		generateNextPlanetoid();
		if (Game.savedDrawable != null) build();
	}
	
	@Override
	protected void modify() {
		for (int i = index-1; i >= 0; i--) {
			verts[i] = verts[i].normalize().scale(radius).scale(number.nextNumber());
		}
	}

}
