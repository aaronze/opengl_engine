package sagma.games.rts.entity.cellestial;

import sagma.core.model.Constructable;
import sagma.core.model.Model;
import sagma.core.render.Game;
import sagma.core.render.Render;
import sagma.games.rts.InstanceContainer;
import sagma.games.rts.client.Player;
import sagma.games.rts.emitters.Entity;
import sagma.games.rts.emitters.GlobalEmitter;
import sagma.games.rts.entity.satellite.MiningStation;

public abstract class Celestial extends Entity{

	private int fuel;
	private int ore;
	private int crystal;
	
	private GlobalEmitter globalEmitter;
	private MiningStation mine;
	
	public Celestial(Constructable c) {
		super(c);
		this.fuel = 0;
		this.ore = 0;
		this.crystal = 0;
		this.globalEmitter = new GlobalEmitter(this);
		this.mine = null;
	}
	
	public Celestial(Model m) {
		super(m);
		this.fuel = 0;
		this.ore = 0;
		this.crystal = 0;
		this.globalEmitter = new GlobalEmitter(this);
		this.mine = null;
	}
	
	public Celestial(String m) {
		super(m);
		this.fuel = 0;
		this.ore = 0;
		this.crystal = 0;
		this.globalEmitter = new GlobalEmitter(this);
		this.mine = null;
	}
	
	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getCrystal() {
		return crystal;
	}

	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	
	public void addMiningStation(Player p){
		Render.remove(mine);
		this.mine = new MiningStation(p, this);
		Game.add(mine);
	}
	
	/**
	 * This should be removed when automatic resource chunks get added
	 * @param amount
	 */
	public void addAsteroids(int amount){
		globalEmitter.add("Moons", new InstanceContainer(Planet.class, amount));
	}
	
	@Override
	public void heartbeat() {
		super.heartbeat();
		if (!isAlive()) return;
		if(ore == 0 && fuel == 0 && crystal == 0){
			destroy();
		}
	}
	
	@Override
	public void destroy(){
		globalEmitter.pulse();
		Render.remove(globalEmitter);
		Render.remove(mine);
		mine = null;
		globalEmitter = null;
	}
	
}
