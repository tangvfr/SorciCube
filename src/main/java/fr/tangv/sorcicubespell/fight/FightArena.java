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
	private Location firstEntity1;
	private Location firstEntity2;
	private Location firstEntity3;
	private Location firstEntity4;
	private Location firstEntity5;
	private Location secondBase;
	private Location secondEntity1;
	private Location secondEntity2;
	private Location secondEntity3;
	private Location secondEntity4;
	private Location secondEntity5;
	
	public FightArena(String name, ConfigurationSection config) {
		this.name = name;
		this.world = Bukkit.getWorld(config.getString("world"));
		this.firstBase = ((Vector) config.get("first.base")).toLocation(world, 0, 0);
		this.firstEntity1 = ((Vector) config.get("first.entity1")).toLocation(world, 0, 0);
		this.firstEntity2 = ((Vector) config.get("first.entity2")).toLocation(world, 0, 0);
		this.firstEntity3 = ((Vector) config.get("first.entity3")).toLocation(world, 0, 0);
		this.firstEntity4 = ((Vector) config.get("first.entity4")).toLocation(world, 0, 0);
		this.firstEntity5 = ((Vector) config.get("first.entity5")).toLocation(world, 0, 0);
		this.secondBase = ((Vector) config.get("second.base")).toLocation(world, 180, 0);
		this.secondEntity1 = ((Vector) config.get("second.entity1")).toLocation(world, 180, 0);
		this.secondEntity2 = ((Vector) config.get("second.entity2")).toLocation(world, 180, 0);
		this.secondEntity3 = ((Vector) config.get("second.entity3")).toLocation(world, 180, 0);
		this.secondEntity4 = ((Vector) config.get("second.entity4")).toLocation(world, 180, 0);
		this.secondEntity5 = ((Vector) config.get("second.entity5")).toLocation(world, 180, 0);
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
	
	public Location getFirstEntity1() {
		return firstEntity1;
	}
	
	public Location getFirstEntity2() {
		return firstEntity2;
	}
	
	public Location getFirstEntity3() {
		return firstEntity3;
	}
	
	public Location getFirstEntity4() {
		return firstEntity4;
	}
	
	public Location getFirstEntity5() {
		return firstEntity5;
	}
	
	public Location getSecondBase() {
		return secondBase;
	}
	
	public Location getSecondEntity1() {
		return secondEntity1;
	}
	
	public Location getSecondEntity2() {
		return secondEntity2;
	}
	
	public Location getSecondEntity3() {
		return secondEntity3;
	}
	
	public Location getSecondEntity4() {
		return secondEntity4;
	}
	
	public Location getSecondEntity5() {
		return secondEntity5;
	}
	
}
