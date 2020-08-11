package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

public class FightArena {

	private String name;
	private World world;
	private Location firstBase;
	private Location[] firstEntity;
	private Location secondBase;
	private Location[] secondEntity;
	
	public FightArena(String name, ConfigurationSection config) throws Exception {
		this.name = name;
		this.world = Bukkit.getWorld(config.getString("world"));
		this.firstBase = ((Vector) config.get("first.base")).toLocation(world, 0, 0);
		this.firstEntity = new Location[5];
		this.firstEntity[0] = ((Vector) config.get("first.entity1")).toLocation(world, 0, 0);
		this.firstEntity[1] = ((Vector) config.get("first.entity2")).toLocation(world, 0, 0);
		this.firstEntity[2] = ((Vector) config.get("first.entity3")).toLocation(world, 0, 0);
		this.firstEntity[3] = ((Vector) config.get("first.entity4")).toLocation(world, 0, 0);
		this.firstEntity[4] = ((Vector) config.get("first.entity5")).toLocation(world, 0, 0);
		this.secondBase = ((Vector) config.get("second.base")).toLocation(world, 180, 0);
		this.secondEntity = new Location[5];
		this.secondEntity[0] = ((Vector) config.get("second.entity1")).toLocation(world, 180, 0);
		this.secondEntity[1] = ((Vector) config.get("second.entity2")).toLocation(world, 180, 0);
		this.secondEntity[2] = ((Vector) config.get("second.entity3")).toLocation(world, 180, 0);
		this.secondEntity[3] = ((Vector) config.get("second.entity4")).toLocation(world, 180, 0);
		this.secondEntity[4] = ((Vector) config.get("second.entity5")).toLocation(world, 180, 0);
		String errorNull = "";
		if (this.name == null)
			errorNull = "Name";
		else if (this.world == null)
			errorNull = "World";
		else if (this.firstBase == null)
			errorNull = "FirstBase";
		else if (this.firstEntity == null)
			errorNull = "ListFirstEntity";
		else if (this.firstEntity[0] == null)
			errorNull = "FirstEntity1";
		else if (this.firstEntity[1] == null)
			errorNull = "FirstEntity2";
		else if (this.firstEntity[2] == null)
			errorNull = "FirstEntity3";
		else if (this.firstEntity[3] == null)
			errorNull = "FirstEntity4";
		else if (this.firstEntity[4] == null)
			errorNull = "FirstEntity5";
		else if (this.secondBase == null)
			errorNull = "ListSecondEntity";
		else if (this.secondEntity == null)
			errorNull = "SecondEntity1";
		else if (this.secondEntity[0] == null)
			errorNull = "SecondEntity2";
		else if (this.secondEntity[1] == null)
			errorNull = "SecondEntity3";
		else if (this.secondEntity[2] == null)
			errorNull = "SecondEntity4";
		else if (this.secondEntity[3] == null)
			errorNull = "SecondEntity5";
		else if (this.secondEntity[4] == null)
			errorNull = "SecondEntity6";
		//test
		if (!errorNull.isEmpty())
			throw new Exception("Value \""+errorNull+"\" is null, arena \""+name+"\" !");
	}
	
	public String getName() {
		return name;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Location getFirstBase() {
		return firstBase;
	}
	
	public Location[] getFirstEntity() {
		return firstEntity;
	}
	
	public Location getSecondBase() {
		return secondBase;
	}
	
	public Location[] getSecondEntity() {
		return secondEntity;
	}
	
}
