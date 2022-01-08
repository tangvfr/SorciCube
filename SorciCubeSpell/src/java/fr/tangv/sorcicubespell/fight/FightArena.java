package fr.tangv.sorcicubespell.fight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.tangv.sorcicubecore.configs.ArenaConfig;
import fr.tangv.sorcicubespell.SorciCubeSpell;

public class FightArena {

	private final String id;
	private final String name;
	private final World world;
	private final int radiusSpectator;
	private final Location firstBase;
	private final Location[] firstEntity;
	private final Location secondBase;
	private final Location[] secondEntity;
	
	public FightArena(ArenaConfig arena) throws Exception {
		this.id = arena.id.value;
		this.name = arena.name.value;
		this.world = Bukkit.getWorld(arena.world.value);
		this.radiusSpectator = arena.spectatorRadius.value;
		this.firstBase = SorciCubeSpell.convertLocation(arena.firstBase.base, world, 0, 0);
		this.firstEntity = new Location[5];
		this.firstEntity[0] = SorciCubeSpell.convertLocation(arena.firstBase.entity1, world, 0, 0);
		this.firstEntity[1] = SorciCubeSpell.convertLocation(arena.firstBase.entity2, world, 0, 0);
		this.firstEntity[2] = SorciCubeSpell.convertLocation(arena.firstBase.entity3, world, 0, 0);
		this.firstEntity[3] = SorciCubeSpell.convertLocation(arena.firstBase.entity4, world, 0, 0);
		this.firstEntity[4] = SorciCubeSpell.convertLocation(arena.firstBase.entity5, world, 0, 0);
		this.secondBase = SorciCubeSpell.convertLocation(arena.secondBase.base, world, 180, 0);
		this.secondEntity = new Location[5];
		this.secondEntity[0] = SorciCubeSpell.convertLocation(arena.secondBase.entity1, world, 180, 0);
		this.secondEntity[1] = SorciCubeSpell.convertLocation(arena.secondBase.entity2, world, 180, 0);
		this.secondEntity[2] = SorciCubeSpell.convertLocation(arena.secondBase.entity3, world, 180, 0);
		this.secondEntity[3] = SorciCubeSpell.convertLocation(arena.secondBase.entity4, world, 180, 0);
		this.secondEntity[4] = SorciCubeSpell.convertLocation(arena.secondBase.entity5, world, 180, 0);
		String errorNull = "";
		if (this.name == null)
			errorNull = "Name";
		else if (this.world == null)
			errorNull = "World";
		else if (this.radiusSpectator <= 0)
			errorNull = "Radius Spectator";
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
			throw new Exception("Value \""+errorNull+"\" is null, arena \""+id+"\" !");
	}

	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getRadiusSpectator() {
		return radiusSpectator;
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
