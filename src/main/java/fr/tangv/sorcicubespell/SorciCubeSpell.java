package fr.tangv.sorcicubespell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.tangv.sorcicubespell.manager.ManagerCards;
import fr.tangv.sorcicubespell.manager.ManagerClickNPC;
import fr.tangv.sorcicubespell.manager.ManagerDefaultDeck;
import fr.tangv.sorcicubespell.manager.ManagerGui;
import fr.tangv.sorcicubespell.manager.ManagerLobby;
import fr.tangv.sorcicubespell.manager.ManagerPacketCards;
import fr.tangv.sorcicubespell.manager.ManagerPlayers;
import fr.tangv.sorcicubespell.manager.MongoDBManager;
import fr.tangv.sorcicubespell.util.Config;
import fr.tangv.sorcicubespell.util.EnumTool;

public class SorciCubeSpell extends JavaPlugin {

	private Config message;
	private Config parameter;
	private Config enumConfig;
	private Config guiConfig;
	private EnumTool enumTool;
	private MongoDBManager mongo;
	private ManagerCards managerCards;
	private ManagerGui managerGuiAdmin;
	private ManagerPlayers managerPlayers;
	private ManagerDefaultDeck managerDefaultDeck;
	private ManagerClickNPC managerClickNPC;
	private ManagerPacketCards managerPacketCards;
	
	@Override
	public void onEnable() {
		//try for bug
		try {
			//init Config
			this.message = new Config(this, "message.yml");
			this.parameter = new Config(this, "parameter.yml");
			this.enumConfig = new Config(this, "enum.yml");
			this.guiConfig = new Config(this, "gui.yml");
			//init tool
			this.enumTool = new EnumTool(this.enumConfig);
			//init manager
			this.mongo = new MongoDBManager(parameter.getString("mongodb"), parameter.getString("database"));
			this.managerCards = new ManagerCards(this.mongo);
			this.managerGuiAdmin = new ManagerGui(this);
			this.managerPlayers = new ManagerPlayers(this);
			this.managerDefaultDeck = new ManagerDefaultDeck(this.mongo, this.managerCards);
			this.managerClickNPC = new ManagerClickNPC(this);
			this.managerPacketCards = new ManagerPacketCards(this);
			if (getParameter().getBoolean("is_lobby")) {
				new ManagerLobby(this);
			} else {
				//new Manager...
			}
		} catch (Exception e) {
			Bukkit.getLogger().warning(e.getMessage());
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers())
			player.closeInventory();
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public Config getMessage() {
		return message;
	}
	
	public Config getParameter() {
		return parameter;
	}

	public EnumTool getEnumTool() {
		return enumTool;
	}

	public Config getEnumConfig() {
		return enumConfig;
	}
	
	public Config gertGuiConfig() {
		return guiConfig;
	}

	public MongoDBManager getMongo() {
		return mongo;
	}
	
	public ManagerCards getManagerCards() {
		return managerCards;
	}
	
	public ManagerGui getManagerGui() {
		return managerGuiAdmin;
	}
	
	public ManagerPlayers getManagerPlayers() {
		return managerPlayers;
	}
	
	public ManagerDefaultDeck getManagerDefaultDeck() {
		return managerDefaultDeck;
	}
	
	public ManagerClickNPC getManagerClickNPC() {
		return managerClickNPC;
	}
	
	public ManagerPacketCards getManagerPacketCards() {
		return managerPacketCards;
	}
	
}
