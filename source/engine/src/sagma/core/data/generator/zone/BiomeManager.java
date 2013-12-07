package sagma.core.data.generator.zone;

import sagma.core.data.generator.zone.biome.*;

public class BiomeManager {
	public static BiomeType plains = new Plains();
	public static BiomeType hills = new Hills();
	public static BiomeType forest = new Forest();
	public static BiomeType desert = new Desert();
	public static BiomeType snow = new Snow();
	public static BiomeType ice = new Ice();
	public static BiomeType river = new River();
	public static BiomeType lake = new Lake();
	public static BiomeType village = new Village();
	public static BiomeType town = new Town();
	public static BiomeType city = new City();
	public static BiomeType lava = new Lava();
	public static BiomeType ocean = new Ocean();
	public static BiomeType field = new Field();
	public static BiomeType nomad = new Nomad();
	public static BiomeType beach = new Beach();
	public static BiomeType dunes = new Dunes();
	public static BiomeType volcano = new Volcano();
	public static BiomeType mountains = new Mountains();
	public static BiomeType crater = new Crater();
	
	{
		plains.buildDependencies();
		hills.buildDependencies();
		forest.buildDependencies();
		desert.buildDependencies();
		snow.buildDependencies();
		ice.buildDependencies();
		river.buildDependencies();
		lake.buildDependencies();
		village.buildDependencies();
		town.buildDependencies();
		city.buildDependencies();
		lava.buildDependencies();
		ocean.buildDependencies();
		field.buildDependencies();
		nomad.buildDependencies();
		beach.buildDependencies();
		dunes.buildDependencies();
		volcano.buildDependencies();
		mountains.buildDependencies();
		crater.buildDependencies();
	}
}
