package sagma.games.rts.client;

import sagma.core.model.Instance;
import sagma.core.render.Game;
import sagma.core.render.Render;
import sagma.core.sound3d.Mixer3D;
import sagma.games.rts.entity.ship.CapitalShip;
import sagma.games.rts.entity.ship.Ship;

public class Player extends Instance {
	public Ship ship;
	private String username;
	private int ore;
	private int fuel;
	private int crystal;

	private PlayerMouseAdapter adapter;

	public Player(String username, Mixer3D sounds, Game game){
		setVisible(false);
		setPickable(false);
		this.username = username;
		this.ship = new CapitalShip(this, sounds);
		this.ore = 0;
		this.fuel = 0;
		this.crystal = 0;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getCrystal() {
		return crystal;
	}

	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}



	public String getUsername(){
		return username;
	}

	public void action(){
		if(adapter != null){
			adapter.action();
		}
	}

	public void addMouseAdapter(){
		this.adapter = new PlayerMouseAdapter(this);
		Render.addMouseListener(adapter);
		Render.addMouseWheelListener(adapter);
	}
	
	public PlayerMouseAdapter getMouseAdapter(){
		return adapter;
	}
	
	@Override
	public String toString(){
		return username;
	}

}
