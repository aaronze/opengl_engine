package sagma.games.rts.entity.satellite;

import sagma.games.rts.RTS;
import sagma.games.rts.client.Player;
import sagma.games.rts.emitters.Entity;
import sagma.games.rts.entity.cellestial.Celestial;

public class MiningStation extends Entity {

	Player owner;
	Celestial planet;
	
	public MiningStation(Player owner, Celestial planet) {
		super(RTS.resources + "spaceship_asset.png"); //TODO placeholder
		this.owner = owner;
		this.planet = planet;
		RTS.soundSystem.playOnce(RTS.soundCollection.get("upgrade"));
	}

	@Override
	public void heartbeat(){
		mine(1);
	}
	
	private void mine(int amount){
		if(amount <= 0){
			return;
		}
		if(planet.getOre() > amount){
			planet.setOre(planet.getOre() - amount);
			owner.setOre(owner.getOre()+amount);
		}
		else{
			owner.setOre(owner.getOre()+planet.getOre());
			planet.setOre(0);
		}
		
		if(planet.getFuel() > amount){
			planet.setFuel(planet.getFuel() - amount);
			owner.setFuel(owner.getFuel()+amount);
		}
		else{
			owner.setFuel(owner.getFuel()+planet.getFuel());
			planet.setFuel(0);
		}
		
		if(planet.getCrystal() > amount){
			planet.setCrystal(planet.getCrystal() - amount);
			owner.setCrystal(owner.getCrystal()+amount);
		}
		else{
			owner.setCrystal(owner.getCrystal()+planet.getCrystal());
			planet.setCrystal(0);
		}
		
		
		
			
	}
	
}
