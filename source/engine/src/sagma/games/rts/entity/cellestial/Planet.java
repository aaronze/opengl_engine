package sagma.games.rts.entity.cellestial;

import java.awt.event.MouseEvent;

import sagma.core.data.generator.array.ConstantArrayGenerator;
import sagma.core.data.generator.field.ArrayFieldGenerator;
import sagma.core.data.generator.planetoid.DirectedFieldPlanetoidGenerator;
import sagma.core.data.generator.planetoid.RandomPlanetoidGenerator;
import sagma.core.event.DelayedEvent;
import sagma.core.event.WildcardEvent;
import sagma.core.material.Material;
import sagma.core.model.Model;
import sagma.core.model.ModelConstructor;
import sagma.core.render.Render;
import sagma.games.rts.RTS;
import sagma.games.rts.sfx.MeshExploder;

public class Planet extends Celestial {
	public Planet(Material material) {
		super(new DirectedFieldPlanetoidGenerator("name",
				new ArrayFieldGenerator(new ConstantArrayGenerator(1, 100, 100).nextArray2D()), 
				material, 6).getModelConstructor().getModel());
		setLocation((float)Math.random()*20-10, (float)Math.random()*20-10, 1.0f);
		addRotationalSpeed(0, 0.1f, 0);
		setFuel(2000);
		setOre(2000);
		setCrystal(2000);
		this.setPickable(MouseEvent.BUTTON3, true);
	}
	
	public Planet(String name) {
		super(name);
		addRotationalSpeed(0, 0.1f, 0);
		setFuel(2000);
		setOre(2000);
		setCrystal(2000);
		this.setPickable(MouseEvent.BUTTON3, true);
	}
	
	public static ModelConstructor getTemplate() {
		return new RandomPlanetoidGenerator("name", 1.0f, 1.0f).getModelConstructor();
	}
	
	public Planet(Model m) {
		super(m);
		setLocation((float)Math.random()*20-10, (float)Math.random()*20-10, 1.0f);
		addRotationalSpeed(0, 0.1f, 0);
		setFuel(2000);
		setOre(2000);
		setCrystal(2000);
		this.setPickable(MouseEvent.BUTTON3, true);
	}
	
	@Override
	public void destroy() {
		setSolid(false);
		new DelayedEvent(2000, new WildcardEvent() {
			@Override
			public void eventRecieved() {
				Render.remove(this);
			}
		});
		new MeshExploder().explode(this);
		super.destroy();
		RTS.soundSystem.playOnce(RTS.soundCollection.get("implode"));
	}
	
}
